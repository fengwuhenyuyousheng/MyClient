package base;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.test.yang.myclient.R;

import activity.MainActivity;

/**五个子标签的基类
 * Created by Administrator on 2016/6/12.
 */
public class BasePager {

    public Activity mActivity;
    public ImageButton mMenuImageButton;
    public TextView mTitleBarTextView;
    public FrameLayout mFragmentContent;
    public View mRootView;

    public BasePager(Activity activity){
        mActivity=activity;
        mRootView=initView();
    }

    //初始化页面布局
    private View initView() {
        View view=View.inflate(mActivity, R.layout.base_pager,null);
        mMenuImageButton= (ImageButton) view.findViewById(R.id.menu_image_button);
        setMenuImageButtonClick();
        mTitleBarTextView= (TextView) view.findViewById(R.id.title_bar_text);
        mFragmentContent= (FrameLayout) view.findViewById(R.id.fragment_content_fl);
        return view;
    }

    //初始化数据
    public void initData(){

    }

    /**
     * 设置菜单图标点击事件
     */
    public void setMenuImageButtonClick(){
        MainActivity mainActivity= (MainActivity) mActivity;
        final DrawerLayout drawerLayout=mainActivity.getMainDrawerLayout();
        mMenuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawers();
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

}
