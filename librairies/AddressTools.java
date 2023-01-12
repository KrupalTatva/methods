package com.app.fap.librairies;

import android.location.Address;
import android.text.TextUtils;

import java.util.List;

/****************************************************
 * Created by Tahiana-MadiApps on 12/08/2016.
 ****************************************************/
public class AddressTools {
    private String adressLine;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    private String cityAndCountry;

    public AddressTools() {
        this.adressLine = "";
        this.city = "";
        this.state = "";
        this.country = "";
        this.postalCode = "";
        this.knownName = "";
    }

    public AddressTools(List<Address> addresses) {
        adressParser(addresses);
    }

    public AddressTools(String adressLine, String city, String state, String country, String postalCode, String knownName) {
        this.adressLine = adressLine;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.knownName = knownName;
    }

    public String getAdressLine() {
        return adressLine;
    }

    public void setAdressLine(String adressLine) {
        this.adressLine = adressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        if(!GenericTools.isNullOrEmpty(postalCode)){
            String[] temp = postalCode.split(",");
            return temp[0];
        }
        return postalCode;
    }

    public String getCityAndCountry() {

        String cityAndCountry ="";

        if(!TextUtils.isEmpty(city)){

            cityAndCountry = city;
        }
        if(!TextUtils.isEmpty(country)){

            if(!GenericTools.isNullOrEmpty(cityAndCountry)){

                cityAndCountry = cityAndCountry+", "+country;

            }else{
                cityAndCountry=country;
            }

        }
        return cityAndCountry;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }

    private void adressParser(List<Address> addresses){
        if (addresses.size() > 0 && addresses.get(0)!=null) {
            adressLine = getValue(addresses.get(0).getAddressLine(0));
            city = getValue(addresses.get(0).getLocality());
            state = getValue(addresses.get(0).getAdminArea());
            country = getValue(addresses.get(0).getCountryName());
            postalCode = getValue(addresses.get(0).getPostalCode());
            knownName = getValue(addresses.get(0).getFeatureName());
        }
    }

    private String  getValue(String value){
        if(!GenericTools.isNullOrEmpty(value)){
            return value;
        }
        return "";
    }

}
