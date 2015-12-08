import Global.System_Global;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by DBJ on 08-12-2015.
 */
public class SocketHandler extends Thread {
    /*-------------------------------
    *   Variables
    *-------------------------------*/
    private Thread SocketHandler_Thread;
    private Socket ThreadSocket;

    /*-------------------------------
    *   Handler Functions
    *-------------------------------*/
    /*final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }*/

    public byte[] MessageHandler(byte[] Message){


        return "Test".getBytes();
    }

    /*-------------------------------
    *   System Functions
    *-------------------------------*/
    SocketHandler(Socket socket) {
        ThreadSocket = socket;
    }

    public void Initialize () {
        if (SocketHandler_Thread == null) {
            SocketHandler_Thread = new Thread(this);
            SocketHandler_Thread.setDaemon(true);
            SocketHandler_Thread.start();
        }
    }

    public void run() {
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(ThreadSocket.getInputStream());
            String cliAddr = ThreadSocket.getInetAddress().getHostAddress();

            try {
                byte[] buffer = new byte[System_Global.MAX_MESSAGE_LEN];
                int bytesRead = 0;
                boolean end = false;
                while(!end)
                {
                    bytesRead = inputStream.read(buffer);
                    if (bytesRead == System_Global.MAX_MESSAGE_LEN)
                    {
                        end = true;
                    }
                }

                //Send reply message
                byte[] SendingData = new byte[System_Global.MAX_MESSAGE_LEN];
                Arrays.fill(SendingData, (byte) 0 );
                byte[] data = MessageHandler(buffer);
                for (int i = 0; i < data.length; i++)
                {
                    SendingData[i] = data[i];
                }
                outputStream = new DataOutputStream(ThreadSocket.getOutputStream());
                outputStream.write(SendingData);
            }
            catch (IOException e) {
            }
        }
        catch (UnknownHostException e) {
        }
        catch (IOException e) {
        }
        finally {
            if (ThreadSocket != null){
                try {
                    ThreadSocket.close();
                }
                catch (IOException e) {
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                }
                catch (IOException e) {
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                }
            }
        }
    }
}
