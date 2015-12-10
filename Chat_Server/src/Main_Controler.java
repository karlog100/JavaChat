import Global.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by DBJ on 08-12-2015.
 */
public class Main_Controler extends Thread implements IBackThread {
    /*-------------------------------
    *   Variables
    *-------------------------------*/
    //<editor-fold desc="Thread Properties">
    private Thread Main_Controler_Thread;

    private volatile boolean IsProcessing = false;
    public boolean IsProcessing(){ return IsProcessing; };
    private volatile Boolean IsDisposed = false;
    public boolean IsDisposed(){ return IsDisposed; };
    // </editor-fold>

    private volatile System_Global.SYS_STATES State;
    public System_Global.SYS_STATES System_State(){ return State; }

    private BlockingQueue<System_Global.SYS_COMMANDS> System_Commands;
    private int MailBuffer = 10;
    public int System_MailCount(){ return System_Commands.size(); }

    //<editor-fold desc="Server Properties">
    private SocketServer Socket_server;
    private List<Chat_Room> Chat_Rooms;
    //</editor-fold>
    /*-------------------------------
    *   Handler Functions
    *-------------------------------*/
    public synchronized void System_DoCommand(System_Global.SYS_COMMANDS Command) throws InterruptedException{
        System_Commands.put(Command);
    }

    private void Process_Command(System_Global.SYS_COMMANDS Command) throws Exception{
        switch (Command){
            case USER_JOIN_ROOM:
                boolean done = false;
                for (int i = 0; i < Chat_Rooms.size(); i++)
                {
                    if (Chat_Rooms.get(i).Get_RoomName().equals((String)Command.Info()[0])){
                        Chat_Rooms.get(i).Add_User((User)Command.Info()[1]);
                        done = true;
                        break;
                    }
                }
                if(!done)
                {
                    Chat_Room new_room = new Chat_Room((String)Command.Info()[0]);
                    new_room.Add_User((User)Command.Info()[1]);
                    Chat_Rooms.add(new_room);
                }
                break;
            case USER_MESSAGE:
                String Message = new String(((Socket_Packet)Command.Info()[1]).Get_Data());
                for (int i = 0; i < Chat_Rooms.size(); i++)
                {
                    String Room = Chat_Rooms.get(i).Get_RoomName();
                    String Message_Room = Message.split("my3324ts")[0];
                    if (Room.equals(Message_Room)){
                        String User = "";
                        if (Message.startsWith("@")){
                            User = Message.substring(1, Message.split(" ")[0].length()-1);
                            Message = Message.substring(1 + User.length(), Message.length() - (1+User.length()));
                        }
                        User[] Users = Chat_Rooms.get(i).Get_Users();
                        for (int x = 0; x < Users.length; x++)
                        {
                            if (User.equals("") || Users[x].Get_NickName().equals(User) || Users[x].Get_IpAddress().equals((String)Command.Info()[0])){
                                SocketSender Sender = new SocketSender(Users[x].Get_IpAddress(),System_Global.SOC_PORT_OUT,Users[x].Get_NickName()+ ": "+Message);
                                Sender.Initialize();
                            }
                        }
                        break;
                    }
                }
                break;
            case USER_LEAVE:
                String LeaveMessage = new String(((Socket_Packet)Command.Info()[1]).Get_Data());
                for (int i = 0; i < Chat_Rooms.size(); i++)
                {
                    if (Chat_Rooms.get(i).Get_RoomName().equals(LeaveMessage.split("my3324ts")[0])){
                        User[] Users = Chat_Rooms.get(i).Get_Users();
                        for (int x = 0; x < Users.length; x++)
                        {
                            if (Users[x].Get_IpAddress().equals((String)Command.Info()[0])){
                                Chat_Rooms.get(i).Remove_User(Users[x]);
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            case SYS_SHUTDOWN:
                //TODO close window
                break;
            case NOF_COMMANDS:
            case UNKNOWN:
            default:
                //TODO Display Message
                break;
        }
    }

    /*-------------------------------
    *   System Functions
    *-------------------------------*/
    public Main_Controler(){
    }

    public void Initialize(){
        System.out.println("Init MainControler");
        State = System_Global.SYS_STATES.WORKING;
        State.Info(String.format("Initializing"));

        if (Chat_Rooms == null){
            Chat_Rooms = new ArrayList<>();
        }
        //Init Main Items .. eg Command Queue
        if (System_Commands == null){
            System_Commands = new ArrayBlockingQueue<System_Global.SYS_COMMANDS>(MailBuffer);
        }

        if (Main_Controler_Thread == null) {
            Main_Controler_Thread = new Thread(this);
            Main_Controler_Thread.start();
        }

        if (Socket_server == null){
            Socket_server = new SocketServer(this,System_Global.SOC_PORT_IN);
            Socket_server.Initialize();
        }
    }

    public void run() {
        try{
            Process();
        }
        catch (InterruptedException e){

        }
    }

    public void Process() throws InterruptedException {
        IsProcessing = true;
        System.out.println("MainControler Started");
        while (IsProcessing()){
            if (Main_Controler_Thread.isInterrupted() || !IsProcessing()){
                throw new InterruptedException();
            }
            State = System_Global.SYS_STATES.NON;
            State.Info("Waiting for Command");
            if (!System_Commands.isEmpty()){
                State = System_Global.SYS_STATES.WORKING;
                System_Global.SYS_COMMANDS Command = System_Commands.poll();
                State.Info(String.format("Processing Command: %s", System_Global.SYS_COMMANDS.values()[Command.value()+1]));
                try{
                    Process_Command(Command);
                }
                catch (Exception e){
                    //System_DoCommand(Command);
                }
            }
        }
    }

    public void Dispose(){
        System.out.println("Disposing MainControler");
        State = System_Global.SYS_STATES.SHUTDOWN;
        while (Main_Controler_Thread.isAlive()) {
            if (IsProcessing()){
                Main_Controler_Thread.interrupt();
            }
        }
        IsDisposed = true;
        System.out.println("MainControler Disposed");

        if (Socket_server != null){
            Socket_server.Dispose();
        }
    }
}
