package com.example.ken.newsapp;

import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ProgressBar progressBar;
    //private TextView textView;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //textView = (TextView) findViewById(R.id.resultText);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    class NetworkTask extends AsyncTask<URL, Void, ArrayList<NewsItem>>{

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
        protected ArrayList<NewsItem> doInBackground(URL... params) {
            ArrayList<NewsItem> newsItemsList = null;
            URL url = NetworkUtils.makeURL(searchQuery, sortBy, apiKey);
            Log.d(TAG, url.toString());
            try {
                String result = NetworkUtils.getResponseFromHttpUrl(url);
                newsItemsList = NetworkUtils.parseJSON(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            return newsItemsList;
        }

        @Override
        protected void onPostExecute(final ArrayList<NewsItem> data) {
            super.onPostExecute(data);
            progressBar.setVisibility(View.GONE);

            if(data != null){
                NewsAdapter adapter = new NewsAdapter(data, new NewsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int clickedItemIndex) {
                        String url = data.get(clickedItemIndex).getUrl();

                        Uri webpage = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }

                    }
                });

                recyclerView.setAdapter(adapter);
            }
            else{
                //textView.setText(s);
            }
        }
    }
}
