package base;

import android.app.Activity;
import android.view.View;

/**这是侧边栏菜单详情页
 * Created by Administrator on 2016/6/15.
 */
public abstract class BaseDetailsPager {

    public Activity mActivity;
    public View mRootView;

    public BaseDetailsPager(Activity activity){
        mActivity=activity;
        mRootView=initView();
    }

    public abstract void initData();
    public abstract View initView();
}
