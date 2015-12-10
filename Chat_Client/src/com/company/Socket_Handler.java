package com.company;

/**
 * Created by Stationær on 10-12-2015.
 */
public class Socket_Handler extends Thread {
    private Thread HandlerThread;

    Socket_Handler(){}

    public void Initalice() {
        if(HandlerThread == null) {
            HandlerThread = new Thread(this);
            HandlerThread.start();
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
                if(HandlerThread.isInterrupted()){
                    throw new InterruptedException();
                }
            }   catch(InterruptedException e) {
                break;
            }
        }
    }

    //To close the server thread
    public void Dispose() {
        while(HandlerThread.isAlive()) {
            HandlerThread.interrupt();
        }
    }
}
