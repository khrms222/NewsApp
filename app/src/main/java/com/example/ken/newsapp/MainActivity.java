package com.example.ken.newsapp;

import android.net.Network;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ProgressBar progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.resultText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNum = item.getItemId();

        if(itemNum == R.id.search){
            NetworkTask networkTask = new NetworkTask();
            networkTask.execute();
        }

        return true;
    }

    class NetworkTask extends AsyncTask<URL, Void, String>{

        String searchQuery;
        String sortBy;
        String apiKey;

        NetworkTask(){
            searchQuery = "the-next-web";
            sortBy = "latest";
            apiKey = "4bbc5a00be8d40448ad056a1acc0d68a";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            String result = null;
            URL url = NetworkUtils.makeURL(searchQuery, sortBy, apiKey);
            Log.d(TAG, url.toString());
            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);

            if(s == null){
                textView.setText("Failed to retrieve result");
            }
            else{
                textView.setText(s);
            }
        }
    }
}
