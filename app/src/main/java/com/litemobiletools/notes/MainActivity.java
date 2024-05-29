package com.litemobiletools.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);

        dataList = new ArrayList<>();
        loadData();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
    }

    public void saveButton(View view){

        EditText edittextt = (EditText) findViewById(R.id.edittextt);
        String newTitle = edittextt.getText().toString();

        if (newTitle.isEmpty()) {
            Toast.makeText(MainActivity.this, "Cannot save empty data", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isInserted = insertData(newTitle);

        if (isInserted) {
            Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_LONG).show();
            dataList.add(0,newTitle);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "Error, could not save", Toast.LENGTH_LONG).show();
        }

    }

    public boolean insertData(String title) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2, title);
        long result = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        return result != -1;
    }

    private void loadData() {
        Cursor res = dbHelper.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (res.moveToNext()) {
                dataList.add(res.getString(1)); // Assuming column 1 is the title
            }
        }
    }

}