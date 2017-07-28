package com.example.ken.newsapp;

import android.os.AsyncTask;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Donk on 7/25/2017.
 */

public class NewsJob extends JobService {
    AsyncTask mBackgroundTask;

    /*
    *
    *   The job to perform in the background service when ScheduleUitls is called.
    *   Update the database whenever this job is performed.
    *
    * */
    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                Toast.makeText(NewsJob.this, "News refreshed", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                RefreshUtils.updateNewsArticles(NewsJob.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
                super.onPostExecute(o);

            }
        };


        mBackgroundTask.execute();

        return true;
    }

    /*
    *   Stop the background task
    * */
    @Override
    public boolean onStopJob(JobParameters job) {

        if (mBackgroundTask != null) mBackgroundTask.cancel(false);

        return true;
    }
}
