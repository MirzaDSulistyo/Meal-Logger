package com.example.android.logmeal;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.logmeal.data.MealContract.MealEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MEAL_LOADER = 0;

    MealCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView mealListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        mealListView.setEmptyView(emptyView);

        mCursorAdapter = new MealCursorAdapter(this, null);
        mealListView.setAdapter(mCursorAdapter);

        mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                Uri currentMealUri = ContentUris.withAppendedId(MealEntry.CONTENT_URI, id);

                intent.setData(currentMealUri);

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(MEAL_LOADER, null, this);
    }

    private void insertMeal() {

        ContentValues values = new ContentValues();
        values.put(MealEntry.COLOMN_MEAL_NAME, "Banana");
        values.put(MealEntry.COLOMN_MEAL_CALORIE, 100);

        Uri newUri = getContentResolver().insert(MealEntry.CONTENT_URI, values);
    }

    private void deleteAllMeals() {
        int rowsDeleted = getContentResolver().delete(MealEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows delete from meal databse");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertMeal();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllMeals();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                MealEntry._ID,
                MealEntry.COLOMN_MEAL_NAME,
                MealEntry.COLOMN_MEAL_CALORIE };

        return new CursorLoader(this,
                MealEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
