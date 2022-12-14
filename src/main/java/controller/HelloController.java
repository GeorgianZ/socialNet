package controller;
import com.example.socialnet.SocialApplication;
import controller.RegisterController;
import domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Objects;

public class HelloController {
    Service service;
    @FXML
    private Button loginButton;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;

    @FXML
    public Hyperlink hyperlink;

    @FXML
    protected void onloginButtonClick() {
        String name = username.getText();
        String pass = password.getText();
        boolean valid = false;
        User user = null;

        if (name.isEmpty() || pass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Fail");
            alert.setContentText("Invalid user name or password");
            alert.show();
            return;
        }

        if(service.userExistsByUsername(name)) {
            user = service.findUserByUsername(name);
            valid = true;
        }

        if (!valid) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hellow");
            alert.setHeaderText("Fail");
            alert.setContentText("Invalid user name");
            alert.show();
            return;
        }
        //System.out.println(name);
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
            //System.out.println(new String(encryptedBytes));
            if (!Objects.equals(user.getPassword(), new String(encryptedBytes))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hellow");
                alert.setHeaderText("Fail");
                alert.setContentText("Invalid password");
                alert.show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Ai ajuns la final bossule");

        FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("interface.fxml"));
        try {
            AnchorPane root = loader.load();

            InterfaceController ctrl =loader.getController();
            ctrl.setService(service, name);
            ctrl.loadCommunity();

            ctrl.welcomeUser(name);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 500));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Stage thisStage = (Stage) loginButton.getScene().getWindow();
        thisStage.close();

    }

    @FXML
    void hyperlinkClicked() {
        FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("register.fxml"));
        try {
            AnchorPane root = loader.load();

            RegisterController ctrl =loader.getController();
            ctrl.setService(service);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 322, 375));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e);
        }

        Stage thisStage = (Stage) hyperlink.getScene().getWindow();
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