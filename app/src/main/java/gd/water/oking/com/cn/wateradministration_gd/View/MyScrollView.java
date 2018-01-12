package gd.water.oking.com.cn.wateradministration_gd.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by zhao on 2017-4-6.
 */

public class MyScrollView extends ScrollView {
    private float mEndRawY;
    private float mStarRawY;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                requestDisallowInterceptTouchEvent(true);
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }




}
