

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class SingleShotLocationProvider {
    public static interface LocationCallback {
        public void onNewLocationAvailable(GPSCoordinates location);
    }

    public static void requestSingleUpdate(final Context context, final LocationCallback callback){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        boolean gps_enabled = false;
        boolean network_enabled = false;
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try
        {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {}
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {}
        if (gps_enabled){
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            locationManager.requestSingleUpdate(criteria, new LocationListener(){
                @Override
                public void onLocationChanged(Location location){
                    callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras){}

                @Override
                public void onProviderEnabled(String provider){}

                @Override
                public void onProviderDisabled(String provider){}
            }, null);
        }
        else if (network_enabled){
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            locationManager.requestSingleUpdate(criteria, new LocationListener(){
                @Override
                public void onLocationChanged(Location location){
                    callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras){}

                @Override
                public void onProviderEnabled(String provider){}

                @Override
                public void onProviderDisabled(String provider){}
            }, null);
        }
        else{
            callback.onNewLocationAvailable(new GPSCoordinates(-1, -1));
        }
    }

    public static class GPSCoordinates{
        public float longitude = -1;
        public float latitude = -1;

        public GPSCoordinates(float theLatitude, float theLongitude){
            longitude = theLongitude;
            latitude = theLatitude;
        }

        public GPSCoordinates(double theLatitude, double theLongitude){
            longitude = (float) theLongitude;
            latitude = (float) theLatitude;
        }
    }
}
