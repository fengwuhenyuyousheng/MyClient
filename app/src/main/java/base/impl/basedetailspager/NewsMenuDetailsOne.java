package base.impl.basedetailspager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.test.yang.myclient.R;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import base.BaseDetailsPager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import model.NewsMenu;

/**
 * 这是新闻中心侧边栏第一个详情页
 * Created by Administrator on 2016/6/15.
 */
public class NewsMenuDetailsOne extends BaseDetailsPager {

    @InjectView(R.id.vp_news_menu_details)
    ViewPager mNewsMenuDetails;
    @InjectView(R.id.tab_page_indicator_news_details)
    TabPageIndicator mTabPageIndicator;
    @InjectView(R.id.ib_news_cate_arr_next)
    ImageButton mNewsCateArrNextImageButton;
    @InjectView(R.id.ib_news_cate_arr_back)
    ImageButton mNewsCateArrBackImageButton;

    private ArrayList<NewsMenu.Children> mNewDateChildren;
    private NewsMenuDetailsPagerAdapter mNewsMenuDetailsPagerAdapter;
    private int mViewPagerCurrentItem = 0;

    public NewsMenuDetailsOne(Activity activity, NewsMenu.NewsData newsDataOne) {
        super(activity);
        mNewDateChildren = newsDataOne.getChildren();
    }

    @Override
    public void initData() {
//        Log.d("NewsMenuDetailsOne","设置页面数据:"+mNewDateOne.getTitle());
        if (mNewsMenuDetailsPagerAdapter == null) {
            mNewsMenuDetailsPagerAdapter = new NewsMenuDetailsPagerAdapter();
        }
        mNewsMenuDetails.setAdapter(mNewsMenuDetailsPagerAdapter);
        mNewsMenuDetails.setCurrentItem(mViewPagerCurrentItem);
        mNewsMenuDetails.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPagerCurrentItem = position;
                if (position == mNewDateChildren.size() - 1) {
                    mNewsCateArrNextImageButton.setVisibility(View.INVISIBLE);
                } else if (position == 0) {
                    mNewsCateArrBackImageButton.setVisibility(View.INVISIBLE);
                } else {
                    mNewsCateArrNextImageButton.setVisibility(View.VISIBLE);
                    mNewsCateArrBackImageButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabPageIndicator.setViewPager(mNewsMenuDetails);
    }


    @OnClick(R.id.ib_news_cate_arr_next)
    public void nextPager(View view) {
        if (mViewPagerCurrentItem < (mNewDateChildren.size() - 1)) {
            mNewsMenuDetails.setCurrentItem(mNewsMenuDetails.getCurrentItem() + 1);
        }
    }

    @OnClick(R.id.ib_news_cate_arr_back)
    public void backPager(View view) {
        if (mViewPagerCurrentItem > 0) {
            mNewsMenuDetails.setCurrentItem(mNewsMenuDetails.getCurrentItem() - 1);
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.news_menu_details, null);
        ButterKnife.inject(this, view);
        return view;
    }

    class NewsMenuDetailsPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mNewDateChildren.size();
        }

        //获取页面标签调用该方法
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewDateChildren.get(position).getTitle();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailsPager tabDetailsPager = new TabDetailsPager(mActivity, mNewDateChildren.get(position));
            View view = tabDetailsPager.mRootView;
            tabDetailsPager.initData();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
