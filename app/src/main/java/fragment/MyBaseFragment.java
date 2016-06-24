package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**这是自定义Fragment的基类
 * Created by Administrator on 2016/6/12.
 */
public abstract class MyBaseFragment extends Fragment{

    public Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStater){
        View view=initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化Fragment数据
     */
    protected abstract void initData();

    /**
     * 初始化Fragment布局
     * @return
     */
    protected abstract View initView();
}
