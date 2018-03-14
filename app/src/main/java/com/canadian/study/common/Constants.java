package com.canadian.study.common;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.canadian.study.R;

/**
 * Created by zheng on 16/9/7.
 */
public class Constants {
    public static final String CANADIA_DB = "canadia_db.realm";


    public static final String PRACTIAL_NET_JSON = "[{'name':'留学加拿大政府官方网站','url':'http://www.educanada.ca'}," +
            "{'name':'加拿大政府奖学金网站','url':'http://www.scholarships.gc.ca'}," +
            "{'name':'加拿大驻华使领馆','url':'http://www.china.gc.ca'}," +
            "{'name':'加拿大移民局','url':'http://www.cic.gc.ca'}," +
            "{'name':'加拿大签证申请中心','url':'http://www.vfsglobal.ca/canada/china/index.html'}," +
            "{'name':'加拿大教育部长联席会','url':'http://www.cmec.ca'}," +
            "{'name':'加拿大国际学历中心','url':'http://www.cicic.ca'}," +
            "{'name':'加拿大大学协会','url':'http://www.aucc.ca'}," +
            "{'name':'加拿大国际教育局','url':'http://www.cbie.ca'}," +
            "{'name':'加拿大应用技术与职业学院协会','url':'http://www.collegesinstitutes.ca'}," +
            "{'name':'加拿大公立中学国际项目协会','url':'http://www.caps-i.ca'}," +
            "{'name':'加拿大语言教育联盟','url':'http://www.languagescanada.ca'}" +
            "]";

    public static final String[] zonenames = {
            "不列颠哥伦比亚省",
            "艾伯塔省",
            "萨斯喀彻温省",
            "曼尼托巴省",
            "安大略省",
            "魁北克省",
            "新斯科舍省",
            "爱德华王子岛"
    };

    public static final int[] boothArr = {R.raw.bejing, R.raw.shanghai,  R.raw.guangzhou, R.raw.chengdu};

    public static void closeSoftKey(Activity activity) {
        if(null != activity.getCurrentFocus()){
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 2);
        }
    }


    public static final int[] tabViewNormalBackgroundIds = {R.drawable.beijing_icon_drawable_normal, R.drawable.shanghai_icon_drawable_normal,
            R.drawable.guangzhou_icon_drawable_normal, R.drawable.chengdu_icon_drawable_normal, R.drawable.xian_icon_drawable_normal};

    public static final int[] tabViewSelectedBackgroundIds = {R.drawable.beijing_icon_drawable_selected, R.drawable.shanghai_icon_drawable_selected,
            R.drawable.guangzhou_icon_drawable_selected,  R.drawable.chengdu_icon_drawable_selected, R.drawable.xian_icon_drawable_selected};

    public static final String[] citynames = {"北京", "上海", "广州", "成都", "西安"};
}
