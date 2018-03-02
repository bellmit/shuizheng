package gd.water.oking.com.cn.wateradministration_gd.bean;

import java.text.ParseException;

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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getDatetime() {

        try {
            return Contants.SDF.parse(datetime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void setDatetime(long datetime) {
        this.datetime = Contants.SDF.format(datetime);
    }

    @Override
    public String toString() {
        return "Point{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
