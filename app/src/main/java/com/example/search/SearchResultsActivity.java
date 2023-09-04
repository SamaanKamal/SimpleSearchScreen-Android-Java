package com.example.search;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchResultsActivity extends AppCompatActivity {

    ListView productsList;
    ArrayAdapter<String> productsAdapter;
    database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        productsList = findViewById(R.id.res);
        productsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        productsList.setAdapter(productsAdapter);
        db = new database(this);
        String res =getIntent().getExtras().getString("ProductSearched");
        Cursor cursor;
        cursor=db.getProducts(res);
        while (!cursor.isAfterLast())
        {
            String content= cursor.getString(0)+cursor.getString(1)+cursor.getString(2);
            productsAdapter.add(content);
            cursor.moveToNext();
        }
    }
}