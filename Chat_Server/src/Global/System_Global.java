package Global;

/**
 * Created by Lynspitti on 25-11-2015.
 */
public final class System_Global {
    public static final int MAX_MESSAGE_LEN = 1027;

//<editor-fold desc="System Properties">
    public enum SYS_STATES {
        ERROR       (-1),
        NON         (00),
        WORKING     (01),
        SHUTDOWN    (02),
        NOF_STATES  (03);

        private final int VALUE;
        private String INFO = "";

        SYS_STATES(int value) {
            this.VALUE = value;
        }

        public void Info(String info){ this.INFO = info; }

        public int value() { return VALUE; }
        public String Info(){ return INFO; }
    }

    public enum SYS_COMMANDS {
        UNKNOWN                 (-1),
        SYS_SHUTDOWN            (0),
        SYS_REBOOT              (1),
        SYS_MESSAGE             (2,""),

        USER_WHISPER            (3),
        USER_MESSAGE            (4),
        USER_CREATE_ROOM        (5),
        USER_JOIN_ROOM          (6),

        NOF_COMMANDS            (7);

        private final int VALUE;
        private Object args[];

        SYS_COMMANDS(int value) { this.VALUE = value; }
        SYS_COMMANDS(int value,Object arg) {
            this.VALUE = value;
            this.args = new Object[]{arg};
        }

        public void Info(Object args[]){ this.args = args; }
        public void Info(Object args){ this.args = new Object[] {args}; }

        public int value() { return VALUE; }
        public Object[] Info(){ return args; }
    }

//</editor-fold>
//<editor-fold desc="Connection Properties">
    public static final int SOC_PORT = 50000;
//</editor-fold>
}
