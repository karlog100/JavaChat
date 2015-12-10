package Global;

/**
 * Created by DBJ on 10-12-2015.
 */
public class User {
    private String IpAddress;
    private String NickName;
    private String Chat_Room;

    public User(String Ip, String Name){
        this.IpAddress = Ip;
        this.NickName = Name;
    }

    public String Get_IpAddress(){return IpAddress;}
    public void Set_IpAddress(String Ip){this.IpAddress = Ip;}

    public String Get_NickName(){return NickName;}
    public void Set_NickName(String Name){this.NickName = Name;}

    public String Get_ChatRoom(){return Chat_Room;}
    public void Set_ChatRoom(String Room){this.Chat_Room = Room;}
}
