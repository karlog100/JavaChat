package Global;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DBJ on 10-12-2015.
 */
public class Chat_Room {
    private String Name;
    private List<User> Users;

    public Chat_Room(String Name){
        this.Name = Name;
        Users = new ArrayList<>();
    }

    public String Get_RoomName(){
        return this.Name;
    }

    public User[] Get_Users(){
        return Users.toArray(new User[Users.size()]);
    }

    public User Get_User(String Name){
        for (int i = 0; i < Users.size(); i++)
        {
            if (Users.get(i).Get_NickName().equals(Name)){
                return Users.get(i);
            }
        }
        return null;
    }

    public void Add_User(User user){
        while(Get_User(user.Get_NickName()) != null){
            user.Set_NickName(user.Get_NickName() + "#");
        }
        user.Set_ChatRoom(this.Name);
        Users.add(user);
    }

    public void Remove_User(User user){
        Users.remove(user);
    }
}
