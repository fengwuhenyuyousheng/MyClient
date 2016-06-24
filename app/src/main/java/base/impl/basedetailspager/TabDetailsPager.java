package base.impl.basedetailspager;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.test.yang.myclient.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import Global.GlobalContents;
import activity.NewsReadingActivity;
import base.BaseDetailsPager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import model.NewsMenu;
import model.TabNewDetails;
import tools.CacheTool;
import tools.DensityTool;
import tools.ScrollImageTask;
import view.InterceptScrollViewPager;
import view.RefreshListView;

/**这是页面标签
 * Created by Administrator on 2016/6/15.
 */
public class TabDetailsPager extends BaseDetailsPager{

    private NewsMenu.Children mChildren;

    @InjectView(R.id.vp_tab_details_scroll_image)
    InterceptScrollViewPager mTabDetailsScrollImageViewPager;
    @InjectView(R.id.tv_tab_details_pager_image_desc)
    TextView mTabDetailsPagerImageDesc;
    @InjectView(R.id.ll_tab_details_pager_image_point)
    LinearLayout mTabDetailsPagerImagePoint;

    private RefreshListView mTabDetailsPagerNewsList;

    private Gson mGson;
    private ArrayList<TabNewDetails.TabNewDetailsData.TabNewDetailsDataTopNews>
            mTabNewDetailsDataTopNewsList;
    private ArrayList<TabNewDetails.TabNewDetailsData.TabNewDetailsDataNews>
            mTabNewDetailsDataNews;
    private ScrollImageViewPagerAdapter mScrollImageViewPagerAdapter;
    private ListNewsAdapter mListNewsAdapter;
    private int mPicSelectIndex;
    private ScrollImageTask mScrollImageTask;
    private RefreshListView.OnRefreshDataListener mOnRefreshListener;
    private String mLoadingMoreUrl="";

    public TabDetailsPager(Activity activity, NewsMenu.Children children) {
        super(activity);
        mChildren=children;
        mGson=new Gson();
    }

