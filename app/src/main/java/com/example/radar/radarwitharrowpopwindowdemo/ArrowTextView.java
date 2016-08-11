package com.example.radar.radarwitharrowpopwindowdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by cpg
 * on 2016/8/10.
 * 带箭头的TextView.
 * 支持4个方向.
 * 并可用偏移箭头点的X,Y轴坐标.
 */
public class ArrowTextView extends TextView {
    private static final String TAG = "ArrowTextView";
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int RIGHT = 3;
    public static final int BOTTOM = 4;
    /**
     * 箭头的方向
     */
    private int mArrowOrientation;
    /**
     * 箭头宽度
     */
    private float mArrowWidth;
    /**
     * 箭头高度
     */
    private float mArrowHeight;
    /**
     * 箭头位置
     */
    private float mArrowLocation;
    /**
     * 弧度
     */
    private float mContentRadius;
    /**
     * 偏移距离X
     */
    private float mArrowOffsetX;
    /**
     * 偏移距离Y
     */
    private float mArrowOffsetY;
    /**
     * 背景颜色
     */
    private int mBackgroundColor;
    /**
     * 画笔
     */
    private Paint mPaint = new Paint();
    private RectF mRectF;
    Path mPath = new Path();

    //private boolean isCreateRectF = false;

    public ArrowTextView(Context context) {
        this(context, null);
    }

