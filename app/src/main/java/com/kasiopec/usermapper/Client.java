package com.kasiopec.usermapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Client{
    private static final String SERVER_IP = "ios-test.printful.lv";
    private static final int SERVER_PORT = 6111;

    private String responseMessage;
    private boolean isRunning = false;
    private PrintWriter outputBuffer;
    private BufferedReader inputBuffer;
    private OnMessageReceived mMessageListener;

    public Client(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    // Client stopper, just in case it is needed
    public void stopClient(){
        isRunning = false;
        if(outputBuffer != null){
            outputBuffer.flush();
            outputBuffer.close();
        }

        mMessageListener = null;
        inputBuffer = null;
        outputBuffer = null;
        responseMessage = null;
    }

    /**
     * Method which initializes TCP connection to the server.
     * **/
    public void run(){
        isRunning = true;
        try{
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            Socket socket = new Socket(serverAddr, SERVER_PORT);
            try{
                // Authorization message to the server
                String authServerMessage = "AUTHORIZE kasiclysm@gmail.com";

                // Sends the message to the server
                outputBuffer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                outputBuffer.println(authServerMessage);
                outputBuffer.flush();

                // Receives the message which the server sends back
                inputBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Keeping socket open for the updates
                while(isRunning){
                    responseMessage = inputBuffer.readLine();

                    if(responseMessage !=null && mMessageListener != null){
                        mMessageListener.messageReceived(responseMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //sockets needs to be recreated
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface OnMessageReceived {
        void messageReceived(String message);
    }




}


