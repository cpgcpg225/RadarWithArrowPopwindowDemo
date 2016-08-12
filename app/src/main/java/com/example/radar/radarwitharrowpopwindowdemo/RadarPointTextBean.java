package com.example.radar.radarwitharrowpopwindowdemo;

/**
 * Created by cpg
 * on 2016/8/10.
 * 蜘蛛图里面的文本
 */
public class RadarPointTextBean {
    /**
     * 标题说明
     */
    public String titleDesc;
    /**
     * 开始的x坐标
     */
    public float startX;
    /**
     *结束的x坐标
     */
    public float endX;
    /**
     * 开始的y坐标
     */
    public float startY;
    /**
     * 结束的y坐标
     */
    public float endY;
    /**
     * 标题
     */
    public String title;
    /**
     * 得分
     */
    public int score;
    public RadarPointTextBean(String title, String titleDesc, int score) {
        this.titleDesc = titleDesc;
        this.title = title;
        this.score = score;
    }
}
