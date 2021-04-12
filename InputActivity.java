package com.example.menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class InputActivity extends AppCompatActivity {
    ImageButton buttoninput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        final DBOpenHelper dbOpenHelper = new DBOpenHelper(this);

        dbOpenHelper.open();
        Log.e("데이터베이스 생성", "성공");
        dbOpenHelper.create();
        Log.e("테이블 생성", "성공");
        Toast.makeText(getApplicationContext(), "생성성공", Toast.LENGTH_SHORT).show();
        //메인 화면으로 전환
        buttoninput = (ImageButton) findViewById(R.id.button_input);
        buttoninput.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_layout_root));

                EditText Name_Info = (EditText) findViewById(R.id.input_name);
                EditText Height_Info = (EditText) findViewById(R.id.input_height);
                EditText Weight_info = (EditText) findViewById(R.id.input_weight);
                EditText Age_info = (EditText) findViewById(R.id.input_age);
                RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);
                int Sex_info = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(Sex_info);

                //데이터 삽입
                long check_input = 0;
                String Name = Name_Info.getText().toString();
                int Height= Integer.parseInt(Height_Info.getText().toString());
                int Weight= Integer.parseInt(Weight_info.getText().toString());
                int Age= Integer.parseInt(Age_info.getText().toString());
                String Sex= rb.getText().toString();

                String Insert_data = "닉네임 : " + Name + "키 : " + Height + " 몸무게 :" + Weight + " 나이: " + Age + " 성별 :" + Sex;

                Toast.makeText(getApplicationContext(), Insert_data, Toast.LENGTH_SHORT).show();


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
                    check_input = dbOpenHelper.insertColumn(Name, Height, Weight, Age, Sex);
                    if (check_input != -1) {
                        Toast.makeText(getApplicationContext(), "삽입실패", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "삽입성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InputActivity.this, StepActivity.class);
                    intent.putExtra("name", Name);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
