package com.familymap.family_map.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.familymap.family_map.model.Person;
import com.familymap.family_map.net.ServerProxy;

import RequestResult.RegisterRequest;

public class RegisterAsyncTask implements Runnable {
    private static final String USER_RESULT = "UserResult";
    private final Handler messageHandler;
    private RegisterRequest r;

    public RegisterAsyncTask(Handler messageHandler, RegisterRequest rR, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        ServerProxy.serverHost = serverHost;
        ServerProxy.serverPort = serverPort;
        this.r = rR;
    }

    @Override
    public void run() {
        ServerProxy prox = new ServerProxy();
        Person user = prox.register(r);

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