    @Override
    public void initData() {
        if(mScrollImageViewPagerAdapter==null) {
            mScrollImageViewPagerAdapter = new ScrollImageViewPagerAdapter();
        }

        if(mTabNewDetailsDataNews==null){
            mTabNewDetailsDataNews=new ArrayList
                    <TabNewDetails.TabNewDetailsData.TabNewDetailsDataNews>();
        }
        if(mTabNewDetailsDataTopNewsList==null) {
            mTabNewDetailsDataTopNewsList = new ArrayList
                    <TabNewDetails.TabNewDetailsData.TabNewDetailsDataTopNews>();
        }
        if(mListNewsAdapter==null){
            mListNewsAdapter=new ListNewsAdapter();
        }
        mPicSelectIndex=0;
        mTabDetailsScrollImageViewPager.setAdapter(mScrollImageViewPagerAdapter);
        mTabDetailsPagerNewsList.setAdapter(mListNewsAdapter);
        mOnRefreshListener=new RefreshListView.OnRefreshDataListener() {
            @Override
            public void refreshData(){
                getTabDetailsDataFromServer(GlobalContents.SERVER_URL+mChildren.getUrl());
            }
            @Override
            public void loadingMore(){
//                Log.d("RefreshListViewLog","调用RefreshListView的处理函数");
                if(!TextUtils.isEmpty(mLoadingMoreUrl)){
                    getTabDetailsDataFromServer(GlobalContents.SERVER_URL+mLoadingMoreUrl);
                }else{
                    Toast.makeText(mActivity,"没有更多的数据了",Toast.LENGTH_SHORT).show();
                    mTabDetailsPagerNewsList.refreshStateFinish();
                }
            }
        };
        mTabDetailsPagerNewsList.setOnRefreshDataListener(mOnRefreshListener);
        mTabDetailsPagerNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String oldNewsDetailsReadUrl=mTabNewDetailsDataNews.get(position).url;
                String newNewsDetailsReadUrl=oldNewsDetailsReadUrl.replace(GlobalContents.OLD_IP,GlobalContents.NEW_IP);
                Intent starNewsReadingActivity=new Intent(mActivity, NewsReadingActivity.class);
                starNewsReadingActivity.putExtra("news_reading_url",newNewsDetailsReadUrl);
                mActivity.startActivity(starNewsReadingActivity);
            }
        });
        //给轮播图添加页面切换事件
        mTabDetailsScrollImageViewPager.addOnPageChangeListener
                (new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mPicSelectIndex = position;
                setPicDescAndPointSelect(mPicSelectIndex);
            }
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        String cacheString=null;
        cacheString=CacheTool.getCacheFromFile(mActivity,mChildren.getUrl());
        if(!TextUtils.isEmpty(cacheString)){
            TabNewDetails tabNewDetails=analyzeTabNewsDetailsJson(cacheString);
            setTabNewsDetailsToView(tabNewDetails,GlobalContents.SERVER_URL+mChildren.getUrl());
        }
        getTabDetailsDataFromServer(GlobalContents.SERVER_URL+mChildren.getUrl());

    }

    @Override
    public View initView() {

        View imageScrollView=View.inflate(mActivity,R.layout.tab_image_scroll,null);
        ButterKnife.inject(this,imageScrollView);
        View view=View.inflate(mActivity, R.layout.tab_details_pager,null);
        mTabDetailsPagerNewsList= (RefreshListView) view.findViewById(R.id.lv_tab_details_pager_news_list);
        mTabDetailsPagerNewsList.setIsUseDownRefresh(true);
        mTabDetailsPagerNewsList.addHeaderView(imageScrollView);
        return view;
    }

    /**
     * 从服务器获取新闻标签页详情数据
     * @param url 网络请求的地址
     */
    private void getTabDetailsDataFromServer(final String url){
        x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
//                Log.d("TabDetailsPagerGetDate","网络连接成功");
                if(!TextUtils.isEmpty(result)) {
                    CacheTool.setCacheToFile(mActivity,url , result);
                    TabNewDetails tabNewDetails=analyzeTabNewsDetailsJson(result);
                    setTabNewsDetailsToView(tabNewDetails,url);
                    //将加载更多的网址更新，方便下次的判断
                    mLoadingMoreUrl=tabNewDetails.data.more;
                    Toast.makeText(mActivity,"加载数据成功",Toast.LENGTH_SHORT).show();
                    mTabDetailsPagerNewsList.refreshStateFinish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("TabDetailsPagerGetDate","网络连接失败");
                mTabDetailsPagerNewsList.refreshStateFinish();
                Toast.makeText(mActivity,"加载数据失败",Toast.LENGTH_SHORT).show();
                mTabDetailsPagerNewsList.refreshStateFinish();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
//                Log.d("TabDetailsPagerGetDate","网络连接完成");
            }

        });
    }

    /**
     * 解析获取到的数据
     * @param json
     * @return
     */
    private TabNewDetails analyzeTabNewsDetailsJson(String json){
        return mGson.fromJson(json, TabNewDetails.class);
    }


    /**
     * 将新闻标签页详情数据设置到各个组件界面
     * @param tabNewDetails
     */
    private void setTabNewsDetailsToView(TabNewDetails tabNewDetails ,String url){
        //初始化滚动图片
        setScrollImageViewPager(tabNewDetails.data.topnews);
        //初始化滚动图片的点
        initPoints();
        //设置选中的滚动图片点和描述信息
        setPicDescAndPointSelect(mPicSelectIndex);
        //设置图片开始自动滚动
        setImageAutoScroll();
        //设置新闻列表数据
        setTabNewsListViewData(tabNewDetails.data.news,url);
    }

    private void setTabNewsListViewData(ArrayList<TabNewDetails.TabNewDetailsData.
            TabNewDetailsDataNews> tabNewDetailsDataNews,String url) {
        if(TextUtils.isEmpty(mLoadingMoreUrl)) {
            mTabNewDetailsDataNews = tabNewDetailsDataNews;
        }else if(url.contains(mLoadingMoreUrl)){
            mTabNewDetailsDataNews.addAll(tabNewDetailsDataNews);
        }
        mListNewsAdapter.notifyDataSetChanged();
    }

    /**
     * 设置图片开始自动滚动
     */
    private void setImageAutoScroll() {
        if(mScrollImageTask==null){
            mScrollImageTask=new ScrollImageTask();
        }
        mScrollImageTask.startImageScroll(mTabDetailsScrollImageViewPager,4000);
    }

    /**
     * 给滚动图片设置数据
     * @param tabNewDetailsDataTopNewsList
     */
    private void setScrollImageViewPager(ArrayList<TabNewDetails.TabNewDetailsData.TabNewDetailsDataTopNews>
                                                 tabNewDetailsDataTopNewsList ){
        mTabNewDetailsDataTopNewsList=tabNewDetailsDataTopNewsList;
        mScrollImageViewPagerAdapter.notifyDataSetChanged();

    }

    /**
     * 初始化滑动图片的点
     */
    private void initPoints() {
        //清空以前存在的点
        mTabDetailsPagerImagePoint.removeAllViews();
        //轮播图有几张 加几个点
        for (int i = 0; i < mTabNewDetailsDataTopNewsList.size() ; i++) {
            View pointView = new View(mActivity);
            //设置点的背景选择器
            pointView.setBackgroundResource(R.drawable.white_red_point_selector);
            pointView.setEnabled(false);//默认是默认的灰色点

            //设置点的大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityTool.dip2px(mActivity,5), DensityTool.dip2px(mActivity,5));
            //设置点与点直接的间距
            params.leftMargin = DensityTool.dip2px(mActivity,10);

            //设置参数
            pointView.setLayoutParams(params);
            mTabDetailsPagerImagePoint.addView(pointView);
        }
    }

    /**
     * 设置选中的滚动图片点和描述信息
     * @param picSelectIndex
     */
    private void setPicDescAndPointSelect(int picSelectIndex) {
        //设置描述信息
        mTabDetailsPagerImageDesc.setText(mTabNewDetailsDataTopNewsList.get(picSelectIndex).title);

        //设置点是否是选中
        for (int i = 0; i < mTabNewDetailsDataTopNewsList.size(); i++) {
            mTabDetailsPagerImagePoint.getChildAt(i).setEnabled(i == picSelectIndex);
        }

    }



    class ScrollImageViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mTabNewDetailsDataTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView scrollImageView=new ImageView(mActivity);
            scrollImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            scrollImageView.setImageResource(R.drawable.news_pic_default);

            TabNewDetails.TabNewDetailsData.TabNewDetailsDataTopNews tabNewDetailsDataTopNews
                    =mTabNewDetailsDataTopNewsList.get(position);
            String scrollImageViewOldUrl=tabNewDetailsDataTopNews.topimage;
            String scrollImageViewNewUrl=scrollImageViewOldUrl.replace(GlobalContents.OLD_IP,GlobalContents.NEW_IP);
