package controller;

import com.example.socialnet.SocialApplication;
import domain.Friendship;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.MessageService;
import service.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import java.util.List;
import java.util.stream.StreamSupport;

public class InterfaceController {
    ObservableList<User> users = FXCollections.observableArrayList();

    private Service service;

    private MessageService messageService;

    private String name;

    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableView;

    @FXML
    TableColumn<User, String> last_name;

    @FXML
    TableColumn<User, String> first_name;

    @FXML
    TableColumn<User, String> username;

    @FXML
    Label welcomeLabel;

    @FXML
    Button homeButton;

    @FXML
    Button searchButton;

    @FXML
    Button notificationButton;

    @FXML
    Button deleteButton;

    @FXML
    Button messageButton;

    @FXML
    Label dateLabel;

    public void setService(Service service, String name, MessageService messageService) {
        this.service = service;
        //users.setAll(getFriendsWithUser());
        this.name = name;
        this.messageService = messageService;
        initModel();
    }

    private void initModel(){
        List<User> friendsList = StreamSupport.stream(getFriendsWithUser().spliterator(), false).collect(Collectors.toList());
        model.setAll(friendsList);
    }

    @FXML
    private void initialize(){
        this.last_name.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        this.first_name.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        this.username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        this.tableView.setItems(this.model);
        deleteButton.setDisable(true);
        messageButton.setDisable(true);
    }

    public void welcomeUser(String name) {
        welcomeLabel.setText("Hello "+name);
    }

   /*@FXML
    public void loadCommunity(){
        /*for(User u : service.getFriendsForUser(service.findUserByUsername(this.name))){
            listView.getItems().add(u.toString());
        System.out.println(getFriendsWithUser().size());
        listView.setStyle("-fx-font-size: 20;");
        for(User u:getFriendsWithUser()){
            listView.getItems().add(u.toString());
        }
    }*/

    private List<User> getFriendsWithUser(){
        return service.getFriendsForUser(service.findUserByUsername(this.name))
                .stream()
                .map(u -> new User(u.getFirstName(), u.getLastName(), u.getUsername(), u.getPassword()))
                .collect(Collectors.toList());
        /*return service.getListOfUsers()
                .stream()
                .map(u -> new User(u.getFirstName(), u.getLastName(), u.getUsername(), u.getPassword()))
                .collect(Collectors.toList());*/
    }

    @FXML
    private void onSearchButtonClick(){
        FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("search.fxml"));
        try {
            AnchorPane root = loader.load();

            SearchController ctrl =loader.getController();
            ctrl.setService(service, name, messageService);

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

    @FXML
    private void onDeleteButtonClick(){
        User user = tableView.getSelectionModel().getSelectedItem();
        model.remove(user);
        User user1 = service.findUserByUsername(user.getUsername());
        User owner = service.findUserByUsername(this.name);
        service.removeFriendship(owner.getId(), user1.getId());
        deleteButton.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("S-a sters prietenia");
        alert.setContentText("Prietenie stearsa cu succes");

        alert.showAndWait();
    }

    @FXML
    public void tableViewMouseClicked() {
        User user = tableView.getSelectionModel().getSelectedItem();
        User user1 = service.findUserByUsername(user.getUsername());
        User owner = service.findUserByUsername(this.name);
        LocalDateTime time = null;
        for(Friendship f: service.getFriendships()){
            if(f.getUser1() == user1.getId() && f.getUser2() == owner.getId())
                time = f.getDateTime();
            else
            if(f.getUser1() == owner.getId() && f.getUser2() == user1.getId()){
                time = f.getDateTime();
            }
        }
        //dateLabel.setText(String.valueOf(time));
        deleteButton.setDisable(false);
        messageButton.setDisable(false);
    }

    @FXML
    private void onNotificationButtonClick(){
        FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("notifications.fxml"));
        try {
            AnchorPane root = loader.load();

            NotificationsController ctrl =loader.getController();
            LocalDateTime dateTime = LocalDateTime.now();
            ctrl.setService(service, name, messageService);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 500));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Stage thisStage = (Stage) notificationButton.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    private void onMessageButtonClick(){
        messageButton.setDisable(true);
        User user = tableView.getSelectionModel().getSelectedItem();
        User owner = service.findUserByUsername(this.name);
        FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("messages.fxml"));
        try {
            AnchorPane root = loader.load();

            MessagesController ctrl = loader.getController();

            ctrl.setService(service, user.getUsername(), owner.getUsername(), messageService);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 602, 402));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Stage thisStage = (Stage) messageButton.getScene().getWindow();
        thisStage.close();
    }
}
