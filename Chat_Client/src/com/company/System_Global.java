package com.company;

/**
 * Created by Stationær on 10-12-2015.
 */
public class System_Global {
    public enum SYS_COMMANDS{
        SEND_MESSAGE(1,""),
        CONNECT_TO_SERVER(2,""),
        DISCONNECT_FROM_SERVER(3,"");

        private final int Value;
        private Object args[];

        SYS_COMMANDS(int value) {this.Value = value;}
        SYS_COMMANDS(int value, Object arg) {this.Value = value; this.args = new Object[]{arg};}

        public void Info(Object[] args){
            this.args = args;
        }
        public void Info(Object arg){
            this.args = new Object[]{arg};
        }

        public int getValue() {
            return Value;
        }

        public Object[] Info(){
            return args;
        }
    }
}
