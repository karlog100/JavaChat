import Global.Socket_Packet;
import Global.System_Global;
import Global.User;

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
    private Main_Controler Controler;
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

    public byte[] MessageHandler(String ClientAddress, byte[] Message){
        Socket_Packet Message_Packet = new Socket_Packet();
        Message_Packet.Set_Data(Message);

        if (Message_Packet.IsConnect_Message()){
            String ChatRoom = Message_Packet.Get_ChatRoom();

            String NickName = Message_Packet.Get_NickName();
            User new_user = new User(ClientAddress,NickName);

            System_Global.SYS_COMMANDS CMD = System_Global.SYS_COMMANDS.USER_JOIN_ROOM;
            CMD.Info(new Object[]{ChatRoom,new_user});
            try{
                Controler.System_DoCommand(CMD);
            }
            catch (InterruptedException e){}
        }
        else {
            if (Message_Packet.Get_Data().equals("£$@LEAVING${$ff£€$")){
                System_Global.SYS_COMMANDS CMD = System_Global.SYS_COMMANDS.USER_LEAVE;
                CMD.Info(new Object[]{ClientAddress, Message_Packet});
                try{
                    Controler.System_DoCommand(CMD);
                }
                catch (InterruptedException e){}
            }
            else {
                System_Global.SYS_COMMANDS CMD = System_Global.SYS_COMMANDS.USER_MESSAGE;
                CMD.Info(new Object[]{ClientAddress, Message_Packet});
                try{
                    Controler.System_DoCommand(CMD);
                }
                catch (InterruptedException e){}
            }
        }

        return "".getBytes();
    }

    /*-------------------------------
    *   System Functions
    *-------------------------------*/
    SocketHandler(Main_Controler Main, Socket socket) {
        Controler = Main;
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
            System.out.println(cliAddr);
            try {
                //Get Client Message
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

                //Create Response Packet
                Socket_Packet Packet = new Socket_Packet();
                //Handle Client Message and Set Response
                Packet.Set_Data(MessageHandler(cliAddr,buffer));
                if (!Packet.IsEmpty()){
                    //Send reply message
                    outputStream = new DataOutputStream(ThreadSocket.getOutputStream());
                    outputStream.write(Packet.Get_Data());
                }
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
