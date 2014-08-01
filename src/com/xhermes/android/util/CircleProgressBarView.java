package com.xhermes.android.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
  
public class CircleProgressBarView extends View {  
    private int progress;  
    private int max;  
    private Paint paint;  
    private RectF oval;  
    public int getMax() {  
        return max;  
    }  
    public void setMax(int max) {  
        this.max = max;  
    }  
    public int getProgress() {  
        return progress;  
    }  
    public void setProgress(int progress) {  
        this.progress = progress;  
        invalidate();  
    }  
  
    public CircleProgressBarView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        paint = new Paint();  
        oval = new RectF();  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        paint.setAntiAlias(true);// 设置是否抗锯齿  
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿  
        paint.setColor(Color.GRAY);// 设置画笔灰色  
        paint.setStrokeWidth(10);// 设置画笔宽度  
        paint.setStyle(Paint.Style.STROKE);// 设置中空的样式  
        canvas.drawCircle(100, 100, 55, paint);// 在中心为（100,100）的地方画个半径为55的圆，宽度为setStrokeWidth：10，也就是灰色的底边  
        paint.setColor(Color.GREEN);// 设置画笔为绿色  
        oval.set(45, 45, 155, 155);// 设置类似于左上角坐标（45,45），右下角坐标（155,155），这样也就保证了半径为55  
        canvas.drawArc(oval, -90, ((float) progress / max) * 360, false, paint);// 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心  
        paint.reset();// 将画笔重置  
        paint.setStrokeWidth(3);// 再次设置画笔的宽度  
        paint.setTextSize(35);// 设置文字的大小  
        paint.setColor(Color.BLACK);// 设置画笔颜色  
        if (progress == max) {  
            canvas.drawText("完成", 70, 110, paint);  
        } else {  
            canvas.drawText(progress + "%", 70, 110, paint);  
        }  
    }  
}  