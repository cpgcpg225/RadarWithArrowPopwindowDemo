package com.example.radar.radarwitharrowpopwindowdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class MainActivity extends AppCompatActivity{

    private RadarView mRrdarView;
    private ArrayList<RadarPointTextBean> radarList=new ArrayList<RadarPointTextBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mRrdarView= (RadarView) findViewById(R.id.mRadarView);
    }
    private void initData() {
        radarList.add(new RadarPointTextBean("剑客","剑客初学难度不大，职业机动性非常高，近可攻退可守。加上攻击速度与技能连击能力也不弱，使得剑客职业压制力较强。PVE与PVP能力都属中等。",100));
        radarList.add(new RadarPointTextBean("枪客","枪客各方面指数都比较均衡，想要精通却并非易事。技能衔接与攻击速度都不弱，压制力也比较强，PVP较强。",30));
        radarList.add(new RadarPointTextBean("力士","力士拥有不俗的控制能力，近身压制力超强。攻击速度与机动性稍薄弱，但技能衔接与打击力较强。但PVE方面稍显不足，PVP能力稍强。",60));
        radarList.add(new RadarPointTextBean("刺客","刺客操作难，依赖超强的攻击速度与技能连招使得职业压制力较强。刺客在PVE和PVP的表现力都不弱，入手容易精通难",95));
        radarList.add(new RadarPointTextBean("散仙","属可近战也可远程的职业，拥有不弱的攻击力，但攻击速度稍慢。技能连贯性与机动性较强，压制力也不错。PVE较强，PVP也不弱。",70));
       // radarList.add(new RadarPointTextBean("刀客","刀客前期易上手，后期连击操作稍难。攻击速度与机动性还不错，压制力略低一筹。在PVE与PVP能力方面，均属中上。",70));
        //radarList.add(new RadarPointTextBean("弓手","弓手需要超高的判定和操作，虽然攻击速度和机动性很强，但在压制力方面却十分薄弱，需要拉开对手距离方可克敌制胜。PVE能力超强，PVP能力略低。",70));
        mRrdarView.setData(radarList);
    }
}
