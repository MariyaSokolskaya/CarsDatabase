package com.example.carsdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {
    Spinner spinner;
    Button findButton;
    TextView resultText;

    DBHelper helper;
    SQLiteDatabase db;
    ArrayAdapter<String> arrayAdapter;
    String selectedType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        spinner = findViewById(R.id.type_list);
        findButton = findViewById(R.id.find_button);
        resultText = findViewById(R.id.find_result);

        helper = new DBHelper(this);
        db = helper.getReadableDatabase();

        loadTypes();
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedType.equals("none")){
                    resultText.setText("Вам надо выбрать марку");
                }
                else {
                    Cursor cursor = db.query(DBHelper.TABLE_TYPE, new String[]{"_id"},
                            DBHelper.COLUMN_TITLE + "=\"" + selectedType + "\"",
                            null, null, null, null);
                    cursor.moveToFirst();
                    int id = cursor.getInt(0);

                    cursor.close();
                    Cursor cursor1 = db.query(DBHelper.TABLE_CARS,
                            new String[]{DBHelper.COLUMN_MODEL, DBHelper.COLUMN_PRICE},
                            DBHelper.COLUMN_ID_TYPE + " = " + id,
                            null, null, null, null);
                    resultText.setText("");
                    cursor1.moveToFirst();
                    int ind = cursor1.getColumnIndex(DBHelper.COLUMN_MODEL);
                    String str = cursor1.getString(ind);
                    ind = cursor1.getColumnIndex(DBHelper.COLUMN_PRICE);
                    str += " - " + cursor1.getDouble(ind) + "\n";
                    resultText.setText(str);
                    while (cursor1.moveToNext()){
                        ind = cursor1.getColumnIndex(DBHelper.COLUMN_MODEL);
                        str = cursor1.getString(ind);
                        ind = cursor1.getColumnIndex(DBHelper.COLUMN_PRICE);
                        str += " - " + cursor1.getDouble(ind) + "\n";
                        resultText.append(str);
                    }
                    cursor1.close();
                }
            }
        });
    }

    private void loadTypes(){
        //взять все марки автомобилей из базы данных
        Cursor cursor = db.query(DBHelper.TABLE_TYPE, new String[]{DBHelper.COLUMN_TITLE},
                null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> typeList = new ArrayList<>();
        typeList.add(cursor.getString(0));
        while (cursor.moveToNext()) {
            typeList.add(cursor.getString(0));
        }
        cursor.close();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, typeList);
        spinner.setAdapter(arrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedType = "none";
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}