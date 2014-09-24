package com.xhermes.android.util;

import java.io.InputStream;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.xhermes.android.R;

public class PowerImageView extends ImageView implements OnClickListener {  
	  
    /** 
     * ����GIF�����Ĺؼ��� 
     */  
    private Movie mMovie;  
  
    /** 
     * ��ʼ���Ű�ťͼƬ 
     */  
    private Bitmap mStartButton;  
  
    /** 
     * ��¼������ʼ��ʱ�� 
     */  
    private long mMovieStart;  
  
    /** 
     * GIFͼƬ�Ŀ�� 
     */  
    private int mImageWidth;  
  
    /** 
     * GIFͼƬ�ĸ߶� 
     */  
    private int mImageHeight;  
  
    /** 
     * ͼƬ�Ƿ����ڲ��� 
     */  
    private boolean isPlaying;  
  
    /** 
     * �Ƿ������Զ����� 
     */  
    private boolean isAutoPlay;  
  
    /** 
     * PowerImageView���캯���� 
     *  
     * @param context 
     */  
    public PowerImageView(Context context) {  
        super(context);  
    }  
  
    /** 
     * PowerImageView���캯���� 
     *  
     * @param context 
     */  
    public PowerImageView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
  
    /** 
     * PowerImageView���캯����������������б�Ҫ�ĳ�ʼ�������� 
     *  
     * @param context 
     */  
    public PowerImageView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PowerImageView);  
        int resourceId = getResourceId(a, context, attrs);  
        if (resourceId != 0) {  
            // ����Դid������0ʱ����ȥ��ȡ����Դ����  
            InputStream is = getResources().openRawResource(resourceId);  
            // ʹ��Movie��������н���  
            mMovie = Movie.decodeStream(is);  
            if (mMovie != null) {  
                // �������ֵ������null����˵������һ��GIFͼƬ�������ȡ�Ƿ��Զ����ŵ�����  
                isAutoPlay = a.getBoolean(R.styleable.PowerImageView_auto_play, false);  
                Bitmap bitmap = BitmapFactory.decodeStream(is);  
                mImageWidth = bitmap.getWidth();  
                mImageHeight = bitmap.getHeight();  
                bitmap.recycle();  
                if (!isAutoPlay) {  
                    // ���������Զ����ŵ�ʱ�򣬵õ���ʼ���Ű�ť��ͼƬ����ע�����¼�  
                    mStartButton = BitmapFactory.decodeResource(getResources(),  
                            R.drawable.fault);  
                    setOnClickListener(this);  
                }  
            }  
        }  
    }  
  
    @Override  
    public void onClick(View v) {  
        if (v.getId() == getId()) {  
            // ���û����ͼƬʱ����ʼ����GIF����  
            isPlaying = true;  
            invalidate();  
        }  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        if (mMovie == null) {  
            // mMovie����null��˵��������ͨ��ͼƬ����ֱ�ӵ��ø����onDraw()����  
            super.onDraw(canvas);  
        } else {  
            // mMovie������null��˵������GIFͼƬ  
            if (isAutoPlay) {  
                // ��������Զ����ţ��͵���playMovie()��������GIF����  
                playMovie(canvas);  
                invalidate();  
            } else {  
                // �������Զ�����ʱ���жϵ�ǰͼƬ�Ƿ����ڲ���  
                if (isPlaying) {  
                    // ���ڲ��žͼ�������playMovie()������һֱ���������Ž���Ϊֹ  
                    if (playMovie(canvas)) {  
                        isPlaying = false;  
                    }  
                    invalidate();  
                } else {  
                    // ��û��ʼ���ž�ֻ����GIFͼƬ�ĵ�һ֡��������һ����ʼ��ť  
                    mMovie.setTime(0);  
                    mMovie.draw(canvas, 0, 0);  
                    int offsetW = (mImageWidth - mStartButton.getWidth()) / 2;  
                    int offsetH = (mImageHeight - mStartButton.getHeight()) / 2;  
                    canvas.drawBitmap(mStartButton, offsetW, offsetH, null);  
                }  
            }  
        }  
    }  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        if (mMovie != null) {  
            // �����GIFͼƬ����д�趨PowerImageView�Ĵ�С  
            setMeasuredDimension(mImageWidth, mImageHeight);  
        }  
    }  
  
    /** 
     * ��ʼ����GIF������������ɷ���true��δ��ɷ���false�� 
     *  
     * @param canvas 
     * @return ������ɷ���true��δ��ɷ���false�� 
     */  
    private boolean playMovie(Canvas canvas) {  
        long now = SystemClock.uptimeMillis();  
        if (mMovieStart == 0) {  
            mMovieStart = now;  
        }  
        int duration = mMovie.duration();  
        if (duration == 0) {  
            duration = 1000;  
        }  
        int relTime = (int) ((now - mMovieStart) % duration);  
        mMovie.setTime(relTime);  
        mMovie.draw(canvas, 0, 0);  
        if ((now - mMovieStart) >= duration) {  
            mMovieStart = 0;  
            return true;  
        }  
        return false;  
    }  
  
    /** 
     * ͨ��Java���䣬��ȡ��srcָ��ͼƬ��Դ����Ӧ��id�� 
     *  
     * @param a 
     * @param context 
     * @param attrs 
     * @return ���ز����ļ���ָ��ͼƬ��Դ����Ӧ��id��û��ָ���κ�ͼƬ��Դ�ͷ���0�� 
     */  
    private int getResourceId(TypedArray a, Context context, AttributeSet attrs) {  
        try {  
            Field field = TypedArray.class.getDeclaredField("src");  
            field.setAccessible(true);  
            TypedValue typedValueObject = (TypedValue) field.get(a);  
            return typedValueObject.resourceId;  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (a != null) {  
                a.recycle();  
            }  
        }  
        return 0;  
    }  
  
}  
