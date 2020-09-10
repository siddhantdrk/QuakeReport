package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeLoader.class.getName();

    private String url;
    public EarthquakeLoader(Context context,String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {

        Log.i(LOG_TAG,"OnStartLoading Called...");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {

        Log.i(LOG_TAG,"loadInBackground Called...");

        // Don't perform the request if there are no URLs, or the first URL is null.
        if (url==null||url.length()==0) {
            return null;
        }

        return QueryUtils.fetchEarthquakeData(url);
    }
}
