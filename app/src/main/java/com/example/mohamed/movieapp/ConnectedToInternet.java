package com.example.mohamed.movieapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Mohamed on 9/17/2016.
 */
public class ConnectedToInternet {

    public boolean isConnectedToInternet(Context getApplicationContext){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}
