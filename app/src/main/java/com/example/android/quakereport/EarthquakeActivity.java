/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.DownloadManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {



    private EarthquakeArrayAdapter adapter;

    private ProgressBar loadingSpinner;

    private TextView mTextView;

    private boolean isConnected;

    private Button RetryBtn;


    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL ="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(LOG_TAG,"OnCreate Called...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        mTextView= (TextView) findViewById(R.id.empty_txt);

        loadingSpinner= (ProgressBar) findViewById(R.id.loading_spinner);

        RetryBtn= (Button) findViewById(R.id.retry_btn);

        // Find a reference to the {@link ListView} in the layout
        ListView EarthquakeListView = (ListView) findViewById(R.id.list);

        EarthquakeListView.setEmptyView(mTextView);


        // Create a new adapter that takes an empty list of earthquakes as input
        adapter = new EarthquakeArrayAdapter(this, new ArrayList<Earthquake>());

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            RetryBtn.setVisibility(View.GONE);
            getLoaderManager().initLoader(1,null,this);
            Log.i(LOG_TAG,"initLoader Called...");
        }
        else{
            RetryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isConnected) {
                        RetryBtn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingSpinner.setVisibility(View.VISIBLE);
                                getLoaderManager().initLoader(1, null, EarthquakeActivity.this);
                                Log.i(LOG_TAG, "initLoader Called...");
                            }
                        },1000);

                    }
                }
            });
            loadingSpinner.setVisibility(View.GONE);
            mTextView.setText("No Internet Connection");
        }

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        assert EarthquakeListView != null;
        EarthquakeListView.setAdapter(adapter);

//        EarthquakeTask earthquakeTask=new EarthquakeTask();
//        earthquakeTask.execute(USGS_REQUEST_URL);

        //set on item click listener on earthquakeListView
        EarthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get selected earthquake item
                Earthquake currentEarthquake=adapter.getItem(i);
                assert currentEarthquake != null;
                openWebPage(currentEarthquake.getUrl());
            }
        });
    }



    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG,"OnCreateLoader Called...");
        return new EarthquakeLoader(this,USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {

        Log.i(LOG_TAG,"OnLoadFinished Called...");

        loadingSpinner.setVisibility(View.GONE);

        if (isConnected){
            mTextView.setText("No Earthquake data found");
        }
        else{
            mTextView.setText("No Internet Connection");
        }


        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {

        Log.i(LOG_TAG,"OnLoadReset Called...");

        // Clear the adapter of previous earthquake data
        adapter.clear();
    }


//    private class EarthquakeTask extends AsyncTask<String, Void, List<Earthquake>>{
//
//        @Override
//        protected List<Earthquake> doInBackground(String... urls) {
//            // Don't perform the request if there are no URLs, or the first URL is null.
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//
//            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
//            return result;
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPostExecute(List<Earthquake> data) {
//            // Clear the adapter of previous earthquake data
//            adapter.clear();
//
//            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
//            // data set. This will trigger the ListView to update.
//            if (data != null && !data.isEmpty()) {
//                adapter.addAll(data);
//            }
//            super.onPostExecute(data);
//        }
//    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
