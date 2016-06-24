package tools;

import android.os.Handler;
import android.support.v4.view.ViewPager;

/**图片滚动任务类
 * Created by Administrator on 2016/6/17.
 */
public class ScrollImageTask extends Handler implements Runnable{

    private ViewPager mViewPager;
    private int mScrollTime;

    /**
     * 图片开始滚动
     * @param scrollTime
     */
    public void startImageScroll(ViewPager viewPager,int scrollTime){
        this.mViewPager=viewPager;
        this.mScrollTime=scrollTime;
        stopImageScroll();
        postDelayed(this,mScrollTime);

    }
    /**
     * 停止图片滚动
     */
    public void stopImageScroll(){
        removeCallbacksAndMessages(null);
    }
    @Override
    public void run() {
        //控制轮播图的显示
        mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1)
                % mViewPager.getAdapter().getCount());
        postDelayed(this,(mScrollTime));
    }
}