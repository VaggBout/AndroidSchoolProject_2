package vagg.hua.android.androidschoolproject_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ConnectionInfo connectionInfo = new ConnectionInfo();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Register broadcasters if permissions are granted
                registerReceivers();
            } else {
                Toast.makeText(this, "Required permissions: GPS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        } else {
            // If app has location permissions register broadcasters
            registerReceivers();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            this.unregisterReceiver(connectionInfo);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(LocationReceiver);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(connectionReceiver);
        }
        catch (final Exception exception) {
            // The receiver was not registered.
            // There is nothing to do in that case.
            // Everything is fine.
            Log.d("Unregister error", "Receivers are not registered");
        }
        super.onDestroy();
    }

    // Broadcast receiver to get live updates on location from service
    private BroadcastReceiver LocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Double lat = intent.getDoubleExtra("lat", -1);
            Double lon = intent.getDoubleExtra("lon", -1);

            TextView latTextView = findViewById(R.id.lat);
            TextView lonTextView = findViewById(R.id.lon);

            if(lon != -1 && lat != -1) {
                latTextView.setText("Latitude: " + lat.toString());
                lonTextView.setText("Longitude: " + lon.toString());
            }
        }
    };

    // Broadcast receiver to get updates on location change
    private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int status = intent.getIntExtra("status", -1);

            TextView statusTextView = findViewById(R.id.status);
            TextView latTextView = findViewById(R.id.lat);
            TextView lonTextView = findViewById(R.id.lon);

            if(status == 1) {
                statusTextView.setText("Connection status: Connected");
            } else if (status == 0) {
                statusTextView.setText("Connection status: Disconnected");
                latTextView.setText("Latitude: Not available");
                lonTextView.setText("Longitude: Not available");
            } else {
                statusTextView.setText("Connection status: Not available");
            }
        }
    };

    private void registerReceivers() {

        // If app has location permissions register broadcasters
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Context.WIFI_SERVICE);
        registerReceiver(connectionInfo, intentFilter);

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(LocationReceiver, new IntentFilter("LanLonIntent"));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(connectionReceiver, new IntentFilter("ConnectionIntent"));
    }
}
