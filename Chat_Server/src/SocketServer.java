import Global.IBackThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by DBJ on 08-12-2015.
 */
public class SocketServer extends Thread implements IBackThread {
    /*-------------------------------
    *   Variables
    *-------------------------------*/
    //<editor-fold desc="Thread Properties">
    private Main_Controler Controler;
    private Thread SocketServer_Thread;

    private volatile boolean IsProcessing = false;
    public boolean IsProcessing(){ return IsProcessing; };
    private volatile Boolean IsDisposed = false;
    public boolean IsDisposed(){ return IsDisposed; };
    // </editor-fold>
    //<editor-fold desc="Connection Properties">
    private int Port;
    ServerSocket serverSocket;
    // </editor-fold>

    /*-------------------------------
    *   System Functions
    *-------------------------------*/
    SocketServer(Main_Controler Main,int Port){
        Controler = Main;
        this.Port = Port;
    }

    public void Initialize(){
        System.out.println("Init SocketServer");
        IsDisposed =false;
        if (SocketServer_Thread == null) {
            SocketServer_Thread = new Thread(this);
            SocketServer_Thread.start();
        }
    }

    @Override
    public void run() {
        Process();
    }

    public void Process() {
        IsProcessing = true;
        try {
            System.out.println("SocketServer Started");
            // Bind Socket Server to port
            serverSocket  = new ServerSocket(Port);
            serverSocket.setSoTimeout(1000);

            while (IsProcessing) {
                try {
                    if (SocketServer_Thread.isInterrupted() || !IsProcessing()){
                        throw new InterruptedException();
                    }
                    Socket socket = serverSocket.accept();
                    System.out.println("Client Have been Accepted.");

                    //assign own Reply Thread / Handler
                    SocketHandler Handler = new SocketHandler(Controler,socket);
                    Handler.Initialize();
                }
                catch (SocketTimeoutException e){

                }
                catch (InterruptedException e){
                    IsProcessing = false;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Dispose(){
        System.out.println("Disposing SocketServer");
        while (SocketServer_Thread.isAlive()) {
            if (IsProcessing()){
                IsProcessing = false;
                SocketServer_Thread.interrupt();
            }
        }
        IsDisposed = true;
        System.out.println("SocketServer Disposed");
    }
}
