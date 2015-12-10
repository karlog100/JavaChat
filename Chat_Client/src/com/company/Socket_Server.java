package com.company;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Stationær on 10-12-2015.
 */
public class Socket_Server extends Thread {
    private Thread ServerThread;
    private ServerSocket serverSocket;
    private Main main;

    Socket_Server(Main main){
        this.main = main;
    }

    public void Initialize() {
        if(ServerThread == null) {
            ServerThread = new Thread(this);
            ServerThread.start();
        }
    }

    @Override
    public void run() {
        Process();
    }

    //Worker code here
    public void Process() {
        try {
            serverSocket = new ServerSocket(50001);
            serverSocket.setSoTimeout(1000);
            System.out.println("Socket Server Started");
            while (true) {
                try {
                    if (ServerThread.isInterrupted()) {
                        throw new InterruptedException();
                    }
                    Socket socket = serverSocket.accept();
                    System.out.println("Client Have been Accepted.");

                    //assign own Reply Thread / Handler
                    Socket_Handler Handler = new Socket_Handler(main,socket);
                    Handler.Initialize();
                } catch (SocketTimeoutException e) {

                } catch (InterruptedException e) {
                    break;
                }
            }

        } catch (Exception e) {

        }
    }

    //To close the server thread
    public void Dispose() {
        while(ServerThread.isAlive()) {
            ServerThread.interrupt();
        }
    }
}
