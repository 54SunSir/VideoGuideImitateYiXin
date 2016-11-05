package com.kevin.tech.vedioguidedemo;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kevin.tech.vedioguidedemo.adapter.MyViewPagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mIndicator;
    private ViewPager mViewPager;
    private ImageView[] mImageView;
    private int[] mImageArr = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3,R.drawable.image4};
    private MyViewPagerAdapter mAdapter;
    private Timer mTimer;
    private static final int UPDATE_VIEWPAGER = 100;
    private CustomizeVideoView mVideoView;
    private boolean isLoop = true;
    private Button mBtnRegister, mBtnLogin;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mTimer = new Timer();
        mIndicator = (LinearLayout) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mAdapter = new MyViewPagerAdapter(this, mImageArr);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(5000 * (mImageArr.length));
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();
                        isLoop = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
                        isLoop = true;
                        break;
                }
                return false;
            }
        });
        initIndicator();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPDATE_VIEWPAGER;
                if (isLoop) {//如果isLoop = true 才进行轮播
                    handler.sendMessage(message);
                }
            }
        }, 5000, 3000);//这里定义了轮播图切换的间隔时间
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                Log.i("kevin", position + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVideoView = (CustomizeVideoView) findViewById(R.id.video_view);
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.media));
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVideoView.start();
            }
        });
    }

    private void initIndicator() {
        mImageView = new ImageView[mImageArr.length];
        for (int i = 0; i < mImageArr.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.indicator_image, null);
            view.findViewById(R.id.indicator_iamge).setBackgroundResource(R.drawable.shape_origin_point_pink);
            mImageView[i] = new ImageView(this);
            if (i == 0) {
                mImageView[i].setBackgroundResource(R.drawable.shape_origin_point_pink);
            } else {
                mImageView[i].setBackgroundResource(R.drawable.shape_origin_point_white);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 0, 0, 0);
                mImageView[i].setLayoutParams(layoutParams);
            }
            mIndicator.addView(mImageView[i]);
        }
    }

    private void setIndicator(int position) {
        position %= mImageArr.length;
        for (int i = 0; i < mImageArr.length; i++) {
            mImageView[i].setBackgroundResource(R.drawable.shape_origin_point_pink);
            if (position != i) {
                mImageView[i].setBackgroundResource(R.drawable.shape_origin_point_white);
            }

        }
    }


    private boolean isLooper;
    private static final int SCROLL_WHAT = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_VIEWPAGER:
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mVideoView.pause();
                currentPosition = mVideoView.getCurrentPosition();
                Toast.makeText(MainActivity.this, "暂停播放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_register:
                mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.media));
                mVideoView.seekTo(currentPosition);
                mVideoView.start();
                Toast.makeText(MainActivity.this, "继续播放", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
