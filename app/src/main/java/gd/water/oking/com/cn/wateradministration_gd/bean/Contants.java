package gd.water.oking.com.cn.wateradministration_gd.bean;

import com.amap.api.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class Contants {
    public static final int PICTYPE = 1;            //图片
    public static final int RECORDSCREENTYPE = 2;   //视频
    public static final int STARTADNEDD = 0;     //起点终点


    public static long ENDTIME = 0;

    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String[] LOCATIONRESULT = {"","","","","","","","",""};

    public static List<String> MARQUEEVIEWINFO = new ArrayList<>();

    static {
        MARQUEEVIEWINFO.add("");
        MARQUEEVIEWINFO.add("");
        MARQUEEVIEWINFO.add("");

    }

    public static final LatLng PAD_1 = new LatLng(22.545173, 113.360352);
    public static final LatLng PAD_2 = new LatLng(22.5225, 113.384385);
    public static final LatLng PAD_3 = new LatLng(22.481822, 113.403293);
    public static final LatLng PAD_4 = new LatLng(22.45648, 113.411084);
    public static final LatLng PAD_5 = new LatLng(23.146249, 113.333418);
    public static final LatLng PAD_6 = new LatLng(23.145603, 113.334106);
    public static final LatLng PAD_7 = new LatLng(23.145509, 113.333462);
    public static final LatLng PAD_8 = new LatLng(23.146984, 113.332738);
    public static final LatLng PAD_9 = new LatLng(23.146436, 113.334202);


    public static final LatLng MOVECENTER = new LatLng(23.216240864201108, 112.80308326186321);
    public static final LatLng STARTLATLNG = new LatLng(23.234459161444516, 112.8114729970563);
    public static final LatLng ENDLATLNG = new LatLng(23.216927695817933, 112.80293250504201);
}
