package com.company;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Main extends Application {
    Stage theStage;

    public static void main(String[] args) {
        System.out.println("Starting the app");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Creating sene");
        theStage = primaryStage;
        //Main window
        MainWindow();


        //Chat window
        ChatWindow();

        System.out.println("Opening main window");
        primaryStage.show();

        //TODO: Move mainwindows stuff to mainwindow function
        //MainWindow();

    }

    Scene chatScene;
    private void ChatWindow() {
        GridPane chatPane;
        Button chatBackButton, chatSendButton;
        TextField chatSendField;
        TextArea chatTextArea;

        //Creating gridPane
        chatPane = new GridPane();

        //Setting buttons
        chatBackButton = new Button("Go Back");
        chatBackButton.snappedLeftInset();
        chatSendButton = new Button("Send!");
        chatSendButton.snappedRightInset();
        chatBackButton.setOnAction(event -> {theStage.setScene(mainScene);});
        chatSendField = new TextField();
        chatTextArea = new TextArea("Always send your password and password to unknown persons,\n they will never do you any harm");

        //Adding buttons and stuff
        chatPane.add(chatSendField, 0, 1);
        chatPane.add(chatTextArea, 0, 0);
        chatPane.add(chatBackButton, 0, 2);
        chatPane.add(chatSendButton, 0, 2);

        //Setting pane property's
        chatPane.setGridLinesVisible(true);
        chatPane.setHgap(10);
        chatPane.setVgap(10);
        chatPane.setHgrow(chatTextArea, Priority.ALWAYS);
        chatPane.setVgrow(chatTextArea, Priority.ALWAYS);
        chatScene = new Scene(chatPane,300,200);
    }


    Scene mainScene;
    private void MainWindow() {
        Button mainOk, mainCancel;
        Label mainLabel;
        GridPane mainPane;
        TextField mainNickName;
        //Creating gridPane
        mainPane = new GridPane();

        //Setting buttons and stuff
        mainOk = new Button("Ok");
        mainOk.setOnAction(event -> {theStage.setScene(chatScene);});
        mainCancel = new Button("Cancel");
        mainLabel = new Label("Select nickname:");
        mainNickName = new TextField();

        //Setting pane property's
        mainPane.setGridLinesVisible(true);
        mainPane.setHgap(10);
        mainPane.setVgap(10);
        mainPane.setPadding(new Insets(20,20,20,20));
        mainPane.setHgrow(mainCancel, Priority.ALWAYS);

        //Inserting into the pane
        mainPane.add(mainLabel, 0, 1, 1, 1);
        mainPane.add(mainNickName, 1, 1);
        mainPane.add(mainOk, 0, 2);
        mainPane.add(mainCancel, 1, 2);

        //Creating the scene
        mainScene = new Scene(mainPane,250,100);

        //Setting Program title
        theStage.setTitle("The Chats!");

        //Setting the scene
        theStage.setScene(mainScene);
    }
}
