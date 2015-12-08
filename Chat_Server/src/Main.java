import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Java Socket Chat server");

        BorderPane layout = new BorderPane();

        //TODO Create User interface
        //layout.setTop(menubar.Initialize(this));
        //layout.setCenter(textbox.Initialize());
        //layout.setBottom(infobar.Initialize());

        Scene scene = new Scene(layout,400,300);

        window.setScene(scene);
        window.show();
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
