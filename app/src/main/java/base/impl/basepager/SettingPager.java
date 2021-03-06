package base.impl.basepager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import base.BasePager;

/**这是设置中心页
 * Created by Administrator on 2016/6/12.
 */
public class SettingPager extends BasePager{


    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData(){
        // 要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("设置中心");
        view.setTextColor(Color.RED);
        view.setTextSize(25);
        view.setGravity(Gravity.CENTER);

        mFragmentContent.addView(view);

        mTitleBarTextView.setText("设置中心");
        mMenuImageButton.setVisibility(View.GONE);
    }
}
