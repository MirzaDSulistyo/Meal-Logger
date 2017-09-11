package com.example.android.logmeal;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.logmeal.data.MealContract.MealEntry;

/**
 * Created by MIRZA on 3/15/2017.
 */
public class MealCursorAdapter extends CursorAdapter {

    public MealCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        int nameColomnIndex = cursor.getColumnIndex(MealEntry.COLOMN_MEAL_NAME);
        int calorieColomnIndex = cursor.getColumnIndex(MealEntry.COLOMN_MEAL_CALORIE);

        String mealName = cursor.getString(nameColomnIndex);
        int mealCalorie = cursor.getInt(calorieColomnIndex);

        nameTextView.setText(mealName);
        summaryTextView.setText("" + mealCalorie);
    }
}
