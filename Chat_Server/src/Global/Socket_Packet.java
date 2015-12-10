package Global;

import java.util.Arrays;

/**
 * Created by DBJ on 10-12-2015.
 */
public class Socket_Packet {
    private byte[] Data_Packet;

    public Socket_Packet(){
        Data_Packet = new byte[System_Global.MAX_MESSAGE_LEN];
        Arrays.fill(Data_Packet, (byte) 0 );
    }

    public boolean Set_Data(byte[] Data){
        int i = 0;
        for (; i < Data.length && i < System_Global.MAX_MESSAGE_LEN; i++)
        {
            Data_Packet[i] = Data[i];
        }
        if (i +1 == Data.length) return true;
        else return false;
    }

    public byte[] Get_Data(){
        return Data_Packet;
    }

    public String Get_ChatRoom(){
        String Room = new String(Data_Packet).split("25qw#x98")[0];
        String CleanName = "";
        for (char c: Room.toCharArray()) {
            if (c != ((char)((byte)0))){
                CleanName += c;
            }
        }
        return CleanName;
    }

    public String Get_NickName(){
        String Nick = new String(Data_Packet).split("25qw#x98")[1];
        String CleanNick = "";
        for (char c: Nick.toCharArray()) {
            if (c != ((char)((byte)0))){
                CleanNick += c;
            }
        }
        return CleanNick;
    }

    public boolean IsEmpty(){
        if (new String(Data_Packet).equals("")){
            return true;
        }
        return false;
    }

    public boolean IsConnect_Message(){
        if (new String(Data_Packet).contains("25qw#x98")){
            return true;
        }
        return false;
    }
}
