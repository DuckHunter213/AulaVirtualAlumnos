import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ConversationView extends VBox {
    private String conversationPartner;
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();

    private Label contactHeader;
    private ScrollPane messageScroller;
    private VBox messageContainer;
    private HBox inputContainer;

    public ConversationView(String conversationPartner){
        super(5);
        this.conversationPartner = conversationPartner;
        setupElements();
    }

    private void setupElements(){
        setupContactHeader();
        setupMessageDisplay();
        setupInputDisplay();
        getChildren().setAll(contactHeader, messageScroller, inputContainer);
        setPadding(new Insets(5));
    }

    private void setupContactHeader(){
        contactHeader = new Label(conversationPartner);
        contactHeader.setAlignment(Pos.CENTER);
        contactHeader.setFont(Font.font("Comic Sans MS", 14));
    }

    private void setupMessageDisplay(){
        messageContainer = new VBox(5);
        Bindings.bindContentBidirectional(speechBubbles, messageContainer.getChildren());
        messageScroller = new ScrollPane(messageContainer);
        messageScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        messageScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScroller.setPrefHeight(300);
        messageScroller.prefWidthProperty().bind(messageContainer.prefWidthProperty().subtract(5));
        messageScroller.setFitToWidth(true);
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if(change.wasAdded()){
                    messageScroller.setVvalue(messageScroller.getVmax());
                }
            }
        });
    }

    private void setupInputDisplay(){
        inputContainer = new HBox(5);

        TextField userInput = new TextField();
        userInput.setPromptText("Ingresa el mensaje");

        Button sendMessageButton = new Button("Enviar");
        sendMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        sendMessageButton.setOnAction(event-> {
            new Conexion().mandarMensaje(userInput.getText());
            userInput.setText("");
        });


        Button receiveMessageButton = new Button("Participación");
        receiveMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        receiveMessageButton.setOnAction(event-> {
            new Conexion().mandarParticipacion(userInput.getText());
            userInput.setText("");
        });
        receiveMessageButton.setVisible(false);
        inputContainer.getChildren().setAll(userInput, sendMessageButton,receiveMessageButton);
    }

    public void sendMessage(String message){
        speechBubbles.add(new SpeechBox(message, SpeechDirection.RIGHT));
    }

    public void receiveMessage(String message){
        speechBubbles.add(new SpeechBox(message, SpeechDirection.LEFT));
    }
}