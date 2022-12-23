package com.sheyon.fivecats.legendslibrary;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class CardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        String[] gridItems = getResources().getStringArray(R.array.cardResource);
        String[] altGridItems = getResources().getStringArray(R.array.cardFoil);

        GridView gridView = findViewById(R.id.cardActivity_gridView);
        CardsAdapter arrayAdapter = new CardsAdapter(this, R.array.cardResource, gridItems, altGridItems);
        gridView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}