    public ArrowTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs == null) {
            mArrowOrientation = LEFT;
            mArrowWidth = 10;
            mArrowHeight = 10;
            mArrowLocation = 0.5f;
            mContentRadius = 0;
            mBackgroundColor = Color.GRAY;
        }else {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArrowTextView);
            try {
                mArrowOrientation = typedArray.getInt(R.styleable.ArrowTextView_arrowOrientation, LEFT);
                mArrowWidth = typedArray.getDimension(R.styleable.ArrowTextView_arrowWidth, 10);
                mArrowHeight = typedArray.getDimension(R.styleable.ArrowTextView_arrowHeight, 10);
                mArrowLocation = typedArray.getFloat(R.styleable.ArrowTextView_arrowLocation, 0.5f);
                mContentRadius = typedArray.getDimension(R.styleable.ArrowTextView_contentRadius, 0);
                mBackgroundColor = typedArray.getColor(R.styleable.ArrowTextView_backgroundColor, Color.GRAY);
                mArrowOffsetX = typedArray.getDimension(R.styleable.ArrowTextView_arrowOffsetX, 0);
                mArrowOffsetY = typedArray.getDimension(R.styleable.ArrowTextView_arrowOffsetY, 0);
            } finally {
                typedArray.recycle();
            }

        }

        initPadding();
        initPaint();
    }

    @Override
    public void invalidate() {
        Log.d(TAG, "invalidate()... ...");
        super.invalidate();
    }

    /**
     * 通过padding来增加宽高.
     * 如果控件的长宽不固定,绘制图形时,超过控件大小绘制不出.
     */
    private void initPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        switch (mArrowOrientation) {
            case LEFT:
                paddingLeft += mArrowWidth + mArrowOffsetX;
                if(mArrowLocation <= 0) {
                    paddingTop += mArrowOffsetY;
                }else if(mArrowLocation >= 1.0f){
                    paddingBottom += mArrowOffsetY;
                }
                break;
            case TOP:
                paddingTop += mArrowHeight + mArrowOffsetY;
                if(mArrowLocation <= 0) {
                    paddingLeft += mArrowOffsetX;
                }else if(mArrowLocation >= 1.0f){
                    paddingRight += mArrowOffsetX;
                }
                break;
            case RIGHT:
                paddingRight += mArrowWidth + mArrowOffsetX;
                if(mArrowLocation <= 0) {
                    paddingTop += mArrowOffsetY;
                }else if(mArrowLocation >= 1.0f){
                    paddingBottom += mArrowOffsetY;
                }
                break;
            case BOTTOM:
                paddingBottom += mArrowHeight + mArrowOffsetY;
                if(mArrowLocation <= 0) {
                    paddingLeft += mArrowOffsetX;
                }else if(mArrowLocation >= 1.0f){
                    paddingRight += mArrowOffsetX;
                }
                break;
        }

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint.setColor(mBackgroundColor);
        mPaint.setAntiAlias(true);
    }


    /**
     * left             s3
     *                 *
     *              *  *
     *          s1*    *
     *              *  *
     *                 *
     *                  s2
     * top          s1
     *               *
     *             *   *
     *         s2* * * * * s3
     *
     * right
     *          *s2
     *          *  *
     *          *     *s1
     *          *  *
     *          *s3
     * bottom
     *        s3* * * * *s2
     *            *   *
     *              *s1
     */
    @Override
    protected void onDraw(Canvas canvas) {

        //矩形边框
        float left = 0;
        float top = 0;
        float right = getWidth();
        float bottom = getHeight();
        //偏移位置
        float x = 0;
        float y = 0;
        //横向为X轴,纵向为Y轴
        float s1;
        //与#s1相反.
        float s2;
        float s3;

        switch (mArrowOrientation) {
            case LEFT:
                //矩形边框left
                left = mArrowWidth + mArrowOffsetX;
                //偏移Y轴
                y = getHeight() * mArrowLocation;
                y = y > getHeight() - mArrowHeight / 2 ? getHeight() - mArrowHeight / 2 : y + mArrowHeight / 2;
                //三角形x轴
                s1 = x + mArrowWidth + mArrowOffsetX;

                mPath.moveTo(x, y);

                if(mArrowLocation <= 0) {
                    top += mArrowOffsetY;
                    //三角形的2个Y轴坐标.
                    s2 = y - mArrowHeight / 2 + mArrowOffsetY;
                    s3 = y + mArrowHeight / 2 + mArrowOffsetY;
                    mPath.lineTo(s1 + mArrowOffsetX, s2);
                    mPath.lineTo(s1, s3);
                }else if(mArrowLocation >= 1.0f) {
                    bottom -= mArrowOffsetY;
                    s2 = y - mArrowHeight / 2 - mArrowOffsetY;
                    s3 = y + mArrowHeight / 2 - mArrowOffsetY;
                    mPath.lineTo(s1, s2);
                    mPath.lineTo(s1 + mArrowOffsetX, s3);
                }else {
                    s2 = y - mArrowHeight / 2;
                    s3 = y + mArrowHeight / 2;
                    mPath.lineTo(s1, s2);
                    mPath.lineTo(s1, s3);
                }
                mPath.close();
                break;
            case TOP:
                top = mArrowHeight + mArrowOffsetY;
                x = getWidth() * mArrowLocation;
                x = x > getWidth() - mArrowWidth / 2 ? getWidth() - mArrowWidth / 2 : x + mArrowWidth / 2;
                s1 = y + mArrowHeight + mArrowOffsetY;

                mPath.moveTo(x, y);

                if(mArrowLocation <= 0) {
                    left += mArrowOffsetX;
                    s2 = x - mArrowHeight / 2 + mArrowOffsetX;
                    s3 = x + mArrowHeight / 2 + mArrowOffsetX;
                    mPath.lineTo(s2, s1 + mArrowOffsetY);
                    mPath.lineTo(s3, s1);
                }else if(mArrowLocation >= 1.0f) {
                    right -= mArrowOffsetY;
                    s2 = x - mArrowHeight / 2 - mArrowOffsetX;
                    s3 = x + mArrowHeight / 2 - mArrowOffsetX;
                    mPath.lineTo(s2, s1);
                    mPath.lineTo(s3, s1 + mArrowOffsetY);
                }else {
                    s2 = x - mArrowHeight / 2;
                    s3 = x + mArrowHeight / 2;
                    mPath.lineTo(s2, s1);
                    mPath.lineTo(s3, s1);
                }

                mPath.close();
                break;
            case RIGHT:
                right -= (mArrowWidth + mArrowOffsetX);
                x = getWidth();
                y = getHeight() * mArrowLocation;
                y = y > getHeight() - mArrowHeight / 2 ? getHeight() - mArrowHeight / 2 : y + mArrowHeight / 2;
                s1 = x - (mArrowWidth  + mArrowOffsetX);

                mPath.moveTo(x, y);

                if(mArrowLocation <= 0) {
                    top += mArrowOffsetY;
                    s2 = y - mArrowHeight / 2 + mArrowOffsetY;
                    s3 = y + mArrowHeight / 2 + mArrowOffsetY;
                    mPath.lineTo(s1 - mArrowOffsetX, s2);
                    mPath.lineTo(s1, s3);
                }else if(mArrowLocation >= 1.0f) {
                    bottom -= mArrowOffsetY;
                    s2 = y - mArrowHeight / 2 - mArrowOffsetY;
                    s3 = y + mArrowHeight / 2 - mArrowOffsetY;
                    mPath.lineTo(s1, s2);
                    mPath.lineTo(s1 - mArrowOffsetX, s3);
                }else {
                    s2 = y - mArrowHeight / 2;
                    s3 = y + mArrowHeight / 2;
                    mPath.lineTo(s1, s2);
                    mPath.lineTo(s1, s3);
                }

                mPath.close();
                break;
            case BOTTOM:
                bottom -= (mArrowHeight + mArrowOffsetY);
                x =  getWidth() * mArrowLocation;
                x = x > getWidth() - mArrowWidth / 2 ? getWidth() - mArrowWidth / 2 : x + mArrowWidth / 2;
                y = getHeight();
                s1 = y - (mArrowHeight + mArrowOffsetY);

                mPath.moveTo(x, y);

                if(mArrowLocation <= 0) {
                    left += mArrowOffsetX;
                    s2 = x - mArrowHeight / 2 + mArrowOffsetX;
                    s3 = x + mArrowHeight / 2 + mArrowOffsetX;
                    mPath.lineTo(s2, s1 - mArrowOffsetY);
                    mPath.lineTo(s3, s1);
                }else if(mArrowLocation >= 1.0f) {
                    right -= mArrowOffsetY;
                    s2 = x - mArrowHeight / 2 - mArrowOffsetX;
                    s3 = x + mArrowHeight / 2 - mArrowOffsetX;
                    mPath.lineTo(s2, s1);
                    mPath.lineTo(s3, s1 - mArrowOffsetY);
                }else {
                    s2 = x - mArrowWidth / 2;
                    s3 = x + mArrowWidth / 2;
                    mPath.lineTo(s2, s1);
                    mPath.lineTo(s3, s1);
                }

                mPath.close();
                break;
        }
        if(mRectF == null) {
            mRectF = new RectF(left, top, right, bottom);
            //isCreateRectF = false;
        }
        //canvas.restore();
        canvas.drawRoundRect(mRectF, mContentRadius, mContentRadius, mPaint);
        canvas.drawPath(mPath, mPaint);

        super.onDraw(canvas);
    }

    public int getArrowOrientation() {
        return mArrowOrientation;
    }

    public void setArrowOrientation(int arrowOrientation) {
        this.mArrowOrientation = arrowOrientation;
    }

    public float getArrowWidth() {
        return mArrowWidth;
    }

    public void setArrowWidth(float arrowWidth) {
        this.mArrowWidth = arrowWidth;
    }

    public float getArrowHeight() {
        return mArrowHeight;
    }

    public void setArrowHeight(float arrowHeight) {
        this.mArrowHeight = arrowHeight;
    }

    public float getArrowLocation() {
        return mArrowLocation;
    }

    public void setArrowLocation(float arrowLocation) {
        this.mArrowLocation = arrowLocation;
    }

    public float getContentRadius() {
        return mContentRadius;
    }

    public void setContentRadius(float contentRadius) {
        this.mContentRadius = contentRadius;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    public float getArrowOffsetY() {
        return mArrowOffsetY;
    }

    public void setArrowOffsetY(float arrowOffsetY) {
        this.mArrowOffsetY = arrowOffsetY;
    }

    public float getArrowOffsetX() {
        return mArrowOffsetX;
    }

    public void setArrowOffsetX(float arrowOffsetX) {
        this.mArrowOffsetX = arrowOffsetX;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            mArrowOrientation = bundle.getInt(ARROW_ORIENTATION);
            mArrowWidth = bundle.getFloat(ARROW_WIDTH);
            mArrowHeight = bundle.getFloat(ARROW_HEIGHT);
            mArrowLocation = bundle.getFloat(ARROW_LOCATION);
            mContentRadius = bundle.getFloat(CONTENT_RADIUS);
            mArrowOffsetX = bundle.getFloat(ARROW_OFFSET_X);
            mArrowOffsetY = bundle.getFloat(ARROW_OFFSET_Y);
            mBackgroundColor = bundle.getInt(BACKGROUND_COLOR);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE_SAVED_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putInt(ARROW_ORIENTATION, mArrowOrientation);
        bundle.putFloat(ARROW_WIDTH, mArrowWidth);
        bundle.putFloat(ARROW_HEIGHT, mArrowHeight);
        bundle.putFloat(ARROW_LOCATION, mArrowLocation);
        bundle.putFloat(CONTENT_RADIUS, mContentRadius);
        bundle.putFloat(ARROW_OFFSET_X, mArrowOffsetX);
        bundle.putFloat(ARROW_OFFSET_Y, mArrowOffsetY);
        bundle.putInt(BACKGROUND_COLOR, mBackgroundColor);

        bundle.putParcelable(INSTANCE_STATE_SAVED_STATE, super.onSaveInstanceState());
        return super.onSaveInstanceState();
    }

    private static final String INSTANCE_STATE_SAVED_STATE = "ArrowTextView_SavedState";
    private static final String ARROW_ORIENTATION = "ArrowOrientation";
    private static final String ARROW_WIDTH = "ArrowWidth";
    private static final String ARROW_HEIGHT = "ArrowHeight";
    private static final String ARROW_LOCATION = "ArrowLocation";
    private static final String CONTENT_RADIUS = "ContentRadius";
    private static final String ARROW_OFFSET_X = "ArrowOffsetX";
    private static final String ARROW_OFFSET_Y = "ArrowOffsetY";
    private static final String BACKGROUND_COLOR = "BackgroundColor";
}
