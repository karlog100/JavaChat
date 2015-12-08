import Global.IBackThread;
import Global.System_Global;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import static Global.System_Global.SOC_PORT;

/**
 * Created by DBJ on 08-12-2015.
 */
public class SocketSender extends Thread {
    /*-------------------------------
    *   Variables
    *-------------------------------*/
    //<editor-fold desc="Thread Properties">
    private Thread SocketSender_Thread;

    private volatile boolean IsProcessing = false;
    public boolean IsProcessing(){ return IsProcessing; };
    private volatile Boolean IsDisposed = false;
    public boolean IsDisposed(){ return IsDisposed; };
    // </editor-fold>
    //<editor-fold desc="Connection Properties">
    private Socket SocketSender;
    private String Receiver;
    private String Message;
    // </editor-fold>

    /*-------------------------------
    *   System Functions
    *-------------------------------*/
    SocketSender(String ReceiverIp, String message) {
        Receiver = ReceiverIp;
        Message = message;
    }

    public void Initialize () {
        if (SocketSender_Thread == null) {
            SocketSender_Thread = new Thread(this);
            SocketSender_Thread.setDaemon(true);
            SocketSender_Thread.start();
        }
    }

    public void run() {
        try{
            SocketSender = new Socket("localhost", SOC_PORT);
            DataOutputStream dataOutputStream = new DataOutputStream(SocketSender.getOutputStream());

            byte[] SendingData = new byte[System_Global.MAX_MESSAGE_LEN];
            Arrays.fill(SendingData, (byte) 0 );
            byte[] data = Message.getBytes();
            for (int i = 0; i < data.length; i++)
            {
                SendingData[i] = data[i];
            }
            dataOutputStream.write(SendingData);
            InputStream inputStream = SocketSender.getInputStream();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(System_Global.MAX_MESSAGE_LEN);

            byte[] buffer = new byte[System_Global.MAX_MESSAGE_LEN];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            //TODO validate response "buffer"
        }
        catch (IOException e){

        }
    }
}