package vagg.hua.android.androidschoolproject_2;

import android.Manifest;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class LocationService extends Service{

    public static final String KEY_LON = "LON";
    public static final String KEY_LAT = "LAT";

    private LocationManager locationManager;
    private LocationListener GPSLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            // Call content provider to save location
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_LON, location.getLongitude());
            contentValues.put(KEY_LAT, location.getLatitude());
            getContentResolver().insert(
                    Uri.parse("content://vagg.hua.android.LocationProvider/insertLonLat"),
                    contentValues);

            // Notify main Activity for current location
            Intent intent = new Intent("LanLonIntent");
            intent.putExtra("lat", location.getLatitude());
            intent.putExtra("lon", location.getLongitude());

            // To use LocalBroadcastManager add "implementation 'androidx.localbroadcastmanager:localbroadcastmanager:1.0.0' " to build.gradle
            // Chose LocalBroadcastManager for broadcasting sensitive information(Location) instead of global broadcast
            // so there is no leaking of private data
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No GPS permission", Toast.LENGTH_SHORT).show();
            return START_NOT_STICKY;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                10,
                GPSLocationListener
        );

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(GPSLocationListener);
        super.onDestroy();
    }

}
