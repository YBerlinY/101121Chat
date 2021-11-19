package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.List;


public class Controller extends Commands {
    @FXML
    private ListView<String> clientList;
    @FXML
    private HBox messageBox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField loginField;
    @FXML
    private HBox loginBox;
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    private final ChatClient client;


    public Controller() {
        client = new ChatClient(this);
        client.openConnection();
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

        textArea.appendText(message + "\n");
    }

    public void btnAuthClick(ActionEvent actionEvent) {
        client.sendMessage(AUTH_COMMAND +" "+ loginField.getText() + " " + passwordField.getText());


    }

    public void setAuth(boolean isClientAuth) {
        loginBox.setVisible(!isClientAuth);
        messageBox.setVisible(isClientAuth);

    }

    public void selectClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount()==2){
            final String message =textField.getText();
            final String nick = clientList.getSelectionModel().getSelectedItem();
            textField.setText(SEND_MESSAGE_TO_CLIENT_COMMAND+" "+nick+" "+message);
            textField.requestFocus();
            textField.selectEnd();

        }

    }

    public void updateClientsList(List<String> clients) {
        clientList.getItems().clear();
        clientList.getItems().addAll(clients);
    }
}