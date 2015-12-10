package com.company;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Stationær on 10-12-2015.
 */
public class Socket_Client extends Thread {
    private Thread ClientThread;
    private BlockingQueue<System_Global.SYS_COMMANDS> Queue;

    Socket_Client() {
    }

    public void Initalice() {

        if (Queue == null) {
            Queue = new ArrayBlockingQueue<System_Global.SYS_COMMANDS>(999);
        }
        if (ClientThread == null) {
            ClientThread = new Thread(this);
            ClientThread.start();
        }
    }

    @Override
    public void run() {
        Process();
    }

    //Worker code here
    public void Process() {
        while (true) {
            try {
                if (ClientThread.isInterrupted()) {
                    throw new InterruptedException();
                }

                if(!Queue.isEmpty()) {
                    System_Global.SYS_COMMANDS command = Queue.poll();
                    try {
                        ProcessCommand(command);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    //To close the server thread
    public void Dispose() {
        while (ClientThread.isAlive()) {
            ClientThread.interrupt();
        }
    }

    public synchronized void Do_Command(System_Global.SYS_COMMANDS command) throws Exception {
        Queue.put(command);
    }

    /*
    /Command info array
    /ip is number 0
    /port is number 1
    /nickname is number 2
    /chatroom is number 3
    */
    private void ProcessCommand(System_Global.SYS_COMMANDS command) throws Exception {
        String ip = (command.Info()[0] != null) ? (String) command.Info()[0] : "localhost";
        int port = (command.Info()[1] != null) ? (int) command.Info()[1] : 8080;
        System.out.print(ip +":" + port);
        Socket sendSocket = new Socket(ip, port);
        DataOutputStream outputStream = new DataOutputStream(sendSocket.getOutputStream());
        byte[] message = new byte[1027];
        String Data;
        byte[] inputBuffer;
        int bytesRead = 0;

        switch (command) {
            case SEND_MESSAGE:
                Data = (command.Info()[2] != null) ? ((String) command.Info()[3]+"my$@?+ts"+(String) command.Info()[2]) : "";
                for (int i = 0; i < Data.length() && i < 1027; i++)
                {
                    message[i] = Data.getBytes()[i];
                }
                outputStream.write(message);
                break;

            case CONNECT_TO_SERVER:
                Data = (command.Info()[2] != null) ? ((String) command.Info()[3]+"25qw#*x98"+(String) command.Info()[2]) : "";
                for (int i = 0; i < Data.length() && i < 1027; i++)
                {
                    message[i] = Data.getBytes()[i];
                }
                outputStream.write(message);
                /*DataInputStream inputStream = new DataInputStream(sendSocket.getInputStream());
                inputBuffer = new byte[1027];
                boolean end = false;
                while(!end)
                {
                    bytesRead = inputStream.read(inputBuffer);
                    if (bytesRead == 1027)
                    {
                        end = true;
                    }
                }
                String data = new String(inputBuffer);*/
                break;
            case DISCONNECT_FROM_SERVER:
                Data = (String) command.Info()[3]+"£$@LEAVING${$ff£€$";
                for (int i = 0; i < Data.length() && i < 1027; i++)
                {
                    message[i] = Data.getBytes()[i];
                }
                outputStream.write(message);
                Dispose();
                break;

        }

    }
}