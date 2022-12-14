package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import service.Service;

public class SearchController {

    Service service;

    private String name;

    @FXML
    ListView<String> listView;

    @FXML
    TextField searchBox;

    public void setService(Service service) {
        this.service = service;
        //users.setAll(getFriendsWithUser());
        this.name = name;
    }

    public void update(){

    }

    public void initialize(){
        searchBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                update();
            }
        });
    }
}
