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

public class Main extends Application {
    private Stage theStage;
    private int serverPort;
    private Socket_Client sendConnection = new Socket_Client();
    private String nickName, chatRoomName, serverIp;
    private TextArea chatTextArea;
    private Socket_Server Listener;

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

        Listener = new Socket_Server(this);
    }

    Scene chatScene;
    private void ChatWindow() {
        GridPane chatPane;
        Button chatBackButton, chatSendButton;
        TextField chatSendField;

        //Creating gridPane
        chatPane = new GridPane();

        //Setting buttons
        chatBackButton = new Button("Go Back");
        chatBackButton.snappedLeftInset();
        chatBackButton.setOnAction(event -> GobackToMain());

        chatSendField = new TextField();
        chatSendField.setOnKeyPressed(event -> {SendChatMessage(event, chatSendField.getText(), chatSendField); });

        chatSendButton = new Button("Send!");
        chatSendButton.snappedRightInset();
        chatSendButton.setOnAction(event -> {SendChatMessage(event, chatSendField.getText(), chatSendField);});

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
        mainOk.setOnAction(event -> {nickName = mainNickName.getText(); chatRoomName = mainServerField.getText(); ConnectToChat();});

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

    private void ConnectToChat() {
        boolean isServerSet = (chatRoomName.contains("/") && chatRoomName.contains(":"));
        //TODO: If this chat ever goes live then edit localhost to the default server ip
        serverIp = (isServerSet) ? chatRoomName.split(":")[0] : "localhost";
        String _serverPort = chatRoomName.split(":")[1];
        _serverPort = _serverPort.split("/")[0];
        serverPort = (isServerSet) ? Integer.parseInt(_serverPort): 50000;
        chatRoomName = (isServerSet) ? chatRoomName.split("/")[1]: chatRoomName;
        theStage.setScene(chatScene);
        sendConnection.Initalice();
        System_Global.SYS_COMMANDS command = System_Global.SYS_COMMANDS.CONNECT_TO_SERVER;
        command.Info(new Object[]{serverIp, serverPort, nickName, chatRoomName});

        System.out.println("Selected chartroom name: " + chatRoomName);

        System.out.println("Selected server: " + serverIp);

        System.out.println("Selected port: " + serverPort);
        try {
            sendConnection.Do_Command(command);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Listener.Initialize();
    }

    private void DisconnectFromChat() {
        //Text to users: 'Så nu gad {brugernavn} ikke jer idioter mere!'
        System_Global.SYS_COMMANDS messagePack = System_Global.SYS_COMMANDS.DISCONNECT_FROM_SERVER;
        messagePack.Info(new Object[]{serverIp,serverPort,"",chatRoomName});
        Listener.Dispose();
    }

    private void GobackToMain() {
        DisconnectFromChat();
        theStage.setScene(mainScene);
    }

    private void SendChatMessage(Event event, String message, TextField sendText) {
        //If Sendbutton clicked OR pressed key == ENTER then send the message
        //TODO: Send chat message here
        if( ("ACTION".equals((event.getEventType()).toString())) || (("KEY_PRESSED".equals((event.getEventType()).toString())) && ((KeyEvent)event).getCode() == KeyCode.ENTER) ) {
            sendText.clear();
            System_Global.SYS_COMMANDS messagePack = System_Global.SYS_COMMANDS.SEND_MESSAGE;
            //TODO: make ip stuff
            messagePack.Info(new Object[]{serverIp, serverPort, message, chatRoomName});
            try {
                sendConnection.Do_Command(messagePack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ReceivedChatMessage(String message) {
        chatTextArea.appendText(" \n" + message);
    }
}
