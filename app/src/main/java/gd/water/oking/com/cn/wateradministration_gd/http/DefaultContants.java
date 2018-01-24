package gd.water.oking.com.cn.wateradministration_gd.http;

import android.util.Log;
import android.webkit.CookieManager;

import gd.water.oking.com.cn.wateradministration_gd.bean.User;

/**
 * Created by pc on 16/10/19.
 */

public class DefaultContants {


    public static boolean HASGOTTOKEN = false;
    //喆
//    public static final String SERVER_HOST = "http://192.168.0.113:8087/gdWater";
//    public static final String SERVER = "http://192.168.0.113:8087";
//    public static String DOMAIN = "http://192.168.0.113:8087";

    //刘
//    public static final String SERVER_HOST = "http://192.168.0.106:8080/gdWater";
//    public static final String SERVER = "http://192.168.0.106:8080";
//    public static String DOMAIN = "192.168.0.106:8080";

        //测试服务器
    public static final String SERVER_HOST = "http://10.44.21.26:8087/gdWater";
    public static final String SERVER = "http://10.44.21.26:8087";
    public static String DOMAIN = "10.44.21.26:8087";

        //正式服务器
//    public static final String SERVER_HOST = "http://10.44.21.26:80/gdWater";
//    public static final String SERVER = "http://10.44.21.26:80";
//    public static String DOMAIN = "10.44.21.26:80";


    public static String PATH = "/gdWater";
    public static String JSESSIONID = "";
    public static User CURRENTUSER = null;
    public static String ACCESS_TOKEN = "";
    public static boolean ISHTTPLOGIN = false;

    public static void syncCookie(String url) {
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookies(null);
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                Log.d("Nat:oldCookie", oldCookie);
            }

            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", DefaultContants.JSESSIONID));
            sbCookie.append(String.format(";domain=%s", DefaultContants.DOMAIN));
            sbCookie.append(String.format(";path=%s", DefaultContants.PATH));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            cookieManager.flush();
//            String newCookie = cookieManager.getCookie(url);


        } catch (Exception e) {
        }
    }
}
