package activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.test.yang.myclient.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tools.ShareTool;

/**
 * 这是新闻详情阅读页面
 * Created by Administrator on 2016/6/19.
 */
public class NewsReadingActivity extends AppCompatActivity {

    @InjectView(R.id.ib_news_content_back)
    ImageButton mNewsContentBackButton;
    @InjectView(R.id.ib_news_content_share)
    ImageButton mNewsContentShareButton;
    @InjectView(R.id.ib_news_content_text_size)
    ImageButton mNewsContentTextSizeButton;
    @InjectView(R.id.wv_news_details_read)
    WebView mNewsDetailsReadWebView;
    @InjectView(R.id.pb_news_details_loading)
    ProgressBar mNewsDetailsLoading;
    private WebSettings mWebViewSetting;
    private Timer mTimer;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("NewsReadingActivity", "handleMessage");
            mNewsDetailsReadWebView.stopLoading();
//            Toast.makeText(NewsReadingActivity.this, "网络数据加载失败", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.inject(this);
        //初始化数据
        initData();
        //初始化界面
        initView();
        initEvent();


    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        //创建三个按钮公用的监听器
        View.OnClickListener listener = new View.OnClickListener() {
            int textSizeIndex = 2;// 0. 超大号 1,大号  2 正常  3 小号  4 超小号
            private AlertDialog dialog;

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ib_news_content_back://返回键
                        //关闭当前新闻页面
                        finish();
                        break;
                    case R.id.ib_news_content_text_size://修改字体大小
                        //通过对话框来修改字体大小 五种字体大小
                        showChangeTextSizeDialog();
                        //设置字体大小 wv_setting.setTextSize(TextSize.)
                        break;
                    case R.id.ib_news_content_share://分享
//                        Log.d("NewsReadingActivity","执行分享操作");
                        ShareTool.showShare(getApplicationContext());
                        break;
                    default:
                        break;
                }
            }

            private void showChangeTextSizeDialog() {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewsReadingActivity.this);
                dialogBuilder.setTitle("改变字体大小");
                String[] textSize = new String[]{"超大号", "大号", "正常", "小号", "超小号"};
                dialogBuilder.setSingleChoiceItems(textSize, textSizeIndex, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textSizeIndex = which;
                        setTextSize();

                    }
                });
                dialog = dialogBuilder.create();
                dialog.show();
            }

            private void setTextSize() {
                switch (textSizeIndex) {
                    case 0://超大号
                        mWebViewSetting.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1: //大号
                        mWebViewSetting.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2: //正常
                        mWebViewSetting.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3: //小号
                        mWebViewSetting.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4: //最小号
                        mWebViewSetting.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        };
        //给返回键添加事件
        mNewsContentBackButton.setOnClickListener(listener);
        mNewsContentTextSizeButton.setOnClickListener(listener);
        mNewsContentShareButton.setOnClickListener(listener);

        //给WebView添加一个新闻加载完成的监听事件
        mNewsDetailsReadWebView.setWebViewClient(new WebViewClient() {

            /* (non-Javadoc)
             * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
             * 页面加载完成的事件处理
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                mTimer.purge();
                mTimer.cancel();
                //隐藏进度条
                mNewsDetailsLoading.setVisibility(View.GONE);
                Toast.makeText(NewsReadingActivity.this, "网络数据加载完成", Toast.LENGTH_SHORT).show();
                Log.d("NewsReadingActivity", "onPageFinished");
                super.onPageFinished(view, url);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("NewsReadingActivity", "shouldOverrideUrlLoading");
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                Log.d("NewsReadingActivity", "onPageCommitVisible");
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("NewsReadingActivity", "onPageStarted");
                super.onPageStarted(view, url, favicon);
                int timeDelayOut = 5000;
                mTimer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {

                        mHandler.sendEmptyMessage(0);
                        mTimer.purge();
                        mTimer.cancel();

                    }
                };
                mTimer.schedule(timerTask,timeDelayOut);

            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                            WebResourceResponse errorResponse) {
                Log.d("NewsReadingActivity", "onReceivedHttpError");
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                mTimer.purge();
                mTimer.cancel();
                Log.d("NewsReadingActivity", "onReceivedError");
                mNewsDetailsLoading.setVisibility(View.GONE);
                Toast.makeText(NewsReadingActivity.this, "网络数据加载失败", Toast.LENGTH_SHORT).show();
                //隐藏进度条
                super.onReceivedError(view, request, error);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //获取数据
        String url = getIntent().getStringExtra("news_reading_url");
        Log.d("NewsReadingActivity","请求新闻详情的网址是："+url);
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(getApplicationContext(), "网络链接错误", Toast.LENGTH_SHORT).show();
        } else {
            //有新闻
            //加载新闻
            mNewsDetailsReadWebView.loadUrl(url);
        }

    }

    /**
     * 初始化界面
     */
    private void initView() {
        //控制WebView的显示设置
        mWebViewSetting = mNewsDetailsReadWebView.getSettings();
        //设置放大缩小
//        mWebViewSetting.setBuiltInZoomControls(true);
        mWebViewSetting.setJavaScriptEnabled(true);//可以去编译javascript脚本
        //设置双击放大或缩小
        mWebViewSetting.setUseWideViewPort(true);//双击放大或缩小
        //设置WebView不使用缓存模式
        mWebViewSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    protected void onDestroy() {
        //清理缓存
        mNewsDetailsReadWebView.clearCache(true);
        //关闭WebView
        mNewsDetailsReadWebView.clearView();
        mNewsDetailsReadWebView.removeAllViews();
        super.onDestroy();
    }
}
