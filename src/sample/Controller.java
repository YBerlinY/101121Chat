package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;



public class Controller {
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    private EchoClient client;





    public Controller() {

        client= new EchoClient(this);
    }


    public  void btnSendClick(ActionEvent actionEvent) {
        final String message = textField.getText().trim();
        if (message.isEmpty()){
            return;
        }
        client.sendMessage(message);
        textField.clear();
        textField.requestFocus();

    }

    public void addMessage(String message) {

        textArea.appendText(message+ "\n");
    }
}