//            Log.d("ScrollImagePagerAdapter","加载第"+position+"图片的地址"+scrollImageViewNewUrl);
            x.image().bind(scrollImageView,scrollImageViewNewUrl);
            scrollImageView.setOnTouchListener(new View.OnTouchListener() {

                private float downX;
                private float downY;
                private long downTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://按下停止轮播
                            downX = event.getX();
                            downY = event.getY();
                            downTime = System.currentTimeMillis();
                            mScrollImageTask.stopImageScroll();
                            break;
                        case MotionEvent.ACTION_CANCEL://事件取消
                            setImageAutoScroll();
                            break;
                        case MotionEvent.ACTION_UP://松开
                            float upX = event.getX();
                            float upY = event.getY();

                            if (upX == downX && upY == downY) {
                                long upTime = System.currentTimeMillis();
                                if (upTime - downTime < 500) {
                                    //点击
                                    Log.d("scrollImageView", "被单击了。。。。。");
                                }
                            }
                            setImageAutoScroll();
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            container.addView(scrollImageView);
            return scrollImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView)object);
        }
    }

    private class ListNewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTabNewDetailsDataNews.size();
        }

        @Override
        public Object getItem(int position) {
            return mTabNewDetailsDataNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_tab_details_news_list, null);
                holder = new ViewHolder();
//                Log.d("ListNewsAdapter","初始化新闻列表项");
                holder.tabDetailsNewsListIcon = (ImageView) convertView.findViewById(R.id.iv_tab_news_list_item_icon);
                holder.tabDetailsNewsListImage = (ImageView) convertView.findViewById(R.id.iv_tab_news_list_item_Image);
                holder.tabDetailsNewsListTitle = (TextView) convertView.findViewById(R.id.tv_tab_news_list_item_title);
                holder.tabDetailsNewsListTime = (TextView) convertView.findViewById(R.id.tv_tab_news_list_item_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //设置数据

            TabNewDetails.TabNewDetailsData.TabNewDetailsDataNews  tabNewDetailsDataNews=
                    mTabNewDetailsDataNews.get(position);
            //设置标题
            holder.tabDetailsNewsListTitle.setText(tabNewDetailsDataNews.title);

            //设置时间
            holder.tabDetailsNewsListTime.setText(tabNewDetailsDataNews.pubdate);

            //设置图片
            String newsImageViewOldUrl=tabNewDetailsDataNews.listimage;
            String newsImageViewNewUrl=newsImageViewOldUrl.replace(GlobalContents.OLD_IP,GlobalContents.NEW_IP);
//            Log.d("ListNewsAdapter","加载第"+position+"图片的地址"+newsImageViewNewUrl);
            x.image().bind(holder.tabDetailsNewsListImage,newsImageViewNewUrl );

            return convertView;
        }

        private class ViewHolder{
            ImageView tabDetailsNewsListImage;
            TextView tabDetailsNewsListTitle;
            TextView tabDetailsNewsListTime;
            ImageView tabDetailsNewsListIcon;
        }
    }


}
