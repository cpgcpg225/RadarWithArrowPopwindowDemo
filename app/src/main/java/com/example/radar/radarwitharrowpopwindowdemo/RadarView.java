package com.example.radar.radarwitharrowpopwindowdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

/**
 * Created by cpg
 * on 2016/8/9.
 * 带有箭头的popwindow的雷达蜘蛛图
 */
public class RadarView extends View implements View.OnTouchListener {
    /**数据个数*/
    private int count =0;
    /**数据集合*/
    private ArrayList<RadarPointTextBean> radarList;
    /**增加的触摸区域*/
    private final float addLength=30f;

    /**平分角度*/
    private float angle= (float) (Math.PI*2/count);
    /**网格最大半径 */
    private float radius;
    /**中心X */
    private int centerX;
    /**中心Y*/
    private int centerY;
    private float maxValue = 100;             //数据最大值
    private Paint mainPaint;                //雷达区画笔
    private Paint valuePaint;               //数据区画笔
    private Paint textPaint;                //文本画笔
    private float addAngle=0;//补偿角度
    public RadarView(Context context) {
        super(context);
        initview();
    }
    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview();
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview();
    }

    /**
     * 初始化布局
     */
    private void initview() {
        if(radarList==null||radarList.size()==0)
            initDefaultData();
        mainPaint=new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(Color.GRAY);
        mainPaint.setStyle(Paint.Style.STROKE);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(Color.BLUE);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new Paint();
        textPaint.setTextSize(20);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);

        this.setOnTouchListener(this);
    }

    /**
     * 默认数据
     * @return
     */
    private void initDefaultData() {
        radarList=new ArrayList<RadarPointTextBean>();
        radarList.add(new RadarPointTextBean("尽调能力","是指你实地尽调的能力与质量，借款人对你的评价、尽调标是否按时还款等都将影响你的得分",100));
        radarList.add(new RadarPointTextBean("催收能力","我们将综合考察你对逾期标的催收态度及成果",30));
        radarList.add(new RadarPointTextBean("履约能力","你对逾期标是否及时垫付将影响你的履约能力",60));
        radarList.add(new RadarPointTextBean("活跃度","是指你在担保、抢单尽调的次数及金额",95));
        radarList.add(new RadarPointTextBean("风险承受能力","我们将综合考虑你在平台的资产信息及担保情况来判断你的风险承受能力",70));
        count=radarList.size();
        angle=(float) (Math.PI*2/count);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius=Math.min(h,w)/2*0.85f;
        centerX=w/2;
        centerY=h/2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(count%2!=0)
            addAngle= (float) (angle-Math.PI/2);
        drawPloygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    /**
     * 绘制直线
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for(int i=0;i<count;i++){
            path.reset();
            path.moveTo(centerX, centerY);
            float x = (float) (centerX+radius*Math.cos(angle*i+addAngle));
            float y = (float) (centerY+radius*Math.sin(angle*i+addAngle));
            path.lineTo(x, y);
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制正多边形
     * @param canvas
     */
    private void drawPloygon(Canvas canvas) {
        Path path=new Path();
        float r=radius/(count-1);//这是可以设置多少等分的半径
        for (int i=1;i<count;i++){
            float curR=r*i;
            path.reset();
            for (int j=0;j<count;j++){//画一个多边形
                if(j==0){
                    float x =(float) (centerX+curR*Math.cos(addAngle));
                    float y = (float) (centerY+curR*Math.sin(addAngle));
                    path.moveTo(x,y);
                }else {
                    float x = (float) (centerX+curR*Math.cos(angle*j+addAngle));
                    float y = (float) (centerY+curR*Math.sin(angle*j+addAngle));
                    path.lineTo(x,y);
                }
            }
            path.close();
            canvas.drawPath(path,mainPaint);
        }
    }

    /**
     * 绘制文字
     * @param canvas
     */
    private void drawText(Canvas canvas){
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;//文本高度
        for(int i=0;i<count;i++){
            float curAngle=angle*i+addAngle;
            float dis = textPaint.measureText(radarList.get(i).title);//文本长度
            float x = (float) (centerX+(radius+fontHeight/2)*Math.cos(curAngle));
            float y = (float) (centerY+(radius+fontHeight/2)*Math.sin(curAngle));
            radarList.get(i).startY=y-addLength;
            radarList.get(i).endY=y+fontHeight+addLength;

            if(curAngle<0)
                curAngle+=Math.PI*2;
            if((curAngle+"").contains(FormatUtil.formateDoubleAsString(3*Math.PI/2))) {//中上
                canvas.drawText(radarList.get(i).title, x-dis/2,y,textPaint);
                radarList.get(i).startX=x-addLength-dis/2;
                radarList.get(i).endX=x+dis/2+addLength;
            }else if(curAngle>=0&&curAngle<Math.PI/2) {//第一象限
                canvas.drawText(radarList.get(i).title, x,y,textPaint);
                radarList.get(i).startX=x-addLength;
                radarList.get(i).endX=x+dis+addLength;
            }else if(curAngle>Math.PI/2&&curAngle<=Math.PI){//第二象限
                canvas.drawText(radarList.get(i).title, x-dis,y,textPaint);
                radarList.get(i).startX=x-dis-addLength;
                radarList.get(i).endX=x+addLength;
            }else if(curAngle>=Math.PI&&curAngle<3*Math.PI/2){//第三象限
                canvas.drawText(radarList.get(i).title, x-dis,y,textPaint);
                radarList.get(i).startX=x-dis-addLength;
                radarList.get(i).endX=x+addLength;
            }else if(curAngle>=3*Math.PI/2&&curAngle<=Math.PI*2){//第四象限
                canvas.drawText(radarList.get(i).title, x,y,textPaint);
                radarList.get(i).startX=x-addLength;
                radarList.get(i).endX=x+dis+addLength;
            }
        }
    }

    /**
     * 绘制区域
     * @param canvas
     */
    private void drawRegion(Canvas canvas){
        Path path = new Path();
        valuePaint.setAlpha(255);
        for(int i=0;i<count;i++){
            double percent = radarList.get(i).score/maxValue;
            float x = (float) (centerX+radius*Math.cos(angle*i+addAngle)*percent);
            float y = (float) (centerY+radius*Math.sin(angle*i+addAngle)*percent);
            if(i==0){
                path.moveTo(x, y);
            }else{
                path.lineTo(x,y);
            }
            //绘制小圆点
            canvas.drawCircle(x,y,10,valuePaint);
        }
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);
        valuePaint.setAlpha(127);
        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }
    //设置数据
    public void setData(ArrayList<RadarPointTextBean> radarList) {
        this.radarList=radarList;
        count=radarList.size();
        angle=(float) (Math.PI*2/count);
        //清除画布
        Paint mPaint = new Paint();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Canvas canvas=new Canvas();
        canvas.drawPaint(mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        onDraw(canvas);
    }

    //设置蜘蛛网颜色
    public void setMainPaintColor(int color){
        mainPaint.setColor(color);
    }

    //设置标题颜色
    public void setTextPaintColor(int color){
        textPaint.setColor(color);
    }

    //设置覆盖局域颜色
    public void setValuePaintColor(int color){
        valuePaint.setColor(color);
    }

    //设置多边形的边数
    public void setCount(int count){
        this.count=count;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX=event.getX();
        float touchY=event.getY();
        for(int i=0;i<count;i++){
            if(touchX>=radarList.get(i).startX&&touchX<=radarList.get(i).endX&&touchY>=radarList.get(i).startY&&touchY<=radarList.get(i).endY){
                showPopWindow(radarList.get(i));
            }
        }
        return false;
    }

    /**
     * popwindow弹框
     * @param radar 实体
     */
    private void showPopWindow(RadarPointTextBean radar) {
        final View contentView =LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.popup_window_with_arrow, null);
        PopupWindow popupWindow=new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        ArrowTextView mArrowTextView= (ArrowTextView) contentView.findViewById(R.id.mArrowTextView);
        mArrowTextView.setText(radar.titleDesc);

        contentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        final int contentViewWidth = contentView.getMeasuredWidth();
        final int contentViewHeight = contentView.getMeasuredHeight();
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        float rate=calculateArrowLocation((radar.startX+radar.endX)/2,(float)contentViewWidth);
        mArrowTextView.setArrowLocation(rate-0.0333f);//补偿箭头宽度
        final int[] location = new int[2];
        this.getLocationOnScreen(location);
        final int x = location[0]+(int) (radar.startX+radar.endX)/2-contentViewWidth/2;
        final int y = location[1]-contentViewHeight+(int) radar.startY;
        popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, x, y);
    }

    /**
     * 计算箭头比例
     * @param centerX 中心x
     * @param popWidth popwindow 宽度
     * @return 比例
     */
    private  float calculateArrowLocation(float centerX,float popWidth){
        WindowManager wm= (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        int width=wm.getDefaultDisplay().getWidth();
        if(centerX<=(popWidth/2)){
            return centerX/popWidth;
        }else if(centerX>=(width-popWidth/2)){
            return 1-(width-centerX)/popWidth;
        }else {
            return 0.5f;
        }
    }
}
