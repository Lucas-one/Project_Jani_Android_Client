package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.websocketclient.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    //표시할 데이터
    String[] myDataset = {"안녕", "오늘", "뭐했어","영화볼래?"};

    EditText etText;
    Button btnSend;
    String id;
    //FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /*
        //database = FireDatabase.getInstance();
        //FirebaseUser user = FirebaseAut.getInstance().getCurrentuser();   //db user = ###아이디 가져오기       id는 id 변수
        if(user != null){
        id = user.id();
        }
        */
        etText = (EditText) findViewById(R.id.etText);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stText = etText.getText().toString();
                if(stText.equals("") || stText.isEmpty() ){//채팅을 입력하지 않았을 때
                    Toast.makeText(ChatActivity.this,"내용을 입력해 주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChatActivity.this,id + "," + stText,Toast.LENGTH_SHORT).show();

                    //시간 가져오기
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//밑에 코드와 결합하여 날짜와 함께 내용 올라간다.
                    String formattedDate = df.format(c);


                    //5강 17분 전 후 yj
                    /*
                    //채팅 내용 입력

                    ##databasereference myRef = database.getreference("chats"); //chats폴더 에 저장

                   Hashtable<String, String> chat = new Hashtable<String, String>();
                   chat.put("id", id);
                   numbers.put("text", stText);
                   myRef.setValue(stText);

                   */
                }



            }
        });


        Button btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);//리사이클러 뷰 자동계산
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(myDataset);// 채팅방에 표시될 데이터 연결
        mRecyclerView.setAdapter(mAdapter);

        DatabaseReference myRef = database.getReference("chats");
        //6강 참고 9분
        myRef.addChildEventListener(new ChildEventListener(){

        })




    }
}
