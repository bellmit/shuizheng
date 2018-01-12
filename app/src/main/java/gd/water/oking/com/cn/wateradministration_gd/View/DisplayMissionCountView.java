package gd.water.oking.com.cn.wateradministration_gd.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import gd.water.oking.com.cn.wateradministration_gd.R;

/**
 * Created by zhao on 2016/10/13.
 */

public class DisplayMissionCountView extends FrameLayout {

    private View contentView;
    private TextView title1_textView, count1_textView, subTitle1_textView, title2_textView, count2_textView, subTitle2_textView;
    private FrameLayout shadowView;

    public DisplayMissionCountView(Context context) {
        super(context);
        initView(context);
    }

    public DisplayMissionCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {

        contentView = LayoutInflater.from(context).inflate(R.layout.display_mission_count_layout, null);
        this.addView(contentView);

        title1_textView = (TextView) contentView.findViewById(R.id.title1_tv);
        count1_textView = (TextView) contentView.findViewById(R.id.count1_tv);
        subTitle1_textView = (TextView) contentView.findViewById(R.id.subtitle1_tv);
        title2_textView = (TextView) contentView.findViewById(R.id.title2_tv);
        count2_textView = (TextView) contentView.findViewById(R.id.count2_tv);
        subTitle2_textView = (TextView) contentView.findViewById(R.id.subtitle2_tv);
        shadowView = (FrameLayout) contentView.findViewById(R.id.shadow_layout);

        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Animation animation1 = new ScaleAnimation(1f, 1.05f, 1f, 1.05f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation1.setDuration(100);
                        animation1.setFillAfter(true);
                        animation1.setInterpolator(new AccelerateInterpolator());
                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                shadowView.setBackgroundResource(R.drawable.displayshadow_view_bg);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        Animation animation2 = new TranslateAnimation(0, -20, 0, 0);
                        animation2.setDuration(100);
                        animation2.setFillAfter(true);
                        animation2.setInterpolator(new AccelerateInterpolator());

                        //shadowView.startAnimation(animation1);
                        count1_textView.startAnimation(animation2);
                        count2_textView.startAnimation(animation2);

                        break;
                    case MotionEvent.ACTION_UP:
                        Animation animation3 = new ScaleAnimation(1.05f, 1f, 1.05f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation3.setDuration(100);
                        animation3.setFillAfter(true);
                        animation3.setInterpolator(new AccelerateInterpolator());
                        animation3.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                shadowView.setBackground(null);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        Animation animation4 = new TranslateAnimation(-20, 0, 0, 0);
                        animation4.setDuration(100);
                        animation4.setFillAfter(true);
                        animation4.setInterpolator(new AccelerateInterpolator());

                        //shadowView.startAnimation(animation3);
                        count1_textView.startAnimation(animation4);
                        count2_textView.startAnimation(animation4);
                        break;
                }
                return true;
            }
        });
    }

    public void setTitle1(String title) {
        title1_textView.setText(title);
    }

    public void setCount1(int count) {
        count1_textView.setText(count + "");
    }

    public void setCount1Color(int color) {
        count1_textView.setTextColor(color);
    }

    public void setSubTitle1(String title) {
        subTitle1_textView.setText(title);
    }

    public void setTitle2(String title) {
        title2_textView.setText(title);
    }

    public void setCount2(int count) {
        count2_textView.setText(count + "");
    }

    public void setCount2Color(int color) {
        count2_textView.setTextColor(color);
    }

    public void setSubTitle2(String title) {
        subTitle2_textView.setText(title);
    }

    public void showAnimation(int startOffset) {
        Animation animation = new TranslateAnimation(500, 0, 0, 0);
        animation.setDuration(500);
        animation.setRepeatCount(0);
        animation.setStartOffset(startOffset);
        this.startAnimation(animation);
    }
}
