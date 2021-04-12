package com.example.menu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    final DBOpenHelper dbOpenHelper = new DBOpenHelper(this);
    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_record);
        dbOpenHelper.open();
        Intent intent_data  = getIntent();
        final String name = intent_data.getExtras().getString("name"); //입력한 이름값 받아오기

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
        showDatabase(name); //입력됐던 정보 출력

    }

    public void showDatabase(String sort){
        dbOpenHelper.open();
        Cursor iCursor = dbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());

        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){//행을 옮기면서 data, steps 출력
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("date"));

            String tempsteps = iCursor.getString(iCursor.getColumnIndex("steps"));
            tempsteps = setTextLength(tempsteps,10000);


            String Result = "날짜 : "+tempIndex + "   걸음 수 : " + tempsteps;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }
}
