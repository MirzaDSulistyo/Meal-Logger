package com.example.android.logmeal.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by MIRZA on 3/9/2017.
 */
public final class MealContract {

    // Agar contract tidak dapat di instantiating
    // maka di buatlah constructor yang kosong
    private MealContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.logmeal";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MEALS = "meals";


    public static final class MealEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEALS);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                "/" + PATH_MEALS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                "/" + PATH_MEALS;

        /* Nama tabel database untuk meals  */
        public final static String TABLE_NAME = "meals";

        public final static String _ID = BaseColumns._ID;

        public final static String COLOMN_MEAL_NAME = "name";

        public final static String COLOMN_MEAL_CALORIE = "calorie";


    }
}
