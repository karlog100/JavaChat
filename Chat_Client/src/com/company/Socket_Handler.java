package com.company;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Stationær on 10-12-2015.
 */
public class Socket_Handler extends Thread {
    private Thread HandlerThread;
    private Socket HandlerSocket;
    private Main main;

    Socket_Handler(Main main,Socket HandlerSocket){
        this.main = main;
        this.HandlerSocket = HandlerSocket;
    }

    public void Initialize() {
        if(HandlerThread == null) {
            HandlerThread = new Thread(this);
            HandlerThread.start();
        }
    }

    @Override
    public void run() {
        DataInputStream inputStream = null;
        try {

            inputStream = new DataInputStream(HandlerSocket.getInputStream());
            String cliAddr = HandlerSocket.getInetAddress().getHostAddress();
            System.out.println(cliAddr);
            try {
                //Get Client Message
                byte[] buffer = new byte[1027];
                int bytesRead = 0;
                boolean end = false;
                while(!end)
                {
                    bytesRead = inputStream.read(buffer);
                    if (bytesRead == 1027)
                    {
                        end = true;
                    }
                }
                String Message = new String(buffer);
                main.ReceivedChatMessage(Message);

            }
            catch (IOException e) {
            }

        } catch(UnknownHostException e) {

        }catch(IOException e){

        }finally {
            if(HandlerSocket != null) {
                try {
                    HandlerSocket.close();
                } catch (IOException e) {

                }
            }
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
        }
        Process();
    }

    //Worker code here
    public void Process() {
        while(true) {
            try {
                if(HandlerThread.isInterrupted()){
                    throw new InterruptedException();
                }
            }   catch(InterruptedException e) {
                break;
            }
        }
    }

    //To close the server thread
    public void Dispose() {
        while(HandlerThread.isAlive()) {
            HandlerThread.interrupt();
        }
    }
}
