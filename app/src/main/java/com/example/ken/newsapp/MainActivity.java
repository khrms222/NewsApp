package com.example.ken.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ken.newsapp.data.Contract;
import com.example.ken.newsapp.data.DBHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, NewsAdapter.ItemClickListener{

    private static final String TAG = "MainActivity";

    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    private SQLiteDatabase db;
    private Cursor cursor;

    private boolean firstLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstLaunch = prefs.getBoolean("firstLaunch", true);

        if (firstLaunch) {
            load();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }

        ScheduleUtils.scheduleRefresh(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        db = new DBHelper(MainActivity.this).getReadableDatabase();

        cursor = DatabaseUtils.getAll(db);

        adapter = new NewsAdapter(cursor, this);

        recyclerView.setAdapter(adapter);
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
            load();
            /*
            Bundle argsBundle = new Bundle();

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<ArrayList<NewsItem>> loader = loaderManager.getLoader(1);

            if(loader == null){
                loaderManager.initLoader(1, argsBundle, this).forceLoad();
            }
            else{
                loaderManager.restartLoader(1, argsBundle, this);
            }
            */

        }

        return true;
    }

    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar.setVisibility(View.VISIBLE);

                Log.d("tagmsg", "onStartLoading");
            }

            @Override
            public Void loadInBackground() {

                RefreshUtils.updateNewsArticles(MainActivity.this);

                Log.d("tagmsg", "loadInBackground");

                return null;


                /*
                String searchQuery = "the-next-web";
                String sortBy = "latest";
                String apiKey = "4bbc5a00be8d40448ad056a1acc0d68a";

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
                */
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        progressBar.setVisibility(View.GONE);

        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);

        adapter = new NewsAdapter(cursor, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Log.d("tagmsg", "onLoadFinished");

        /*
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
        */

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_URLTOIMAGE));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void load() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(1, null, this).forceLoad();
    }
}
