package com.app.fap.librairies;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.app.fap.R;
import com.app.fap.maputils.PanelClusterOptionsProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

/****************************************************
 * Created by Tahiana-MadiApps on 03/08/2016.
 ****************************************************/
public class MapTools {
    public static Location locationByAddressLine(String addressString, Activity activity)
    {
        Location locationAddress = null;
        try
        {
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocationName(addressString, 1);

            if (addresses.size() > 0)
            {
                locationAddress = new Location("locationAddress");
                locationAddress.setLatitude(addresses.get(0).getLatitude());
                locationAddress.setLongitude(addresses.get(0).getLongitude());
            }
        }
        catch (Exception ex)
        {
            Log.e("EXCEPTION GEOCODER", ex.getMessage());
        }
        finally
        {
            return locationAddress;
        }
    }

    public static Address addressFromLocation(Location location, Activity activity) {

        Address address = null;

        try {

            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses.size() > 0) {
                address = addresses.get(0);
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            return address;
        }
    }

    public static String addressOneLineFromLocation(Location location, Activity activity) {

        Address address = MapTools.addressFromLocation(location,activity);

        if (address != null) {

            StringBuilder addressString = new StringBuilder();

            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String addressLine = address.getAddressLine(0);
            addressString.append(addressLine);

            String postalCode = address.getPostalCode();
            addressString.append(", " + postalCode);

            String city = address.getLocality();
            addressString.append(" " + city);

            /*
            String state = address.getAdminArea();
            addressString.append(state);
            */

            /*
            String country = address.getCountryName();
            addressString.append(", " + country);
            */

            return addressString.toString();

        } else {
            return null;
        }
    }

    public static String getGoogleDirectionUrl(LatLng src, LatLng dest)
    {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json?");

        if(src!=null && dest!=null)
        {
            urlString.append("origin=");
            urlString.append(Double.toString((double) src.latitude));
            urlString.append(",");
            urlString.append(Double.toString((double) src.longitude));
            urlString.append("&destination=");// to
            urlString.append(Double.toString((double) dest.latitude));
            urlString.append(",");
            urlString.append(Double.toString((double) dest.longitude));
            urlString.append("&sensor=false&mode=driving&alternatives=false");
            // This is the old map key
            //urlString.append("&key=AIzaSyD9lnjOaXwfAamX1jdd0dqwJPn0Ld6wbns");
            urlString.append("&key=AIzaSyA6E6KPrm26Yls94c3tKMo2ejH9qSzR6TI");
        }
        return urlString.toString();
    }

    public static void centerMap(Context context,LatLngBounds.Builder  builder,final GoogleMap map){

        final LatLngBounds bounds = builder.build();

        final int padding = context.getResources().getDimensionPixelSize(R.dimen.map_bound_padding);

        try {
            if(map!=null){
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            }

        } catch (IllegalStateException ise) {

            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

                @Override
                public void onMapLoaded() {
                    if(map!=null){
                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                    }

                }
            });
        }


    }
    public static void updateClusteringRadius(Context context,ClusteringSettings clusterSetting,GoogleMap map) {
        if (clusterSetting == null) {
            clusterSetting = new ClusteringSettings();
            clusterSetting.addMarkersDynamically(true);
            clusterSetting.clusterSize(clusterRadiusCalculation(map));
            PanelClusterOptionsProvider provider = new PanelClusterOptionsProvider(context);
            map.setClustering(clusterSetting.clusterOptionsProvider(provider));
        } else if (map.getCameraPosition().zoom>13F){

        } else {
            clusterSetting.clusterSize(clusterRadiusCalculation(map));
        }

    }
    public static int clusterRadiusCalculation(GoogleMap map) {
        final int minRad = 0, maxRad = 150;
        final float minRadZoom = 10F, maxRadZoom = 7.333F;

        if (map.getCameraPosition().zoom >= minRadZoom) {

            return minRad;

        } else if (map.getCameraPosition().zoom <= maxRadZoom)
            return maxRad;
        else
            return (int) (maxRad - (maxRadZoom - map.getCameraPosition().zoom)
                    * (maxRad - minRad) / (maxRadZoom - minRadZoom));
    }

    public static float dynamicZoomLevel(GoogleMap map) {
        float currZoomLvl = map.getCameraPosition().zoom;
      /*  final float minZoomStepAtZoom = 17.3F, minZoomStep = 1.8F;
        final float maxZoomStepAtZoom = 7F, maxZoomStep = 2.8F;*/
        final float minZoomStepAtZoom = 16.3F, minZoomStep = 1.5F;
        final float maxZoomStepAtZoom = 6F, maxZoomStep = 1.5F;

        if (currZoomLvl >= minZoomStepAtZoom)
            return minZoomStep;
        else if (currZoomLvl <= maxZoomStepAtZoom)
            return maxZoomStep;
        else
            return (currZoomLvl - maxZoomStepAtZoom)
                    * (maxZoomStep - minZoomStep)
                    / (maxZoomStepAtZoom - minZoomStepAtZoom) + maxZoomStep;
    }

    public static String latitudeRef(double latitude) {
        return latitude<0.0d?"S":"N";
    }

    public static String longitudeRef(double longitude) {
        return longitude<0.0d?"W":"E";
    }


    synchronized public static final String convert(double latitude)
    {
        StringBuilder sb = new StringBuilder(20);

        latitude=Math.abs(latitude);
        int degree = (int) latitude;
        latitude *= 60;
        latitude -= (degree * 60.0d);
        int minute = (int) latitude;
        latitude *= 60;
        latitude -= (minute * 60.0d);
        int second = (int) (latitude*1000.0d);

        sb.setLength(0);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000,");
        return sb.toString();
    }


}
