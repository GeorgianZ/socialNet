package controller;

import com.example.socialnet.SocialApplication;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.Service;
import java.util.stream.Collectors;

import java.util.List;

public class InterfaceController {
    ObservableList<User> users = FXCollections.observableArrayList();

    private Service service;

    private String name;

    @FXML
    ListView<String> listView;

    @FXML
    Label welcomeLabel;

    @FXML
    Button homeButton;

    @FXML
    Button searchButton;

    @FXML
    Button notificationButton;

    public void setService(Service service, String name) {
        this.service = service;
        //users.setAll(getFriendsWithUser());
        this.name = name;
    }

    public void welcomeUser(String name) {
        welcomeLabel.setText("Hello "+name);
    }

    @FXML
    public void loadCommunity(){
        /*for(User u : service.getFriendsForUser(service.findUserByUsername(this.name))){
            listView.getItems().add(u.toString());*/
        System.out.println(getFriendsWithUser().size());
        listView.setStyle("-fx-font-size: 21;");
        for(User u:getFriendsWithUser()){
            listView.getItems().add(u.toString());
        }
    }

    private List<User> getFriendsWithUser(){
        /*return service.getFriendsForUser(service.findUserByUsername(this.name))
                .stream()
                .map(u -> new User(u.getFirstName(), u.getLastName(), u.getUsername(), u.getPassword()))
                .collect(Collectors.toList());*/
        return service.getListOfUsers()
                .stream()
                .map(u -> new User(u.getFirstName(), u.getLastName(), u.getUsername(), u.getPassword()))
                .collect(Collectors.toList());
    }

    @FXML
    private void onSearchButtonClick(){
        FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("search.fxml"));
        try {
            AnchorPane root = loader.load();

            SearchController ctrl =loader.getController();
            ctrl.setService(service);

            //ctrl.welcomeUser(userName);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 500));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Stage thisStage = (Stage) searchButton.getScene().getWindow();
        thisStage.close();

    }
}
