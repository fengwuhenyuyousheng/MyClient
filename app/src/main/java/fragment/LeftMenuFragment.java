package fragment;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.test.yang.myclient.R;

import java.util.ArrayList;

import activity.MainActivity;
import base.impl.basepager.NewsPager;
import model.NewsMenu;

/**
 * 这是侧边栏的Fragment
 * Created by Administrator on 2016/6/12.
 */
public class LeftMenuFragment extends MyBaseFragment {

    private LeftMenuAdapter mLeftMenuAdapter;
    private ListView mLeftMenu;
    private ArrayList<NewsMenu.NewsData> newsMenuDataArrayList;
    private int mCurrentTag=0;


    @Override
    protected void initData() {

    }

    @Override
    protected View initView() {
        mCurrentTag=0;
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        mLeftMenu = (ListView) view.findViewById(R.id.lv_left_menu);
        mLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentTag=position;
                mLeftMenuAdapter.notifyDataSetChanged();
                onLeftMenu();
                //设置侧边栏选择后的详情页
                setMenuDetailsContent(position);
            }
        });
        return view;
    }


    /**
     * 设置新闻侧边栏选择后的详情页
     * @param position
     */
    private void setMenuDetailsContent(int position) {
        MainActivity mainActivity= (MainActivity) mActivity;
        ContentFragment contentFragment=mainActivity.getContentFragment();
        NewsPager newsPager=contentFragment.getCurrentNewsPager();
        newsPager.setDetailsContent(position);
    }


    public void initNewsMenuData(ArrayList<NewsMenu.NewsData> newsMenusData) {
        newsMenuDataArrayList = newsMenusData;
        if(mLeftMenuAdapter==null) {
            mLeftMenuAdapter = new LeftMenuAdapter();
            mLeftMenu.setAdapter(mLeftMenuAdapter);
        }else{
            mLeftMenuAdapter.notifyDataSetChanged();
        }
    }


    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
//            Log.d("LeftMenuFragment","返回数组长度:"+newsMenuDataArrayList.size());
            return newsMenuDataArrayList.size();
        }

        @Override
        public NewsMenu.NewsData getItem(int position) {
            return newsMenuDataArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            Log.d("LeftMenuFragment","初始化界面");
            View view = View.inflate(mActivity, R.layout.item_left_menu, null);
            TextView leftMenuItem = (TextView) view.findViewById(R.id.tv_left_menu_item);

            if(mCurrentTag==position){
//                Log.d("LeftMenuFragment","设置为红色");
                leftMenuItem.setEnabled(true);
            }else{
//                Log.d("LeftMenuFragment","设置为白色");
                leftMenuItem.setEnabled(false);
            }
            leftMenuItem.setText(getItem(position).getTitle());
            return view;
        }
    }

    public void onLeftMenu(){
        MainActivity mainActivity= (MainActivity) mActivity;
        DrawerLayout drawerLayout=mainActivity.getMainDrawerLayout();
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
