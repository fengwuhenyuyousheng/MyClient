package base.impl.basepager;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import Global.GlobalContents;
import activity.MainActivity;
import base.BaseDetailsPager;
import base.BasePager;
import base.impl.basedetailspager.NewsMenuDetailsFour;
import base.impl.basedetailspager.NewsMenuDetailsOne;
import base.impl.basedetailspager.NewsMenuDetailsThree;
import base.impl.basedetailspager.NewsMenuDetailsTwo;
import fragment.LeftMenuFragment;
import model.NewsMenu;
import tools.CacheTool;

/**这是新闻页
 * Created by Administrator on 2016/6/12.
 */
public class NewsPager extends BasePager {

    private NewsMenu mNewsMenu;
    private ArrayList<BaseDetailsPager> mBaseDetailsPagers;


    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData(){

        String cacheString=null;
        cacheString=CacheTool.getCacheFromFile(mActivity,GlobalContents.CATEGORY_URL);
        if(!TextUtils.isEmpty(cacheString)){
//            Log.d("NewsPager","读取到的缓存："+cacheString);
            analyzeNewsJson(cacheString);
            setBaseDetailsPagersViews();
            setLeftNewsMenuData();
        }
        getDataFromServer();

    }


    /**
     * 从服务器获取数据
     */
    private void getDataFromServer(){
        x.http().get(new RequestParams(GlobalContents.CATEGORY_URL), new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if(!TextUtils.isEmpty(result)) {
                    CacheTool.setCacheToFile(mActivity, GlobalContents.CATEGORY_URL, result);
                    analyzeNewsJson(result);
                    setBaseDetailsPagersViews();
                    setLeftNewsMenuData();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity,"网络连接失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }

        });
    }

    /**
     * 解析获取到的数据
     * @param json
     */
    private void analyzeNewsJson(String json){
        Gson gson=new Gson();
        mNewsMenu=gson.fromJson(json, NewsMenu.class);
    }
    /**
     * 给页面设置数据
     */

    private void setBaseDetailsPagersViews(){
        if(mBaseDetailsPagers==null) {
            mBaseDetailsPagers = new ArrayList<BaseDetailsPager>();
        }
        mBaseDetailsPagers.add(new NewsMenuDetailsOne(mActivity,mNewsMenu.getData().get(0)));
        mBaseDetailsPagers.add(new NewsMenuDetailsTwo(mActivity,mNewsMenu.getData().get(1)));
        mBaseDetailsPagers.add(new NewsMenuDetailsThree(mActivity,mNewsMenu.getData().get(2)));
        mBaseDetailsPagers.add(new NewsMenuDetailsFour(mActivity,mNewsMenu.getData().get(3)));
        setDetailsContent(0);
    }

    /**
     * 给侧面菜单栏设置数据
     */
    private void setLeftNewsMenuData(){
        MainActivity mainActivity=(MainActivity)mActivity;
        LeftMenuFragment leftMenuFragment=mainActivity.getLeftMenuFragment();
        leftMenuFragment.initNewsMenuData(mNewsMenu.getData());

    }

    /**
     * 根据侧边栏菜单选择设置详情页信息
     * @param position
     */
    public void setDetailsContent(int position){
        BaseDetailsPager baseDetailsPager=mBaseDetailsPagers.get(position);
        baseDetailsPager.initData();
        View view=baseDetailsPager.mRootView;
        mFragmentContent.removeAllViews();
        mFragmentContent.addView(view);
        mTitleBarTextView.setText(mNewsMenu.getData().get(position).getTitle());
    }
}
