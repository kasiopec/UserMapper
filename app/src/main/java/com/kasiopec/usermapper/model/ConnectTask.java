package com.kasiopec.usermapper.model;

import android.os.AsyncTask;
import android.util.Log;

import com.kasiopec.usermapper.Client;

/**
 * Asynchronous task which establishes network connection with the server
 * **/
public class ConnectTask extends AsyncTask<String, String, Client> {

    private UserModel userModel;

    ConnectTask(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    protected Client doInBackground(String... message) {
        //we create a TCPClient object and calls the onProgressUpdate
        Client tcpClient = new Client(new Client.OnMessageReceived() {
            @Override
            public void messageReceived(String message) {
                publishProgress(message);
            }
        });
        tcpClient.run();

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //response received from server
        Log.d("Server", "response " + values[0]);
        if(!userModel.isInitialized){
            userModel.initialize(values[0]);
            userModel.isInitialized = true;
        }else{
           userModel.updateData(values[0]);
        }
    }
}
