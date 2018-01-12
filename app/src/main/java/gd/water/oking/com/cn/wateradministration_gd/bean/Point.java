package gd.water.oking.com.cn.wateradministration_gd.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhao on 2016/10/8.
 */

public class Point {

    private double longitude;
    private double latitude;
    private String datetime;

    public double getLongitude() {
        return longitude;
    }

    public Point setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Point setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public long getDatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(datetime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Point setDatetime(long datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.datetime = sdf.format(new Date(datetime));
        return this;
    }
}
