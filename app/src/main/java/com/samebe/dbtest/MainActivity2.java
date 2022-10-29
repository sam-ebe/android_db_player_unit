package com.samebe.dbtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity2 extends AppCompatActivity {

    ListView l1;
    /* TODO: manage multiple teams per player, here's just a placeholder teamArray. Every entry points to the same team */
    String [] teamArray = {"Team 1","Team 2","Team 3","Team 4","Team 5"};
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        l1 = findViewById(R.id.l1);
        adapter = new ArrayAdapter(MainActivity2.this, android.R.layout.simple_list_item_1, android.R.id.text1, teamArray);
        l1.setAdapter(adapter);

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(MainActivity2.this, UnitSelection.class);
                i.putExtra("pla_id", "1");
                startActivity(i);
            }
        });
    }
}