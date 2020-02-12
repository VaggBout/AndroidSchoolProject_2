package vagg.hua.android.androidschoolproject_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ConnectionInfo extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = checkOnline(context);

        if(state == 1) {
            // If connected to the Internet start service
            Intent serviceIntent = new Intent();
            serviceIntent.setClassName(context, LocationService.class.getName());
            context.startService(serviceIntent);

            // Notify Activity for new connection mode
            Intent connectionIntent = new Intent("ConnectionIntent");
            connectionIntent.putExtra("status", 1);
            LocalBroadcastManager.getInstance(context).sendBroadcast(connectionIntent);

        } else {
            // If disconnected stop service
            Intent serviceIntent = new Intent();
            serviceIntent.setClassName(context, LocationService.class.getName());
            context.stopService(serviceIntent);

            // Notify Activity for new connection mode
            Intent connectionIntent = new Intent("ConnectionIntent");
            connectionIntent.putExtra("status", 0);
            LocalBroadcastManager.getInstance(context).sendBroadcast(connectionIntent);
        }
    }

    public int checkOnline(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check connectivity state
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return 1;
        } else {
            return 0;
        }
   }
}
