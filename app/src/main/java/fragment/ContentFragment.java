package fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.test.yang.myclient.R;

import java.util.ArrayList;

import activity.MainActivity;
import base.BasePager;
import base.impl.basepager.GovernmentPager;
import base.impl.basepager.HomePager;
import base.impl.basepager.NewsPager;
import base.impl.basepager.SettingPager;
import base.impl.basepager.SmartPager;
import view.NoScrollViewPager;

/**这是侧边栏的Fragment
 * Created by Administrator on 2016/6/12.
 */
public class ContentFragment extends MyBaseFragment{

    private NoScrollViewPager mContentViewPager;
    private RadioGroup mBottomTabRadioGroup;
    private ArrayList<BasePager> mBasePager;
    private ContentPagerAdapter contentPagerAdapter;

    @Override
    protected void initData() {
        mBasePager=new ArrayList<BasePager>();
        mBasePager.add(new HomePager(mActivity));
        mBasePager.add(new NewsPager(mActivity));
        mBasePager.add(new SmartPager(mActivity));
        mBasePager.add(new GovernmentPager(mActivity));
        mBasePager.add(new SettingPager(mActivity));

        if(contentPagerAdapter==null){
            contentPagerAdapter=new ContentPagerAdapter();
            mContentViewPager.setAdapter(contentPagerAdapter);
        }else{
            contentPagerAdapter.notifyDataSetChanged();
        }
        mBasePager.get(0).initData();
        setLeftMenuEnable(false);
        setRadioGroupListener();
        setPagerListener();
    }

    /**
     * 设置ViewPager监听页面
     */
    private void setPagerListener(){
        mContentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager basePager=mBasePager.get(position);
                basePager.initData();
                if (position == 0 || position == mBasePager.size() - 1) {
                    // 首页和设置页要禁用侧边栏
                    setLeftMenuEnable(false);
                } else {
                    // 其他页面开启侧边栏
                    setLeftMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置标签组监听事件
     */
    private void setRadioGroupListener(){
        mBottomTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.home_radio_button:
                        mContentViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.news_radio_button:
                        mContentViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.smart_radio_button:
                        mContentViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.government_radio_button:
                        mContentViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.setting_radio_button:
                        mContentViewPager.setCurrentItem(4,false);
                        break;
                }
            }
        });
    }

    @Override
    protected View initView() {

        View view = View.inflate(mActivity,R.layout.fragment_content,null);
        mContentViewPager= (NoScrollViewPager) view.findViewById(R.id.content_view_pager);
        mBottomTabRadioGroup= (RadioGroup) view.findViewById(R.id.bottom_tab_radio_group);
        return view;
    }

    /**
     * 开启或者禁用侧边栏划出
     * @param enable
     */
    private void setLeftMenuEnable(boolean enable){
        MainActivity mainActivity= (MainActivity) mActivity;
        DrawerLayout drawerLayout=mainActivity.getMainDrawerLayout();
        if(enable){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else{
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    class ContentPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mBasePager.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            BasePager basePager=mBasePager.get(position);
            View view=basePager.mRootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((View) object);
        }

    }

    public NewsPager getCurrentNewsPager(){
        return (NewsPager) mBasePager.get(1);
    }
}
