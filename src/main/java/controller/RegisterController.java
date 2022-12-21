package controller;

import com.example.socialnet.SocialApplication;
import domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class RegisterController {
    Service service;

    @FXML
    public TextField firstName;

    @FXML
    public TextField lastName;

    @FXML
    public TextField username;

    @FXML
    public TextField password;

    @FXML
    public Button registerButton;

    @FXML
    public Hyperlink backhyperlink;

    @FXML
    void onRegisterButtonClick(){
        String first_name = firstName.getText();
        String last_name = lastName.getText();
        String userName = username.getText();
        String pass = password.getText();

        if (first_name.isEmpty() || last_name.isEmpty() || userName.isEmpty()|| pass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Fail");
            alert.setContentText("Invalid data");
            alert.show();
            return;
        }


        //encrypting the password
        try {
            // Get the key to use for encrypting the data
            byte[] key = getKey();

            // Create a Cipher instance for encrypting the data
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // Encrypt the data
            String originalString = pass;
            byte[] encryptedBytes = cipher.doFinal(originalString.getBytes());

            // Print the encrypted data
            System.out.println(new String(encryptedBytes));

            User u = new User(first_name, last_name, userName, new String(encryptedBytes));
            service.addUser(u);

            FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("interface.fxml"));
            try {
                AnchorPane root = loader.load();

                InterfaceController ctrl =loader.getController();
                ctrl.setService(service, userName);
                //ctrl.loadCommunity();

                ctrl.welcomeUser(userName);

                Stage stage = new Stage();
                stage.setScene(new Scene(root, 800, 500));
                stage.show();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            Stage thisStage = (Stage) registerButton.getScene().getWindow();
            thisStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void hyperlinkClicked() {
        FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("hello-view.fxml"));
        try {
            AnchorPane root = loader.load();

            HelloController ctrl =loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 320, 400));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e);
        }

        Stage thisStage = (Stage) backhyperlink.getScene().getWindow();
        thisStage.close();
    }

    public void setService(Service service){
        this.service = service;
    }

    private static byte[] getKey() {
        return new byte[]{
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };
    }
}
