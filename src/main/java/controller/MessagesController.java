package controller;

import domain.Message;
import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import service.MessageService;
import service.Service;

import java.time.LocalDateTime;
import java.util.List;

public class MessagesController {
    private Service service;

    private MessageService messageService;

    private String name;

    private String ownerName;

    @FXML
    TextField textField;

    @FXML
    Button sendButton;

    @FXML
    TextArea textArea;

    @FXML
    Text text;

    public void setService(Service service, String name, String ownerName, MessageService messageService){
        this.service = service;
        this.name = name;
        this.ownerName = ownerName;
        this.messageService = messageService;
        //text.setStyle("-fx-font-size: 20;");
        text.setText("Chatting with " + this.name);
        User owner = service.findUserByUsername(ownerName);
        User friend = service.findUserByUsername(name);
        List<Message> messageList = messageService.getMessagesInOrder(owner, friend);
        if(messageList != null) {
            for (Message m : messageList) {
                textArea.appendText("\nfrom "+service.findUserById(m.getFrom()).getUsername()+" to "+service.findUserById(m.getTo()).getUsername()+":\n"+m.getMessage());
            }
        }

    }

    @FXML
    private void onSendButtonClick(){
        String text = textField.getText();
        if(text.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Fail");
            alert.setContentText("No input text given");
            alert.show();
            return;
        }
        else{
            Message m = new Message(text, service.findUserByUsername(ownerName).getId(), service.findUserByUsername(name).getId(), LocalDateTime.now());
            messageService.addMessage(m);
            textArea.appendText("\nfrom "+service.findUserById(m.getFrom()).getUsername()+" to "+service.findUserById(m.getTo()).getUsername()+":\n"+m.getMessage());
            textField.clear();
        }
    }

}
