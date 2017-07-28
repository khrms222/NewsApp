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

    //A reference to the device's local sqllite database.
    private SQLiteDatabase db;

    //A reference to a cursor indicating a position in a database record(s).
    private Cursor cursor;

    //A bool variable to keep track if its the first time launching this app...
    private boolean firstLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Get a reference to the device's local preferences for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Check if "firstLaunch" already exists, if it does, then the app was previously launched
        // If not, store fireLaunch in the shared prefs
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

        //Instantiate the database reference.
        db = new DBHelper(MainActivity.this).getReadableDatabase();

        //Get all the records in the database and store it in cursor.
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
            //When the search menu item is clicked call load()
            load();
        }

        return true;
    }

    /*
    *   A asynctaskloader to refresh the database when search is clicked.
    * */
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

                //Method to refresh the database...
                RefreshUtils.updateNewsArticles(MainActivity.this);

                Log.d("tagmsg", "loadInBackground");

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        progressBar.setVisibility(View.GONE);

        //Reset the cursor to the new updated database
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);

        adapter = new NewsAdapter(cursor, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Log.d("tagmsg", "onLoadFinished");

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    /*
    *   When a NewsItem view object is clicked, open the news article in a browser.
    * */
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
