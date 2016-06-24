package view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**这是请求父控件不拦截事件的ViewPager
 * Created by Administrator on 2016/6/16.
 */
public class InterceptScrollViewPager extends ViewPager{

    private float downX;
    private float downY;

    public InterceptScrollViewPager(Context context) {
        super(context);
    }

    public InterceptScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //申请父控件不拦截自己的手势事件
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                getParent().requestDisallowInterceptTouchEvent(true);
                //记录下点的位置
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE://移动
                //获取移动的位置坐标
                float moveX = ev.getX();
                float moveY = ev.getY();

                float dx = moveX  - downX;
                float dy = moveY  - downY;

                //横向移动
                if (Math.abs(dx) > Math.abs(dy)) {
                    //如果在第一个页面，并且是从左往右滑动,让父控件拦截我
                    if (getCurrentItem() == 0 && dx > 0) {
                        //由父控件处理该事件
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (getCurrentItem() == getAdapter().getCount() - 1 && dx < 0){//如果在最后一个页面,并且是从右往左滑动，父控件拦截
                        //由父控件处理该事件
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        //否则都不让父类拦截
                    }
                } else {
                    //让父控件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
