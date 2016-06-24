package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yang.myclient.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 这是自定义包含下拉刷新和上拉刷新的ListView
 * Created by Administrator on 2016/6/18.
 */
public class RefreshListView extends ListView {

    private final int PULL_DOWN = 1;            // 下拉刷新状态
    private final int RELEASE_STATE = 2;            // 松开刷新
    private final int REFRESHING = 3;            // 正在刷新
    private int mCurrentState = PULL_DOWN;    // 当前的状态

    private View mFootView;
    private int mFootRefreshViewHeight;
    private LinearLayout mHeadView;
    private int mHeadRefreshViewHeight;

    private View mScrollView;
    private RelativeLayout mHeadRefreshView;
    private TextView mHeadRefreshViewStateText;
    private TextView mHeadRefreshViewTimeText;
    private ImageView mHeadRefreshViewArrowImage;
    private ProgressBar mHeadRefreshViewLoadingProgress;
    private float mDownY = -1;
    private int mListViewLocationY;
    private RotateAnimation mArrowUpAnimation;
    private RotateAnimation mArrowDownAnimation;
    private OnRefreshDataListener listener;
    private boolean mLoadingMode = false;
    private boolean mPullRefreshEnable = false;


    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeadView();
        initFoodView();
        initAnimation();
        initOnListener();
    }

    /**
     * 初始化监听事件
     */
    private void initOnListener() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //状态停止，如果listView显示最后一条 加载更多数据 的显示
                    case OnScrollListener.SCROLL_STATE_IDLE:
                        // 是否最后一条数据显示
                        if (getLastVisiblePosition() == getAdapter().getCount() - 1 && !mLoadingMode) {
                            Log.d("RefreshListViewLog", "显示加载组件");
                            //最后一条数据,显示加载更多的 组件
                            mFootView.setPadding(0, 0, 0, 0);//显示加载更多
                            setSelection(getAdapter().getCount());
                            //加载更多数据

                            mLoadingMode = true;
                            if (listener != null) {
                                listener.loadingMore();//实现该接口的组件取完成数据的加载
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 初始化尾部组件
     */
    private void initFoodView() {

        // listView 的尾部
        mFootView = View.inflate(getContext(), R.layout.list_view_foot_layout, null);
        // 测量尾部组件的高度
        mFootView.measure(0, 0);
        // listView尾部组件的高度
        mFootRefreshViewHeight = mFootView.getMeasuredHeight();

        mFootView.setPadding(0, -mFootRefreshViewHeight, 0, 0);
        // 加载ListView中
        addFooterView(mFootView);
    }

    /**
     * 初始化头组件
     */
    private void initHeadView() {

        // listView 的头部
        mHeadView = (LinearLayout) View.inflate(getContext(), R.layout.list_view_head_container, null);
        //获取头布局中的刷新布局
        mHeadRefreshView = (RelativeLayout) mHeadView.findViewById(R.id.list_view_head_refresh_layout);
        //获取刷新布局中的组件
        //刷新状态的文本
        mHeadRefreshViewStateText =
                (TextView) mHeadRefreshView.findViewById(R.id.tv_list_view_head_state_dec);
        //刷新时间的文本
        mHeadRefreshViewTimeText =
                (TextView) mHeadRefreshView.findViewById(R.id.tv_list_view_head_refresh_time);
        //刷新箭头
        mHeadRefreshViewArrowImage =
                (ImageView) mHeadRefreshView.findViewById(R.id.iv_list_view_head_arrow);
        //刷新加载的进度条
        mHeadRefreshViewLoadingProgress =
                (ProgressBar) mHeadRefreshView.findViewById(R.id.pb_list_view_head_loading);

        // 测量部组件的高度
        mHeadRefreshView.measure(0, 0);

        // listView头部刷新组件的高度
        mHeadRefreshViewHeight = mHeadRefreshView.getMeasuredHeight();
        mHeadRefreshView.setPadding(0, -mHeadRefreshViewHeight, 0, 0);

        addHeaderView(mHeadView);

    }

    /**
     * 设置是否启动下拉刷新功能
     *
     * @param mIsUseDownRefresh
     */
    public void setIsUseDownRefresh(boolean mIsUseDownRefresh) {
        this.mPullRefreshEnable = mIsUseDownRefresh;
    }

    /**
     * 添加滚动图到头布局的方法
     *
     * @param view
     */
    @Override
    public void addHeaderView(View view) {
        if (mPullRefreshEnable) {
            mScrollView = view;
            mHeadView.addView(mScrollView);
        } else {
            super.addHeaderView(view);
        }
    }

    /**
     * 判断滚动图是否全部显示
     *
     * @return
     */
    private boolean isScrollViewPagerFullScreen() {
        int[] viewLocation = new int[2];
        if (mListViewLocationY == 0) {
            //获取ListView在屏幕中的位置X和Y
            this.getLocationOnScreen(viewLocation);
            mListViewLocationY = viewLocation[1];
        }
        mScrollView.getLocationOnScreen(viewLocation);
        int mScrollViewLocationY = viewLocation[1];
        if (mScrollViewLocationY < mListViewLocationY) {
            return false;
        }
        return true;

    }

    /**
     * 重写ListView的onTouchEvent的方法,响应自定义事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();// 按下时y轴坐标
                break;
            case MotionEvent.ACTION_MOVE:

                //判断是否启用下拉刷新
                if (!mPullRefreshEnable) {
                    break;
                }

                //判断如果是正在刷新的状态
                if (mCurrentState == REFRESHING) {
                    break;
                }

                if (!this.isScrollViewPagerFullScreen()) {
                    break;
                }
                if (mDownY == -1) { // 按下的时候没有获取坐标
                    mDownY = ev.getY();
                }
                float moveY = ev.getY();
                float dy = moveY - mDownY;
                if (dy > 0 && this.getFirstVisiblePosition() == 0) {
                    // 当前padding top 的参数值
                    float scrollYDis = -mHeadRefreshViewHeight + dy;

                    if (scrollYDis < 0 && mCurrentState != PULL_DOWN) {
                        // 刷新头没有完全显示
                        // 下拉刷新的状态
                        mCurrentState = PULL_DOWN;// 目的只执行一次
                        refreshState();
                    } else if (scrollYDis >= 0 && mCurrentState != RELEASE_STATE) {
                        mCurrentState = RELEASE_STATE;// 记录松开刷新，只进了一次
                        refreshState();
                    }
                    mHeadRefreshView.setPadding(0, (int) scrollYDis, 0, 0);
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                mDownY = -1;
                //判断状态
                //如果是PULL_DOWN状态,松开恢复原状
                if (mCurrentState == PULL_DOWN) {
                    mHeadRefreshView.setPadding(0, -mHeadRefreshViewHeight, 0,
                            0);
                } else if (mCurrentState == RELEASE_STATE) {
                    //刷新数据
                    mHeadRefreshView.setPadding(0, 0, 0, 0);
                    mCurrentState = REFRESHING;//改变状态为正在刷新数据的状态
                    refreshState();//刷新界面
                    //真的刷新数据
                    if (listener != null) {
                        listener.refreshData();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新界面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case PULL_DOWN:// 下拉刷新
//                System.out.println("下拉刷新");
                //改变文件
                mHeadRefreshViewStateText.setText("下拉刷新");
                mHeadRefreshViewArrowImage.startAnimation(mArrowDownAnimation);
                break;
            case RELEASE_STATE:// 松开刷新
//                System.out.println("松开刷新");
                mHeadRefreshViewStateText.setText("松开刷新");
                mHeadRefreshViewArrowImage.startAnimation(mArrowUpAnimation);
                break;
            case REFRESHING://正在刷新状态
                mHeadRefreshViewArrowImage.clearAnimation();//清除所有动画
                mHeadRefreshViewArrowImage.setVisibility(View.GONE);//隐藏箭头
                mHeadRefreshViewLoadingProgress.setVisibility(View.VISIBLE);//显示进度条
                mHeadRefreshViewStateText.setText("正在刷新数据");
                break;
            default:
                break;
        }
    }

    /**
     * 刷新数据成功,处理结果
     */
    public void refreshStateFinish() {
        if (mLoadingMode) {
            Log.d("RefreshListViewLog", "隐藏加载栏");
            //隐藏加载更多数据的组件
            mFootView.setPadding(0, -mFootRefreshViewHeight, 0, 0);
            //加载更多数据
            mLoadingMode = false;
        }
        if (mCurrentState == REFRESHING) {
            //改变文件
            Log.d("RefreshListViewLog", "隐藏下拉刷新栏");
            mHeadRefreshViewStateText.setText("下拉刷新");
            mHeadRefreshViewArrowImage.setVisibility(View.VISIBLE);//显示箭头
            mHeadRefreshViewLoadingProgress.setVisibility(View.INVISIBLE);//隐藏进度条
            //设置刷新时间为当前时间
            mHeadRefreshViewTimeText.setText(getCurrentFormatDate());
            //隐藏刷新的头布局
            mHeadRefreshView.setPadding(0, -mHeadRefreshViewHeight, 0, 0);
            mCurrentState = PULL_DOWN;
        }

    }

    private String getCurrentFormatDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 定义动画事件
     */
    private void initAnimation() {
        mArrowUpAnimation = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mArrowUpAnimation.setDuration(500);
        mArrowUpAnimation.setFillAfter(true);//停留在动画结束的状态

        mArrowDownAnimation = new RotateAnimation(-180, -360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mArrowDownAnimation.setDuration(500);
        mArrowDownAnimation.setFillAfter(true);//停留在动画结束的状态

    }

    public void setOnRefreshDataListener(OnRefreshDataListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshDataListener {
        void refreshData();

        void loadingMore();
    }
}

