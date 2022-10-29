package com.samebe.dbtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class UnitSelection extends AppCompatActivity {

    SQLiteOpenHelper helper;
    SQLiteDatabase database;
    TextView t1;
    Button confirmUnits;
    SimpleCursorAdapter adapter;
    SimpleCursorAdapter adapterSelected;
    ListView l2;
    ListView listSelected;
    TextView textResult;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_selection);

        t1 = findViewById(R.id.t1);
        l2 = findViewById(R.id.l2);
        listSelected = findViewById(R.id.list_selected);

        helper = new SQLiteOpenHelper(UnitSelection.this,"db_playerunit.db",null,1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(
                        "CREATE TABLE player (\n" +
                                " pla_id INTEGER NOT NULL UNIQUE PRIMARY KEY,\n" +
                                " pla_name TEXT,\n" +
                                " pla_level INTEGER)"
                );
                db.execSQL(
                        "CREATE TABLE player (\n" +
                                " pla_id INTEGER NOT NULL UNIQUE PRIMARY KEY,\n" +
                                " pla_name TEXT,\n" +
                                " pla_level INTEGER)"
                );

                db.execSQL(
                        "CREATE TABLE unit (\n" +
                                " uni_id INTEGER NOT NULL UNIQUE PRIMARY KEY, \n" +
                                " uni_name TEXT, \n" +
                                " uni_attack INTEGER, \n" +
                                " uni_defense INTEGER, \n" +
                                " uni_health INTEGER, \n" +
                                " uni_attack_speed INTEGER, \n" +
                                " uni_level INTEGER)"
                );

                // advantage of creating player_unit table :
                // whenever a new unit is added we just need to insert a link between pla_id and the new uni_id */
                db.execSQL(
                        "CREATE TABLE player_unit (\n" +
                                " plu_id INTEGER NOT NULL UNIQUE PRIMARY KEY, \n" +
                                " plu_selected INTEGER, \n" +
                                " pla_id INTEGER, \n" +
                                " uni_id INTEGER, \n" +
                                " CONSTRAINT fk_pla_id FOREIGN KEY(pla_id) REFERENCES player(pla_id), " +
                                " CONSTRAINT fk_uni_id FOREIGN KEY(uni_id) REFERENCES unit(uni_id))"
                );
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int i, int i1) {

            }
        };

        database = helper.getWritableDatabase();

        database.execSQL("DELETE FROM player");
        database.execSQL("DELETE FROM unit");
        database.execSQL("DELETE FROM player_unit");

        database.execSQL("INSERT INTO player (pla_name, pla_level) VALUES ('Player 1', 10)");

        database.execSQL("INSERT INTO unit (uni_name, uni_attack, uni_defense, uni_health, uni_attack_speed, uni_level) " +
                                    "VALUES  ('Swordsman', 50, 20, 240, 1, 1)," +
                                            "('Archer', 80, 20, 120, 1, 1)," +
                                            "('Healer', 30, 10, 120, 1, 1)," +
                                            "('Beast', 60, 10, 240, 1, 1)," +
                                            "('Bard', 30, 10, 120, 1, 1)");

        database.execSQL("INSERT INTO player_unit (pla_id, uni_id, plu_selected) " +
                "VALUES  (1, 1, 0)," +
                        "(1, 2, 0)," +
                        "(1, 4, 0)," +
                        "(1, 5, 0)");

        /* get all units of the player */
        Cursor c = database.rawQuery("SELECT uni.uni_id as _id, uni.uni_name, plu.plu_selected FROM unit uni INNER JOIN player_unit plu ON plu.uni_id = uni.uni_id INNER JOIN player pla ON plu.pla_id = pla.pla_id WHERE pla.pla_id = 1; ", null);
        String [] from = {"uni_name", "_id"};
        int [] to = {R.id.c1, R.id.tid};

        adapter = new SimpleCursorAdapter(UnitSelection.this, R.layout.item_unit_selection, c, from, to, 0);

        l2.setAdapter(adapter);

        confirmUnits = findViewById(R.id.update_units);

        Intent i = getIntent();
        t1.setText("Select your units " + i.getStringExtra("pla_id").toString());

        confirmUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0; i < l2.getChildCount(); i++) {
                    CheckBox cb = l2.getChildAt(i).findViewById(R.id.c1);
                    TextView tid = l2.getChildAt(i).findViewById(R.id.tid);
                    System.out.println(cb.getText() + " " +cb.isChecked() + " id=" + tid.getText());

                    /* Update player_units table with checked status */
                    database.execSQL("UPDATE player_unit SET plu_selected = " + (cb.isChecked() ? "1" : "0") + " WHERE uni_id = '" + tid.getText() + "'");
                }

                /* get all SELECTED units of the player */
                Cursor cSelected = database.rawQuery("SELECT uni.uni_id as _id, uni.uni_name, plu.plu_selected, uni.uni_attack FROM unit uni INNER JOIN player_unit plu ON plu.uni_id = uni.uni_id INNER JOIN player pla ON plu.pla_id = pla.pla_id WHERE pla.pla_id = 1 AND plu.plu_selected = 1; ", null);
                String [] from = {"uni_name", "uni_attack"};
                int [] to = {android.R.id.text1, android.R.id.text2};

                adapterSelected = new SimpleCursorAdapter(UnitSelection.this, android.R.layout.simple_list_item_2, cSelected, from, to, 0);
                listSelected.setAdapter(adapterSelected);

            }
        });

    }
}