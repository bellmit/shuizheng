package gd.water.oking.com.cn.wateradministration_gd.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public BootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent service = new Intent(context, LocationService.class);
//        context.startService(service);
        Toast.makeText(context, "is running backgrand ", Toast.LENGTH_SHORT).show();
    }
}
