package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.ListTemplate;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import static com.kakao.util.helper.Utility.getPackageInfo;


public class StepActivity extends AppCompatActivity {

    Button weatherbutton;

    // 만보기
    Intent manboService;
    BroadcastReceiver receiver;
    String SaveStep;

    boolean flag = true;
    String Savestep2;
    String serviceData;
    TextView countText;
    Button playingBtn1;
    Button playingBtn2;
    final DBOpenHelper dbOpenHelper = new DBOpenHelper(this);

    public String  getName(){
        dbOpenHelper.open();
        Intent intent_data  = getIntent();
        final String name = intent_data.getExtras().getString("name");
        return name;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);


        final String name = getName();
        Log.e("이름가져오기", "성공");// 입력햇던 이름 가져오기

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date();
        final String recent_time = format1.format(time);//현재 날짜 받아오기
/*

        Cursor cmp_date = dbOpenHelper.sortColumn_date(name);
        while(cmp_date.moveToNext()) {
            String tempDate = cmp_date.getString(cmp_date.getColumnIndex("date"));
            if(tempDate.equals(recent_time)) break;

            else if(!tempDate.equals(recent_time)) StepValue.Step = 0; break;
        }  //만보기 날짜 바뀌면 초기화 해보려다가 실패쓰,,,,,,8ㅁ8
*/

        //날씨화면으로 전환
        weatherbutton=(Button)findViewById(R.id.action_weather);
        weatherbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StepActivity.this,WeatherActivity.class);
                startActivity(intent);
            }
        });

        // 만보기

        manboService = new Intent(this, StepCheckService.class);

        receiver = new PlayingReceiver();

        countText = (TextView) findViewById(R.id.stepText);
        playingBtn1 = (Button) findViewById(R.id.btnSaveService);
        playingBtn2 = (Button) findViewById(R.id.btnResetService);


        playingBtn1.setOnClickListener(new View.OnClickListener() {
            int Data_insert = 0; //걸음수 가져오기
            int check = 0; //새로운 정보인지 판별(이거는 초기화 문제가 해결되면 삭제해도 됨)
            @Override
            public void onClick(View v) {

                if (flag) {
                    // TODO Auto-generated method stub
                    playingBtn1.setText("저장");

                    try {

                        IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");

                        registerReceiver(receiver, mainFilter);
                        startService(manboService);
                        SaveStep = serviceData; // StepCheckService에 있는 Step값을 따로 저장 (밑에 PlayingReceiver에 있는 부분 참고) <지워도 괜찮음. 임의로 생성함>
                        Savestep2= serviceData;

                        Data_insert = Integer.parseInt(serviceData);
                        if(check == 0){
                            dbOpenHelper.insert_StepData(name, recent_time, Data_insert);
                            Log.e("테이블2 데이터삽입", "성공"); // 저장버튼을 눌렀을 때 처음 데이터 입력시 데이터(걸음수) 삽입
                            check = 1;
                        }
                        else{
                            dbOpenHelper.Update_steps(name, recent_time, Data_insert);
                            Log.e("테이블2 데이터수정", "성공"); //원래 있던 정보가 있으면 걸음수 수정
                        }

                        String a = "이름 : "+name+" 날짜 : "+ recent_time + " 걸음수 :" + Data_insert; //수정되거나 삽입된 정보 출력
                        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        // TODO: handle exception
                        // Toast.makeText(getApplicationContext(), e.getMessage(),
                        // Toast.LENGTH_LONG).show();
                    }
                }

                flag = !flag;

                playingBtn2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (flag) {
                            // TODO Auto-generated method stub

                            unregisterReceiver(receiver);
                            stopService(manboService);

                            try {

                                IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");

                                registerReceiver(receiver, mainFilter);
                                startService(manboService);

                            } catch (Exception e) {
                                // TODO: handle exception
                                //Toast.makeText(getApplicationContext(), e.getMessage(),
                                //  Toast.LENGTH_LONG).show();
                            }
                        }

                        flag = !flag;

                    }


                });

            }
        });
    }


    //메뉴 만들기
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    //메뉴 기능 추가
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_profile:
                Intent intent_data  = getIntent();
                final String name = intent_data.getExtras().getString("name");
                Intent input=new Intent(this,UpdateActivity.class);
                input.putExtra("name", name);
                startActivity(input);
                break;
            case R.id.action_share:
               shareKakao();
               break;
            case R.id.action_record:
                Intent intent_data2  = getIntent();
                final String name2 = intent_data2.getExtras().getString("name");
                Intent input2 =new Intent(this,RecordActivity.class);
                input2.putExtra("name", name2);
                startActivity(input2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareKakao(){
        String templateId = "19671";

        Map<String, String> templateArgs = new HashMap<String, String>();
        templateArgs.put("template_arg1", "value1");

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendCustom(this, templateId, templateArgs, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
            }
        });
    }




    // 만보기
    class PlayingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("PlayignReceiver", "IN");
            serviceData = intent.getStringExtra("stepService"); // serviceData에 값이 들어가고 그것을 countText.setText로 화면에 출력
            countText.setText(serviceData);
            // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

        }
    }


}
