package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.yang.myclient.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends AppCompatActivity {

    @InjectView(R.id.splash_text_view)
    TextView splashTextView;
    @InjectView(R.id.splash_progress_bar)
    ProgressBar splashProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        setAnimation();
        init();
    }

    /**
     * 设置闪屏动画
     */
    private void setAnimation(){
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        RotateAnimation rotateAnimation=new RotateAnimation(0,1080, Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
        ScaleAnimation scaleAnimation=new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,0.5f,0.5f);
        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(2000);
        splashTextView.setAnimation(animationSet);
        animationSet.start();
    }

    /**
     * 初始化软件操作
     */
    private void init(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        splashProgressBar.setVisibility(View.VISIBLE);
                    }
                });
                SystemClock.sleep(1000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        splashProgressBar.setVisibility(View.GONE);
                        startMain();
                    }
                });
            }
        }).start();
    }

    private void startMain(){
        Intent jumpGuideActivity=new Intent(SplashActivity.this,GuideActivity.class);
        startActivity(jumpGuideActivity);
        finish();
    }
}
