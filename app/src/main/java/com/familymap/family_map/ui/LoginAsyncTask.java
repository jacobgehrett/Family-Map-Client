package com.familymap.family_map.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.familymap.family_map.model.Person;
import com.familymap.family_map.net.ServerProxy;
import RequestResult.LoginRequest;

public class LoginAsyncTask implements Runnable {
    private static final String USER_RESULT = "UserResult";
    private final Handler messageHandler;
    private final LoginRequest r;

    public LoginAsyncTask(Handler messageHandler, LoginRequest lR, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        ServerProxy.serverHost = serverHost;
        ServerProxy.serverPort = serverPort;
        this.r = lR;
    }

    @Override
    public void run() {
        ServerProxy prox = new ServerProxy();
        Person user = prox.login(r);

        sendMessage(user);
    }

    private void sendMessage(Person user) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString(USER_RESULT, user.getMessage());
        message.setData(messageBundle);

        messageHandler.sendMessage(message);
    }
}
