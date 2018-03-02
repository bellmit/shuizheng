// AmapLocationAidlInterface.aidl
package gd.water.oking.com.cn.wateradministration_gd;
// Declare any non-default types here with import statements
interface AmapLocationAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

     String[] getLocation();

     void refreshNotification();

}
