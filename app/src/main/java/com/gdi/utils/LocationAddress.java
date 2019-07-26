package com.gdi.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationAddress {
    private static final String TAG = "LocationAddress";

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                //String result = null;
                String lat = null;
                String log = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append(",");
                        }
                        sb.append(address.getLocality()).append(",");
                        sb.append(address.getPostalCode()).append(",");
                        sb.append(address.getCountryName());
                        lat = "" + address.getLatitude();
                        log = "" + address.getLongitude();
                        //result = sb.toString();
                    }
                } catch (IOException e) {
                    AppLogger.e(TAG, "Unable connect to Geocoder" +  e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (lat != null && log != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        //result = "Latitude: " + latitude + " Longitude: " + longitude + "\n\nAddress:\n" + result;
                        bundle.putString("latitude", lat);
                        bundle.putString("longitude", log);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        /*result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n Unable to get address for this lat-long.";*/
                        lat = "Unable to get address for this lat-long.";
                        log = "Unable to get address for this lat-long.";
                        bundle.putString("latitude", lat);
                        bundle.putString("longitude", log);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
