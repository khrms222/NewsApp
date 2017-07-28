package com.example.ken.newsapp.data;

import android.provider.BaseColumns;

/**
 * Created by Donk on 7/23/2017.
 */

public class Contract {

    public static class TABLE_ARTICLES implements BaseColumns {

        //The name of the table in the database
        public static final String TABLE_NAME = "news";

        //Name of the columns in the database
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_URLTOIMAGE = "url";
        public static final String COLUMN_NAME_PUBLISHDATE = "publishdate";
    }
}
