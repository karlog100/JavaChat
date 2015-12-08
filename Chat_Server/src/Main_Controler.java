import Global.IBackThread;
import Global.System_Global;

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

    SocketServer Socket_server;

    /*-------------------------------
    *   Handler Functions
    *-------------------------------*/
    public synchronized void System_DoCommand(System_Global.SYS_COMMANDS Command) throws InterruptedException{
        System_Commands.put(Command);
    }

    public static void Process_Command(System_Global.SYS_COMMANDS Command) throws Exception{
        switch (Command){
            case SYS_SHUTDOWN:
                //TODO Close Window
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

        //Init Main Items .. eg Command Queue
        if (System_Commands == null){
            System_Commands = new ArrayBlockingQueue<System_Global.SYS_COMMANDS>(MailBuffer);
        }

        if (Main_Controler_Thread == null) {
            Main_Controler_Thread = new Thread(this);
            Main_Controler_Thread.start();
        }

        Socket_server = new SocketServer(System_Global.SOC_PORT);
        Socket_server.Initialize();
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

        Socket_server.Dispose();
    }
}
