package com.example.carsdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    EditText modelText, priceText, yearText, powerText, typeText, coeffText, countryText;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        modelText = findViewById(R.id.model);
        priceText = findViewById(R.id.price);
        yearText = findViewById(R.id.year);
        powerText = findViewById(R.id.power);
        typeText = findViewById(R.id.type);
        coeffText = findViewById(R.id.coeff);
        countryText = findViewById(R.id.country);
        addButton = findViewById(R.id.add_car);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Добавление записи в таблицу car_type
                //Этап 1. Получение данных из интерфейса
                String type = typeText.getText().toString();
                String country = countryText.getText().toString();
                double coeff = Double.parseDouble(coeffText.getText().toString());
                //TODO проверить, что такой записи нет в таблице
                //Составление прямого SQL-запроса (вариант 2 добавления данных в таблицу)
                String query = "INSERT INTO " + DBHelper.TABLE_TYPE +
                        " (" + DBHelper.COLUMN_TITLE + ", " + DBHelper.COLUMN_COUNTRY + ", " +
                        DBHelper.COLUMN_COEFF + ") VALUES (\"" + type + "\", \"" + country + "\", " +
                        coeff + ");";
                db.execSQL(query);
                //получить _id новой марки (для внешнего ключа таблицы cars)
                //SELECT _id FROM car_type WHERE title = type and country = country
                query = "SELECT _id FROM " + DBHelper.TABLE_TYPE + " WHERE " +
                        DBHelper.COLUMN_TITLE + " = \"" + type + "\" AND " +
                        DBHelper.COLUMN_COUNTRY + "=\"" + country + "\";";
                Cursor cursor = db.rawQuery(query, null);
                //извлечь из курсора информацию
                cursor.moveToFirst();
                int index = cursor.getColumnIndex("_id");
                int _id = -1;
                if (index >= 0)
                    _id = cursor.getInt(index);
                //Составление набора пар для добавления записи в таблицу cars (вариант 1 добавления)
                ContentValues contentValues = new ContentValues();
                if (_id >= 0)
                    contentValues.put(DBHelper.COLUMN_ID_TYPE, _id);
                contentValues.put(DBHelper.COLUMN_YEAR,
                        Integer.parseInt(yearText.getText().toString()));
                contentValues.put(DBHelper.COLUMN_MODEL, modelText.getText().toString());
                contentValues.put(DBHelper.COLUMN_POWER,
                        Double.parseDouble(powerText.getText().toString()));
                contentValues.put(DBHelper.COLUMN_PRICE,
                        Double.parseDouble(priceText.getText().toString()));
                db.insert(DBHelper.TABLE_CARS, null, contentValues);
                cursor.close();
                Toast.makeText(getApplicationContext(), "Данные записаны", Toast.LENGTH_SHORT).show();
                modelText.setText("");
                priceText.setText("");
                yearText.setText("");
                powerText.setText("");
                typeText.setText("");
                coeffText.setText("");
                countryText.setText("");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}