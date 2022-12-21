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
import service.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NotificationsController {
    Service service;

    private String name;

    ObservableList<User> model = FXCollections.observableArrayList();

    LocalDateTime date;

    @FXML
    TableView<User> tableView;

    @FXML
    TableColumn<User, String> last_name;

    @FXML
    TableColumn<User, String> first_name;

    @FXML
    TableColumn<User, String> username;


    @FXML
    Button homeButton;

    @FXML
    Button acceptButton;

    @FXML
    Button declineButton;
    
    @FXML
    Label dateLabel;

    public void setService(Service service, String name) {
        this.service = service;
        //users.setAll(getFriendsWithUser());
        this.name = name;
        initModel();
    }

    @FXML
    private void initialize(){
        this.last_name.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        this.first_name.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        this.username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        this.tableView.setItems(this.model);
    }

    private void initModel(){
        List<User> friendsList = StreamSupport.stream(getFriendsWithUser().spliterator(), false).collect(Collectors.toList());
        model.setAll(friendsList);
    }

    private List<User> getFriendsWithUser(){
        /*return service.getFriendsForUser(service.findUserByUsername(this.name))
                .stream()
                .map(u -> new User(u.getFirstName(), u.getLastName(), u.getUsername(), u.getPassword()))
                .collect(Collectors.toList());*/
        return service.getFriendRequests(service.findUserByUsername(this.name))
                .stream()
                .map(u -> new User(u.getFirstName(), u.getLastName(), u.getUsername(), u.getPassword()))
                .collect(Collectors.toList());
    }

    @FXML
    private void onAcceptButtonClick(){
        User user = tableView.getSelectionModel().getSelectedItem();
        User user1 = service.findUserByUsername(user.getUsername());
        System.out.println(user1.getUsername());

        User owner = service.findUserByUsername(this.name);
        service.updateFriendship2(owner.getId(), user1.getId(), owner.getId(), user1.getId());
        acceptButton.setDisable(true);
        declineButton.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Ati acceptat cererea de prietenie");
        alert.setContentText("Cerere de prietenie acceptata cu succes");

        alert.showAndWait();
        System.out.println("accept");
    }

    @FXML
    private void onDeclineButtonClick(){
        User user = tableView.getSelectionModel().getSelectedItem();
        model.remove(user);
        User user1 = service.findUserByUsername(user.getUsername());
        User owner = service.findUserByUsername(this.name);
        service.removeFriendship(owner.getId(), user1.getId());
        declineButton.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("S-a sters cererea de prietenie");
        alert.setContentText("Cerere de prietenie stearsa cu succes");

        alert.showAndWait();
    }

    @FXML
    private void onHomeButtonClick(){
        FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("interface.fxml"));
        try {
            AnchorPane root = loader.load();

            InterfaceController ctrl =loader.getController();
            ctrl.setService(service, name);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 500));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Stage thisStage = (Stage) homeButton.getScene().getWindow();
        thisStage.close();
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
        dateLabel.setText(String.valueOf(time));
        acceptButton.setDisable(false);
        declineButton.setDisable(false);
    }


}
