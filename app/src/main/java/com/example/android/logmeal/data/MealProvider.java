package com.example.android.logmeal.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Switch;

import com.example.android.logmeal.data.MealContract.MealEntry;

/**
 * Created by MIRZA on 3/11/2017.
 */
public class MealProvider extends ContentProvider {

    public static final String LOG_TAG = MealProvider.class.getSimpleName();

    private static final int MEALS = 100;

    private static final int MEALS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MealContract.CONTENT_AUTHORITY, MealContract.PATH_MEALS, MEALS);

        sUriMatcher.addURI(MealContract.CONTENT_AUTHORITY, MealContract.PATH_MEALS + "/#", MEALS_ID);
    }

    private MealDbHelper mDbHelper;


    @Override
    public boolean onCreate() {
        mDbHelper = new MealDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MEALS:
                cursor = database.query(MealEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MEALS_ID:
                selection = MealEntry._ID + "=?";
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = database.query(MealEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MEALS:
                return MealEntry.CONTENT_LIST_TYPE;
            case MEALS_ID:
                return MealEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEALS:
                return insertMeal(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertMeal(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(MealEntry.COLOMN_MEAL_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Meal requires a name");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer calorie = values.getAsInteger(MealEntry.COLOMN_MEAL_CALORIE);
        if (calorie != null && calorie < 0) {
            throw new IllegalArgumentException("Meal requires valid calorie");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(MealEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEALS:
                rowsDeleted = database.delete(MealEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEALS_ID:
                selection = MealEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(MealEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for "+ uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MEALS:
                return updateMeal(uri, contentValues, selection, selectionArgs);
            case MEALS_ID:
                selection = MealEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMeal(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateMeal(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(MealEntry.COLOMN_MEAL_NAME)) {
            String name = values.getAsString(MealEntry.COLOMN_MEAL_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Meal requires a name");
            }
        }

        if (values.containsKey(MealEntry.COLOMN_MEAL_CALORIE)) {
            Integer calorie = values.getAsInteger(MealEntry.COLOMN_MEAL_CALORIE);
            if (calorie != null && calorie < 0) {
                throw new IllegalArgumentException("Meal requires valid calorie");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(MealEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
