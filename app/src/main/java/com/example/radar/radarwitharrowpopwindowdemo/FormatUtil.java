package com.example.radar.radarwitharrowpopwindowdemo;

/**
 * Created by cpg
 * on 2016/8/10.
 * 工具类
 */
public class FormatUtil {
    /**
     *只为radar 比较圆周
     * @param insure
     * @return
     */
    public static String formateDoubleAsString(double insure){
        return (insure+"").substring(0,5);
    }
}
