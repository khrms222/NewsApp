package com.example.ken.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Donk on 7/25/2017.
 */

public class ScheduleUtils {

    //The amount of minutes to refresh the database
    private static final int SCHEDULE_INTERVAL_MINUTES = 1;

    //A time window given to complete the job.
    private static final int SYNC_FLEXTIME_SECONDS = 10;

    private static final String NEWS_JOB_TAG = "news_job_tag";

    //Make sure we only have 1 instance of this class to call from
    private static boolean sInitialized;

    synchronized public static void scheduleRefresh(@NonNull final Context context){
        if(sInitialized) return;

        //Creating a job object
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        //Set the details of the job and set the job to NewsJob
        Job constraintRefreshJob = dispatcher.newJobBuilder()
                .setService(NewsJob.class)
                .setTag(NEWS_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULE_INTERVAL_MINUTES,
                        SCHEDULE_INTERVAL_MINUTES + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        //Set the details of the job to the jobdispatcher
        dispatcher.schedule(constraintRefreshJob);
        sInitialized = true;

    }
}
