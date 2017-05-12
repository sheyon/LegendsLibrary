package com.sheyon.fivecats.legendslibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sheyon.fivecats.legendslibrary.data.LegendsContract;
import com.sheyon.fivecats.legendslibrary.data.LegendsHelper;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView legendsExpandableView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        legendsExpandableView = (ExpandableListView) findViewById(R.id.legends_expandable_list);

        legendsExpandableView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                LinearLayout ll = (LinearLayout) v;
                TextView tv = (TextView) ll.findViewById(R.id.subcategory_text_view);
                String clickedSubcatText = tv.getText().toString();

                Log.v("***STRING: ", "" + clickedSubcatText);
                return false;
            }
        });

        displayDatabaseInfo();
    }

    private void displayDatabaseInfo()
    {
        LegendsHelper legendsHelper = new LegendsHelper(this);
        SQLiteDatabase legendsDB = legendsHelper.getReadableDatabase();

        cursor = legendsDB.rawQuery(LegendsContract.CAT_QUERY, null, null);

        LegendsCursorTreeAdapter legendsCursorTreeAdaptor = new LegendsCursorTreeAdapter(cursor, this);
        legendsExpandableView.setAdapter(legendsCursorTreeAdaptor);
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeCursor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCursor();
    }

    private void closeCursor()
    {
        if (cursor != null)
        {
            cursor.close();
        }
    }
}