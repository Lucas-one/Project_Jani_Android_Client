package com.example.websocketclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
<<<<<<< HEAD
=======
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
>>>>>>> 734f2d47d8bdb33547b2b0b0fb8f4369c919dc5c

import com.example.websocketclient.databinding.ActivityMainBinding;
import com.example.websocketclient.R;
import com.example.websocketclient.viewmodels.MainViewModel;
import com.example.websocketclient.views.utils.adapters.PageAdapter;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

public class MainActivity extends AppCompatActivity {
<<<<<<< HEAD
=======
////레지스터 버튼 기존 UI의 btnRegister -> register_btn 로 변경 yj
    private Button register_btn;
    private Button btnLogin;
    ProgressBar pbLogin;
    private List<String> mDataSet = new ArrayList<>();
>>>>>>> 734f2d47d8bdb33547b2b0b0fb8f4369c919dc5c

    private static final String TAG = "MainActivityLog";
    private ActivityMainBinding activityMainBinding;
    private MainViewModel mainViewModel;
    private PageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD

        dataBindingInit();
        stompLiveDataInit();

    }
=======
        setContentView(R.layout.activity_main);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + ServerModel.SERVER_IP + ":" + ServerModel.SERVER_PORT + "/janiwss/websocket");
        resetSubscriptions();



        ////////////////////////////
        //레지스터 버튼 코드 yj 시작
        register_btn = (Button)findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sendEchoViaStomp();             //이건 무엇일까 yj
                Intent in = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(in);
                //registerActivity로 넘긴후에 chatroom으로 이동하게 코드 짤 예정 yj

            }
        });
        //레지스터 버튼 끝
        ///////////////////////////

        //로그인 버튼 시작 yj
        //5강 1분 31초 참고
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbLogin.setVisibility(View.VISIBLE);// 로그인되는거 보이게

                //yj 여기에 데이터 받고 로그인 될때랑 안 될때 구현해야한다, DB에서 데이터 가져와서 일치하면 로그인 되는 코드
                //if(!task.isSuccessful()){}        //로그인 안 될때
                //else{} //로그인 될 때 pbLogin.setVisibility(View.GONE);

                Intent ChatRoom = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(ChatRoom);
            }
        });

        //로그인 버튼 끝
        ////////////////////////////////////









        //stompDisconnect();
        //toast("After STOMP Disconnection!");

    }//onCreate
>>>>>>> 734f2d47d8bdb33547b2b0b0fb8f4369c919dc5c

    public void dataBindingInit() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // set LifecycleOwner
        activityMainBinding.setMainActivity(this);
        activityMainBinding.setLifecycleOwner(this);

        // Binds MainViewModel.class
        //mainViewModel = new MainViewModel(this);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        activityMainBinding.setMainViewModel(mainViewModel);

        //mainViewModel.getModelRepository().setInitialRequestModelHashMap();

        pageAdapter = new PageAdapter(getSupportFragmentManager(), activityMainBinding.mainTabLayout.getTabCount());
        pageAdapter.setMainViewModel(mainViewModel);
        activityMainBinding.mainViewPager.setAdapter(pageAdapter);

        activityMainBinding.mainTabLayout.setupWithViewPager(activityMainBinding.mainViewPager);
    }

    public void stompLiveDataInit() {
       //mainViewModel.createChatRoom("/topic/greetings");

        // STOMP Health를 Check함.
        mainViewModel.getStompHealthEvent()
                .observe(this, new Observer<LifecycleEvent.Type>() {
                    @Override
                    public void onChanged(LifecycleEvent.Type type) {
                        switch (type) {
                            // STOMP Over WebSocket이
                            case OPENED: // 성공적으로 Opened
                                Log.i(TAG, "Stomp connection opened");
                                break;
                            case ERROR: // Open 실패
                                Log.e(TAG, "Stomp connection error");
                                break;
                            case CLOSED: // 어떤 이유에서든 Closed 됨
                                break;
                            case FAILED_SERVER_HEARTBEAT: // Heartbeat를 감지할 수 없음
                                break;
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.d("QueueChannelCheck", "MainActivity : onDestroy()");
    }
}
