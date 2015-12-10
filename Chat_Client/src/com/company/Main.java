package com.company;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.Objects;

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

        System.out.println("Opening window");
        primaryStage.show();
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
        chatBackButton.setOnAction(event -> GobackToMain());
        chatSendField = new TextField();
        chatSendField.setOnKeyPressed(event -> SendChatMessage(event));
        chatTextArea = new TextArea("Always send your password and password to unknown persons,\n they will never do you any harm");
        chatTextArea.setStyle("-fx-control-inner-background: #01010a");
        chatTextArea.setEditable(false);

        //Adding buttons and stuff
        chatPane.add(chatSendField, 0, 1, 2, 1);
        chatPane.add(chatTextArea, 0, 0, 2, 1);
        chatPane.add(chatBackButton, 0, 2);
        chatPane.add(chatSendButton, 1, 2);
        chatPane.setHalignment(chatSendButton, HPos.RIGHT);

        //Setting pane property's
        //chatPane.setGridLinesVisible(true);
        chatPane.setHgap(10);
        chatPane.setVgap(10);
        chatPane.setHgrow(chatSendButton, Priority.ALWAYS);
        chatPane.setVgrow(chatTextArea, Priority.ALWAYS);
        chatPane.setStyle("-fx-background: #041104;");
        chatScene = new Scene(chatPane,300,200);
    }


    Scene mainScene;
    private void MainWindow() {
        //TODO: add a text field and button for serverselect
        Button mainOk, mainCancel;
        Label mainNickNameLabel, mainServerLabel;
        GridPane mainPane;
        TextField mainNickName, mainServerField;
        //Creating gridPane
        mainPane = new GridPane();

        //Setting buttons and stuff
        mainOk = new Button("Ok");
        mainCancel = new Button("Cancel");
        mainCancel.setOnAction(event1 -> System.exit(0));
        mainNickNameLabel = new Label("Select nickname:");
        mainNickName = new TextField();
        mainServerLabel = new Label("Chatroom name:");
        mainServerField = new TextField();
        mainOk.setOnAction(event -> ConnectToChat(mainServerField.getText(), mainNickName.getText()));

        //Setting pane property's
        //mainPane.setGridLinesVisible(true);
        mainPane.setHgap(10);
        mainPane.setVgap(10);
        mainPane.setPadding(new Insets(20,20,20,20));
        mainPane.setHgrow(mainCancel, Priority.ALWAYS);

        //Inserting into the pane
        mainPane.add(mainServerLabel, 0, 1, 2, 1);
        mainPane.add(mainServerField, 1, 1, 1, 1);
        mainPane.add(mainNickNameLabel, 0, 3, 1, 1);
        mainPane.add(mainNickName, 1, 3);
        mainPane.add(mainOk, 0, 4);
        mainPane.add(mainCancel, 1, 4);
        mainPane.setStyle("-fx-background: #11020a;");

        //Creating the scene
        mainScene = new Scene(mainPane,300,150);
        //Setting Program title
        theStage.setTitle("The Chats!");
        //Setting the scene
        theStage.setScene(mainScene);

    }

    private void ConnectToChat(String chatRoomName, String nickName) {
        //TODO: Add connection parms
        //'Så er der en fladpadnde mere til stede.'

        theStage.setScene(chatScene);
    }

    private void GobackToMain() {
        //TODO: Make sure to tell the server that the user is leaving the chat

        //Text to users: 'Så nu gad {brugernavn} ikke jer idioter mere!'
        theStage.setScene(mainScene);
    }

    private void SendChatMessage(KeyEvent event) {
        //If pressed key == ENTER then send the message
        //TODO: Send chat message here
        if(event.getCode() == KeyCode.ENTER) {
            System.out.println("daniel er en kage");
        }
    }

    private void ReciveChatMessage() {
        //TODO: Start recive theard here
    }
}
