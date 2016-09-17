package com.example.mohamed.movieapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Mohamed on 9/17/2016.
 */
public class ConnectedToInternet {

    public boolean isConnectedToInternet(Context getApplicationContext){
        NetworkInfo info = ((ConnectivityManager) getApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected())
        {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable internet while roaming, just return false
            return true;
        }

        return true;

    }
}
