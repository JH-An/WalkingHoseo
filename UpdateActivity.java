package com.example.menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    ImageButton buttoninput;
    final DBOpenHelper dbOpenHelper = new DBOpenHelper(this);
    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_update);
        final DBOpenHelper dbOpenHelper = new DBOpenHelper(this);

        Intent intent_data  = getIntent();
        final String name = intent_data.getExtras().getString("name");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter); //데이터베이스 출력위한 리스트뷰 및 어뎁터
        showDatabase(name);//데이터베이스 출력
        buttoninput = (ImageButton) findViewById(R.id.button_input);


        buttoninput.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_layout_root));

                EditText Height_Info = (EditText) findViewById(R.id.input_height);
                EditText Weight_info = (EditText) findViewById(R.id.input_weight);
                EditText Age_info = (EditText) findViewById(R.id.input_age);
                RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);
                int Sex_info = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(Sex_info);
                //데이터 삽입
                long check_input = 0;
                int Height= Integer.parseInt(Height_Info.getText().toString());
                int Weight= Integer.parseInt(Weight_info.getText().toString());
                int Age= Integer.parseInt(Age_info.getText().toString());
                String Sex= rb.getText().toString();

                String Insert_data = "키 : " + Height + " 몸무게 :" + Weight + " 나이: " + Age + " 성별 :" + Sex;

                Toast.makeText(getApplicationContext(), Insert_data, Toast.LENGTH_LONG).show();


                //EditText 공백 체크
                if((TextUtils.isEmpty(Height_Info.getText().toString()))
                        ||(TextUtils.isEmpty(Weight_info.getText().toString()))
                        ||(TextUtils.isEmpty(Age_info.getText().toString())))
                {
                    TextView text=(TextView) layout.findViewById(R.id.text);
                    text.setText("Blank is not allowed.");

                    Toast toast=new Toast(getApplicationContext());

                    toast.setGravity(Gravity.CENTER,0,500);
                    toast.setDuration(Toast.LENGTH_SHORT);

                    toast.setView(layout);
                    toast.show();
                    //Toast.makeText(InputActivity.this,"Blank is not allowed.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dbOpenHelper.Update_profile(name, Height, Weight, Age, Sex);
                    Toast.makeText(getApplicationContext(), "수정성공", Toast.LENGTH_LONG).show();
                    /*
                    Intent intent = new Intent(UpdateActivity.this, StepActivity.class);
                    startActivity(intent);
                    finish(); */
                }
            }
        });

    }
    public void showDatabase(String sort){
        dbOpenHelper.open();
        Cursor iCursor = dbOpenHelper.sortColumn_profile(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());

        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("userid"));
            String tempId = iCursor.getString(iCursor.getColumnIndex("userid"));
            String tempHeight = iCursor.getString(iCursor.getColumnIndex("height"));
            String tempWeight = iCursor.getString(iCursor.getColumnIndex("weight"));
            String tempAge = iCursor.getString(iCursor.getColumnIndex("age"));
            String tempSex = iCursor.getString(iCursor.getColumnIndex("sex"));
                //입력했던 개인정보 출력
            String Result = "<Recent data>\nNAME : "+tempId + "\nHEIGHT : " + tempHeight+"\nWEIGHT : "+tempWeight+"\nAGE : "+tempAge+"\nGENDER : "+tempSex;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }
}
