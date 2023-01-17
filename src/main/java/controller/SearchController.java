package controller;

import com.example.socialnet.SocialApplication;
import domain.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SearchController {

    Service service;

    MessageService messageService;

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
    Button addFriendButton;

    @FXML
    Button homeButton;

    @FXML
    TextField searchBox;

    public void setService(Service service, String name, MessageService messageService) {
        this.service = service;
        //users.setAll(getFriendsWithUser());
        this.name = name;
        this.messageService = messageService;
    }

    public void update(){
        Predicate<User> p1 = n -> n.getUsername().contains(searchBox.getText());
        Predicate<User> p2 = n -> n.getFirstName().contains(searchBox.getText());
        Predicate<User> p3 = n -> n.getLastName().contains(searchBox.getText());

        model.setAll(getFriendsWithUser()
                .stream()
                .filter(p1.or(p2).or(p3))
                .collect(Collectors.toList()));
    }


    private List<User> getFriendsWithUser(){
        /*return service.getFriendsForUser(service.findUserByUsername(this.name))
                .stream()
                .map(u -> new User(u.getFirstName(), u.getLastName(), u.getUsername(), u.getPassword()))
                .collect(Collectors.toList());*/
        return service.getNonFriendsForUser(service.findUserByUsername(this.name))
                .stream()
                .map(u -> new User(u.getFirstName(), u.getLastName(), u.getUsername(), u.getPassword()))
                .collect(Collectors.toList());
    }

    @FXML
    private void initialize(){
        this.last_name.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        this.first_name.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        this.username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        this.tableView.setItems(this.model);

        searchBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                update();
            }
        });
    }

    @FXML
    private void onAddButtonClick(){
        User user = tableView.getSelectionModel().getSelectedItem();
        User user1 = service.findUserByUsername(user.getUsername());
        System.out.println(user1.getUsername());

        User owner = service.findUserByUsername(this.name);
        System.out.println(owner.getUsername());
        System.out.println(user1.getId());
        System.out.println(owner.getId());
        service.addFriendship(owner.getId(), user1.getId());
        addFriendButton.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("S-a trimis cererea de prietenie");
        alert.setContentText("Cerere de prietenie trimisa cu succes");

        alert.showAndWait();
    }

    @FXML
    private void onHomeButtonClick(){
        FXMLLoader loader = new FXMLLoader(SocialApplication.class.getResource("interface.fxml"));
        try {
            AnchorPane root = loader.load();

            InterfaceController ctrl =loader.getController();
            ctrl.setService(service, name, messageService);
            //ctrl.loadCommunity();

            ctrl.welcomeUser(name);

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
        addFriendButton.setDisable(false);
    }
}
