package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.test.yang.myclient.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.guide_view_pager)
    ViewPager mGuideViewPager;
    @InjectView(R.id.gray_point_linear_layout)
    LinearLayout mGrayPointLinearLayout;
    @InjectView(R.id.start_button)
    Button mStartButton;
    @InjectView(R.id.green_point_image_view)
    ImageView mGreenPoint;

    //小红点移动的距离
    private int mPointDis;
    private ArrayList<ImageView> mImageViewList;
    private int[] guideDrawable=new int[]
            {R.drawable.gudie1,R.drawable.gudie2,R.drawable.gudie3,R.drawable.gudie4,};
    private MyPagerAdapter mMyPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        mImageViewList=new ArrayList<ImageView>();
        initData();
    }

    public void startMainActivity(View view){
        Intent startMainActivity=new Intent(GuideActivity.this,MainActivity.class);
        startActivity(startMainActivity);
        finish();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        for(int i=0;i<guideDrawable.length;i++){
            ImageView guideImageView= new ImageView(this);
            guideImageView.setBackgroundResource(guideDrawable[i]);
            mImageViewList.add(guideImageView);
            ImageView grayPointImageView=new ImageView(this);
            grayPointImageView.setImageResource(android.R.drawable.presence_invisible);
            // 初始化布局参数, 宽高包裹内容,父控件是谁,就是谁声明的布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                // 从第二个点开始设置左边距
                params.leftMargin = 10;
            }

            grayPointImageView.setLayoutParams(params);// 设置布局参数
            mGrayPointLinearLayout.addView(grayPointImageView);
        }
        mMyPagerAdapter=new MyPagerAdapter();
        if(mMyPagerAdapter!=null){
            mGuideViewPager.setAdapter(mMyPagerAdapter);
            mGuideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // 更新小红点距离
                    int leftMargin = (int) (mPointDis * positionOffset) + position
                            * mPointDis;// 计算小红点当前的左边距
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mGreenPoint
                            .getLayoutParams();
                    params.leftMargin = leftMargin;// 修改左边距

                    // 重新设置布局参数
                    mGreenPoint.setLayoutParams(params);
                }

                @Override
                public void onPageSelected(int position) {
//                    Log.d("GuideActivity","当前页:"+position);
                    if(position==(mImageViewList.size()-1)){
                        mStartButton.setVisibility(View.VISIBLE);
//                        Log.d("GuideActivity","显示按钮");
                    }else{
                        mStartButton.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        // 计算两个圆点的距离
        // 移动距离=第二个圆点left值 - 第一个圆点left值
        // measure->layout(确定位置)->draw(activity的onCreate方法执行结束之后才会走此流程)
        // mPointDis = llContainer.getChildAt(1).getLeft()
        // - llContainer.getChildAt(0).getLeft();
        // System.out.println("圆点距离:" + mPointDis);

        // 监听layout方法结束的事件,位置确定好之后再获取圆点间距
        // 视图树
        mGreenPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // 移除监听,避免重复回调
                        mGreenPoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        // ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        // layout方法执行结束的回调
                        mPointDis = mGrayPointLinearLayout.getChildAt(1).getLeft()
                                - mGrayPointLinearLayout.getChildAt(0).getLeft();
//                        System.out.println("圆点距离:" + mPointDis);
                    }
                });
    }
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position){
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }


    }
}
