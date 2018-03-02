package gd.water.oking.com.cn.wateradministration_gd.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/2/2.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("网络改变");
        context.sendBroadcast(new Intent("oking.network"));
    }
}
