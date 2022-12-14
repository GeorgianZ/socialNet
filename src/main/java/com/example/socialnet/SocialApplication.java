package com.example.socialnet;

import controller.HelloController;
import domain.Friendship;
import domain.Tuple;
import domain.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.database.FriendshipDBRepository;
import repository.database.Repository;
import repository.database.UserDBRepository;
import service.Service;
import validate.FriendshipValidator;
import validate.UserValidator;

import java.io.IOException;

public class SocialApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Repository<Long, User> repoDb = new UserDBRepository("jdbc:postgresql://localhost:5432/socialNetworkDB", "postgres", "tigancopt64", new UserValidator());
        Repository<Tuple<Long, Long>, Friendship> networkDb = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/socialNetworkDB", "postgres", "tigancopt64", new FriendshipValidator());
        Service service = new Service(repoDb, networkDb);

        FXMLLoader fxmlLoader = new FXMLLoader(SocialApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        HelloController ctrl = fxmlLoader.getController();
        ctrl.setService(service);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}