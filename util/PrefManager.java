

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class PrefManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String KEY_HAS_LOCATION = "hashLocation";

    private Gson gson;

    public PrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public PrefManager(Context context, Gson gson) {
        this.gson = gson;
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void cacheUser(LoginModel profile) {
        String user = gson.toJson(profile);
        editor.putString(Constants.CACHED_USER, user);
        editor.commit();
    }

    public LoginModel getCachedUser() {
        String user = sharedPreferences.getString(Constants.CACHED_USER, null);
        return gson.fromJson(user, LoginModel.class);
    }

    public void loggedInUser(String userType) {
        editor.putString(Constants.LOGGED_USER, userType);
        editor.commit();
    }

    public String getloggedInUser() {
        String user = sharedPreferences.getString(Constants.LOGGED_USER, "");
        return user;
    }

    public void setGpsEnable (boolean gpsOff) {
        editor.putBoolean(IS_GPS, gpsOff);
        editor.commit();
    }

    public boolean getGpsEnable () {
        boolean gps = sharedPreferences.getBoolean(IS_GPS, false);
        return gps;
    }

    public void setCoordiGeneratedTime (String current_time) {
        editor.putString(COORD_TIME_IST, current_time);
        editor.commit();
    }

    public String getCoordiGeneratedTime () {
        String time = sharedPreferences.getString(COORD_TIME_IST, "");
        return time;
    }

    public String getLongitude() {
        String longitude = sharedPreferences.getString(LONGITUDE, "");
        return longitude;
    }

    public void setLongitude(String longitude) {
        editor.putString(LONGITUDE, longitude);
        editor.commit();
    }


    public String getLatitude() {
        String latitude = sharedPreferences.getString(LATITUDE, "");
        return latitude;
    }

    public void setLatitude(String latitude) {
        editor.putString(LATITUDE, latitude);
        editor.commit();
    }

    public void setVehicleType(int id) {
        editor.putInt(Constants.Vehicle_Type, id);
        editor.commit();
    }

    public int getVehicleType() {
        int user = sharedPreferences.getInt(Constants.Vehicle_Type, 1);
        return user;
    }

    public void setCustomerID(int userID) {
        editor.putInt(Constants.Customer_ID, userID);
        editor.commit();
    }

    public int getCustomerID() {
        int user = sharedPreferences.getInt(Constants.Customer_ID, 0);
        return user;
    }

    public void setISBackground(boolean background) {
        editor.putBoolean(Constants.background, background);
        editor.commit();
    }

    public boolean getISBackground() {
        boolean background = sharedPreferences.getBoolean(Constants.background, false);
        return background;
    }

    public void setIsGps(boolean gps) {
        editor.putBoolean(Constants.GPS, gps);
        editor.commit();
    }

    public boolean getIsGps() {
        boolean gps = sharedPreferences.getBoolean(Constants.GPS, false);
        return gps;
    }


    public void setCustomerIDNomenClature(String userID) {
        editor.putString(Constants.Customer_ID_Nomen, userID);
        editor.commit();
    }

    public void saveLocationArray(ArrayList<LocationModel> locationModelArrayList) {
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(locationModelArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("locationData", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();
    }

    public void clearLocationArray() {
        editor.remove("locationData").apply();
    }

    public ArrayList<LocationModel> loadLocationData() {
        Gson gson = new Gson();

        String json = sharedPreferences.getString("locationData", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<LocationModel>>() {
        }.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        ArrayList<LocationModel> locationModalArrayList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (locationModalArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            locationModalArrayList = new ArrayList<>();
        }
        return locationModalArrayList;
    }


    public String getCustomerIDNomenClature() {
        String user = sharedPreferences.getString(Constants.Customer_ID_Nomen, "0");
        return user;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(Constants.LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean IS_APPLICATION_SUBMITTED() {
        return sharedPreferences.getBoolean(Constants.IS_APPLICATION_SUBMITTED, false);
    }

    public void setAPPLICATION_SUBMITTED(boolean isLoggedIn) {
        editor.putBoolean(Constants.IS_APPLICATION_SUBMITTED, isLoggedIn);
        editor.commit();
    }


    public HashMap<String, ArrayList<DBLocation>> getLocationHashMap() {
        String storedHashMapString = sharedPreferences.getString(KEY_HAS_LOCATION, "");
        if (TextUtils.isEmpty(storedHashMapString)) {
            return new HashMap<String, ArrayList<DBLocation>>();
        } else {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<DBLocation>>>() {
            }.getType();
            return gson.fromJson(storedHashMapString, type);
        }
    }

    public void clearPref() {
        String storedHashMapString = sharedPreferences.getString(KEY_HAS_LOCATION, "");
        clear();
        sharedPreferences.edit().putString(KEY_HAS_LOCATION, storedHashMapString).apply();
    }

    public void clearLocationHashMap(String custId) {
        HashMap<String, ArrayList<DBLocation>> locationHashMap = getLocationHashMap();
        if (locationHashMap != null && locationHashMap.containsKey(custId)) {
            locationHashMap.remove(custId);
        }
        Gson gson = new Gson();
        String hashMapString = gson.toJson(locationHashMap);
        sharedPreferences.edit().putString(KEY_HAS_LOCATION, hashMapString).apply();

    }

    public void storeLocationHashMap(String custId, ArrayList<DBLocation> model) {
        HashMap<String, ArrayList<DBLocation>> locationHashMap = getLocationHashMap();
        if (locationHashMap != null && locationHashMap.containsKey(custId)) {
            locationHashMap.remove(custId);
            locationHashMap.put(custId, model);
        } else if (locationHashMap != null) {
            locationHashMap.put(custId, model);
        }
        Gson gson = new Gson();
        String hashMapString = gson.toJson(locationHashMap);
        sharedPreferences.edit().putString(KEY_HAS_LOCATION, hashMapString).apply();
    }

    /*public void clearLocationArrayHash(String  custId){
        HashMap<String, ArrayList<LocationModel>> map = new HashMap<String , ArrayList<LocationModel>>();
        if(map.size()>0){
            if(map.containsKey(custId)){
               map.remove(custId);
            }
        }

    }*/

    public ArrayList<DBLocation> loadLocationArrayHash(String custId) {
        HashMap<String, ArrayList<DBLocation>> map = getLocationHashMap();
        if (map != null && map.containsKey(custId)) {
            return map.get(custId);
        } else {
            return new ArrayList<>();
        }
    }

    public void clear() {
        Map<String, ?> prefs = sharedPreferences.getAll();
        for (Map.Entry<String, ?> prefToReset : prefs.entrySet()) {
            if (!prefToReset.getKey().equals(Constants.LANGUAGE)) {
                editor.remove(prefToReset.getKey()).commit();
            }
        }
    }

    /**
     * Current Location
     */
    public void saveLocationData(double latitude, double longitude) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        editor.putString(LATITUDE, String.valueOf(latitude));
        editor.putString(LONGITUDE, String.valueOf(longitude));
        // Commit the edits!
        editor.commit();
    }

    public ArrayList<Double> getLocationData() {
        ArrayList<Double> location = new ArrayList<>();
        if (!sharedPreferences.getString(LATITUDE, "").isEmpty()) {
            location.add(Double.valueOf(Objects.requireNonNull(sharedPreferences.getString(LATITUDE, ""))));
            location.add(Double.valueOf(Objects.requireNonNull(sharedPreferences.getString(LONGITUDE, ""))));
            return location;
        }
        return location;
    }

    public String getLanguage() {
        String language = sharedPreferences.getString(Constants.LANGUAGE, "");
        return language;
    }

    public void setLanguage(String language) {
        editor.putString(Constants.LANGUAGE, language);
        editor.commit();
    }

    public void setDeviceToken(String userID) {
        editor.putString(Constants.DEVICE_TOKEN, userID);
        editor.commit();
    }

    public String getDeviceToken() {
        String user = sharedPreferences.getString(Constants.DEVICE_TOKEN, "");
        return user;
    }

    public void setLoggedInMobileNo(String loggedInMobileNo) {
        editor.putString(Constants.LOGGED_IN_MOB_NO, loggedInMobileNo);
        editor.commit();
    }

    public String getLoggedInMobileNo() {
        String loggedInMobileNo = sharedPreferences.getString(Constants.LOGGED_IN_MOB_NO, "");
        return loggedInMobileNo;
    }

    public void setAdminAppConfiguration(boolean adminAppConfiguration) {
        editor.putBoolean(Constants.ADMIN_APP_CONFIG, adminAppConfiguration);
        editor.commit();
    }

    public boolean IsAdminAppConfigurationEnabled() {
        return sharedPreferences.getBoolean(Constants.ADMIN_APP_CONFIG, false);
    }

    public void setWalletPin(boolean isWalletPinEnabled) {
        editor.putBoolean(Constants.WALLET_PIN, isWalletPinEnabled);
        editor.commit();
    }

    public boolean isWalletPinEnabled() {
        return sharedPreferences.getBoolean(Constants.WALLET_PIN, false);
    }

    public void setAuthToken(String userID) {
        editor.putString(Constants.AUTH_TOKEN, userID);
        editor.commit();
    }

    public String getAuthToken() {
        String user = sharedPreferences.getString(Constants.AUTH_TOKEN, "");
        return user;
    }


    public void cacheUserData(ProfileData profile) {
        String user = gson.toJson(profile);
        editor.putString(Constants.CACHED_USER_DATA, user);
        editor.commit();
    }

    public ProfileData getCachedUserData() {
        String user = sharedPreferences.getString(Constants.CACHED_USER_DATA, null);
        return gson.fromJson(user, ProfileData.class);
    }


}
