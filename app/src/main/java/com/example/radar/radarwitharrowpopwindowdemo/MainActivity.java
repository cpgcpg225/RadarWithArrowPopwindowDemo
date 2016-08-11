package com.example.radar.radarwitharrowpopwindowdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by cpg
 * on 2016/8/10.
 * 蜘蛛网格图控件
 * 1、带箭头的popwindow弹框
 * 2、数据属性个数可以设置
 * 3、自动补偿圆角，让你的多边形一直是正的
 * 4、数据，文本，颜色都可以设置，如有需要各位看官自己封装实体传入也行，
 * 5、自动计算popwindow箭头位置指向（文本正中）
 * 6、文本点击区域自定义控件中可以设置
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
