package activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import com.test.yang.myclient.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fragment.ContentFragment;
import fragment.LeftMenuFragment;

public class MainActivity extends FragmentActivity{

    private static final String TAG_LEFT_MENU ="TAG_LEFT_MENU" ;
    private static final String TAG_CONTENT ="TAG_CONTENT" ;

    private LeftMenuFragment mLeftMenuFragment;
    private ContentFragment mContentFragment;



    @InjectView(R.id.left_menu_frame_layout)
    FrameLayout mLeftMenuFrameLayout;

    @InjectView(R.id.main_drawer_layout)
    DrawerLayout mMainDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initFragment();
    }

    /*
    public void menuOpenOrClose(View view){
        if(mMainDrawerLayout.isDrawerOpen(Gravity.LEFT)){
//            Log.d("MainActivity","关闭侧边栏");
            mMainDrawerLayout.closeDrawers();
        }else{
            mMainDrawerLayout.openDrawer(Gravity.LEFT);
//            Log.d("MainActivity","打开侧边栏");
        }
    }
     */

    /**
     * 初始化Fragment
     */
    public void initFragment() {
        mLeftMenuFragment = new LeftMenuFragment();
        mContentFragment = new ContentFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.left_menu_frame_layout, mLeftMenuFragment, TAG_LEFT_MENU);
        fragmentTransaction.replace(R.id.content_frame_layout, mContentFragment, TAG_CONTENT);
        fragmentTransaction.commit();
    }

    /**
     * 获取主Activity的DrawerLayout
     */
    public DrawerLayout getMainDrawerLayout(){
        return mMainDrawerLayout;
    }


    /**
     * 获取主Activity的LeftMenuFragment
     */
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment= (LeftMenuFragment) fragmentManager.findFragmentByTag(TAG_LEFT_MENU);
        return leftMenuFragment;
    }

    /**
     * 获取主Activity的ContentFragment
     */
    public ContentFragment getContentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ContentFragment contentFragment= (ContentFragment) fragmentManager.findFragmentByTag(TAG_CONTENT);
        return contentFragment;
    }

}
