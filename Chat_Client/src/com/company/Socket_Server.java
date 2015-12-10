package com.company;

/**
 * Created by Stationær on 10-12-2015.
 */
public class Socket_Server extends Thread {
    private Thread ServerThread;

    Socket_Server(){}

    public void Initalice() {
        if(ServerThread == null) {
            ServerThread = new Thread(this);
            ServerThread.start();
        }
    }

    @Override
    public void run() {
        Process();
    }

    //Worker code here
    public void Process() {
        while(true) {
            try {
                if(ServerThread.isInterrupted()){
                    throw new InterruptedException();
                }
            }   catch(InterruptedException e) {
                break;
            }
        }
    }

    //To close the server thread
    public void Dispose() {
        while(ServerThread.isAlive()) {
            ServerThread.interrupt();
        }
    }
}
