package base.impl.basedetailspager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import base.BaseDetailsPager;
import model.NewsMenu;

/** 这是新闻中心侧边栏第四个详情页
 * Created by Administrator on 2016/6/15.
 */
public class NewsMenuDetailsFour extends BaseDetailsPager{


    private NewsMenu.NewsData mNewDateFour;
    private TextView mView;

    public NewsMenuDetailsFour(Activity activity, NewsMenu.NewsData newsDataFour) {
        super(activity);
        mNewDateFour = newsDataFour;
    }

    @Override
    public void initData() {
        mView.setText("详情页--" + mNewDateFour.getTitle());

    }

    @Override
    public View initView() {
        mView = new TextView(mActivity);
        mView.setTextColor(Color.RED);
        mView.setTextSize(25);
        mView.setGravity(Gravity.CENTER);
        return mView;
    }
}
