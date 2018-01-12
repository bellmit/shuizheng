package gd.water.oking.com.cn.wateradministration_gd.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhao on 2016/10/18.
 */

public class SignatureView extends View {

    private Paint paint = new Paint();
    private Path path = new Path();

    private boolean canPaint = true;

    public SignatureView(Context context) {
        super(context);
        initView(context);
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(25f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(canPaint) {
            canvas.drawPath(path, paint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                path.lineTo(eventX, eventY);
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void clear(){
        this.setDrawingCacheEnabled(false);
        path.reset();
        invalidate();
    }

    public Bitmap save(){
        if(!path.isEmpty()) {
            this.setDrawingCacheEnabled(true);
            this.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            this.setDrawingCacheBackgroundColor(Color.WHITE);
            this.buildDrawingCache();
            return this.getDrawingCache();
        }else{
            return null;
        }
    }

    public boolean isCanPaint() {
        return canPaint;
    }

    public void setCanPaint(boolean canPaint) {
        this.canPaint = canPaint;
    }
}
