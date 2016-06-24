package view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**不滑动的ViewPager
 * Created by Administrator on 2016/6/12.
 */
public class NoScrollViewPager extends ViewPager{
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return true;
    }

    //不拦截子控件的手势事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
