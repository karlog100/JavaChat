import Global.System_Global;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.Inet4Address;

public class Main extends Application {

    public Stage window;

    private String IpAddress = "0.0.0.0";
    TextArea Console;
    TextField Terminal;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Java Socket Chat server");

        BorderPane layout = new BorderPane();

        //TODO Create User interface
        Console = new TextArea();
        Console.setEditable(false);

        Terminal = new TextField();
        Terminal.setMinHeight(40);
        Terminal.setOnKeyPressed(event -> Terminal_Submit(event));

        layout.setCenter(Console);
        layout.setBottom(Terminal);

        Scene scene = new Scene(layout,400,300);

        window.setScene(scene);
        window.show();

        IpAddress = Inet4Address.getLocalHost().getHostAddress();
        Console_Write("Console HaveStarted: Ip: " + IpAddress + " Port: " + System_Global.SOC_PORT_IN);
    }

    public void Console_Write(String Text){
        Console.appendText(Text);
    }

    private void Terminal_Submit(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER)  {
            String Text = Terminal.getText();

            switch(Text.toUpperCase()){
                case "EXIT":
                    window.close();
                    break;
            }

            Terminal.setText("");
        }
    }

    private static Main_Controler main_con;
    public static void main(String[] args) {
        main_con = new Main_Controler();
        System.out.println("Call init");
        main_con.Initialize();
        System.out.println("Init done");
        launch(args);
    }

    @Override
    public void stop(){
        System.out.println("Call disposed");
        main_con.Dispose();
    }
}
