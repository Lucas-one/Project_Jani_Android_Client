package com.example.websocketclient.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.websocketclient.database.entity.ChatRoomModel;
import com.example.websocketclient.database.entity.MessageModel;
import com.example.websocketclient.models.ChatModel;
import com.example.websocketclient.models.ModelRepository;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ChatRoomViewModel extends AndroidViewModel {
    private final String TAG = "ChatRoomViewModelLog";
    private Context context;
    private ModelRepository modelRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable disposable;
    private Gson gson = new Gson();

    public ObservableField<String> messageEdit = new ObservableField<>();

    private MutableLiveData<Integer> messageEvent;

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        modelRepository = ModelRepository.getInstance();
        modelRepository.setReferences(context);
        subscribeToQueueChannel();

        Log.d("QueueChannelCheck", "chat room list num = " + modelRepository.getChatModels().size());
    }

    public void subscribeToQueueChannel() {
        compositeDisposable.add(modelRepository.stompGetTopicMessage("/queue/" + modelRepository.getUserRegisterModel().getRegUserName())
                .subscribe(topicMessage -> {
                    Log.d("QueueChannelCheck", "In ChatRoomViewModel");
                    messageEvent.setValue(0);
                }));
    }

    public LiveData<Integer> getMessageEvent() {
        return this.messageEvent = new MutableLiveData<>();
    }

    public void sendButtonClicked() {
        ChatModel selectedChatModel = modelRepository.getSelectedChatModel();
        String userName = modelRepository.getUserRegisterModel().getRegUserName();

        MessageModel mMessageModel = new MessageModel(
                0,
                selectedChatModel.getParticipantModels().size(),
                selectedChatModel.getChatRoomModel().getChatChannel().substring(7),
                "/queue/" + userName,
                userName,
                getDateFormatString(),
                messageEdit.get()
                );

        compositeDisposable.add(modelRepository.stompSendMessage("/app/" + selectedChatModel.getChatRoomModel().getChatChannel(), gson.toJson(mMessageModel))
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully and content : " + mMessageModel.getMsgContent());

                    mMessageModel.setChatChannel(selectedChatModel.getChatRoomModel().getChatChannel());
                    mMessageModel.setMsgCount(selectedChatModel.getParticipantModels().size() - 1);
                    mMessageModel.setMsgOwner(userName);

                    selectedChatModel.getMessageModels().add(mMessageModel);
                    insertClientDBMessageModel(mMessageModel);

                    messageEdit.set("");
                    messageEvent.setValue(0);
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));
    }

    public void insertClientDBMessageModel(MessageModel messageModel) {
        modelRepository.insertClientDBMessageModel(messageModel)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public ModelRepository getModelRepository() {
        return this.modelRepository;
    }

    /*public void sendButtonClicked() {
        MessageModel sMessageModel = new MessageModel();

        ChatRoomModel selectedChatRoomModel = modelRepository.getSelectedChatRoomModel();

        sMessageModel.setSenderName(modelRepository.getCurUserInformation().getUserName());
        sMessageModel.setSenderChatChannel("/queue/" + modelRepository.getCurUserInformation().getUserName());
        sMessageModel.setContents(messageEdit.get());
        sMessageModel.setSenderSideDate(getDateFormatString());

        compositeDisposable.add(modelRepository.stompSendMessage("/app" + selectedChatRoomModel.getSenderChatChannel(), gson.toJson(sMessageModel))
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully and content : " + sMessageModel.getContents());
                    selectedChatRoomModel.getMessageModels().add(sMessageModel);
                    messageEdit.set("");
                    messageEvent.setValue(0);
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                }));
    }*/

    public String getDateFormatString() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String log = simpleDateFormat.format(date);
        Log.i("DateLogCheck", log);
        return log;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
