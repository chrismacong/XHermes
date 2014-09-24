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
        paint.setAntiAlias(true);// �����Ƿ񿹾��  
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);// �����������  
        paint.setColor(Color.GRAY);// ���û��ʻ�ɫ  
        paint.setStrokeWidth(10);// ���û��ʿ��  
        paint.setStyle(Paint.Style.STROKE);// �����пյ���ʽ  
        canvas.drawCircle(100, 100, 55, paint);// ������Ϊ��100,100���ĵط������뾶Ϊ55��Բ�����ΪsetStrokeWidth��10��Ҳ���ǻ�ɫ�ĵױ�  
        paint.setColor(Color.GREEN);// ���û���Ϊ��ɫ  
        oval.set(45, 45, 155, 155);// �������������Ͻ����꣨45,45�������½����꣨155,155��������Ҳ�ͱ�֤�˰뾶Ϊ55  
        canvas.drawArc(oval, -90, ((float) progress / max) * 360, false, paint);// ��Բ�����ڶ�������Ϊ����ʼ�Ƕȣ�������Ϊ��ĽǶȣ����ĸ�Ϊtrue��ʱ����ʵ�ģ�false��ʱ��Ϊ����  
        paint.reset();// ����������  
        paint.setStrokeWidth(3);// �ٴ����û��ʵĿ��  
        paint.setTextSize(35);// �������ֵĴ�С  
        paint.setColor(Color.BLACK);// ���û�����ɫ  
        if (progress == max) {  
            canvas.drawText("���", 70, 110, paint);  
        } else {  
            canvas.drawText(progress + "%", 70, 110, paint);  
        }  
    }  
}  