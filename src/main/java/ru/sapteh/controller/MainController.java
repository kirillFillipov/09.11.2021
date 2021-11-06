package ru.sapteh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.model.User;
import ru.sapteh.service.UserService;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    private final UserService userService;

    public MainController() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        this.userService = new UserService(factory);
    }

    @FXML
    private Button inputButton;

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField lastNameText;

    @FXML
    private TextField ageText;

    @FXML
    private Label alertText;

    @FXML
    public void initialize() {

    }

    public void addedButton(ActionEvent actionEvent) {
        boolean isAdd = userService.save(new User(
                firstNameText.getText(),
                lastNameText.getText(),
                Integer.parseInt(ageText.getText())
        ));
       if (isAdd){
           alertText.setTextFill(Color.GREEN);
           alertText.setText("Вы успешно добавили пользователя");
       }
       else {
           alertText.setText("Пользователь не добавлен");
       }
       firstNameText.clear();
       lastNameText.clear();
       ageText.clear();

    }

    public void exitButton(ActionEvent actionEvent) {
        final Button source = (Button) actionEvent.getSource();
        source.getScene().getWindow().hide();
    }

    public void inputButton(ActionEvent actionEvent){
        Button input =(Button) actionEvent.getSource();
        input.getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/list_users.fxml")));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        stage.setTitle("List users");
        assert root != null;
        stage.setScene(new Scene(root));
        stage.show();
    }
}

