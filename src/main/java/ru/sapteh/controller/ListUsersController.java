package ru.sapteh.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.model.User;
import ru.sapteh.service.UserService;


public class ListUsersController {

    private final UserService userService;
    private final ObservableList<User> users = FXCollections.observableArrayList();


    public ListUsersController() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        userService = new UserService(factory);
    }

    //Table view
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, Integer> ageColumn;

    //Search
    @FXML
    private TextField searchTxt;
    @FXML
    private Button searchButton;

    //Data count
    @FXML
    private Label countLbl;

    //User info
    @FXML
    public Label labelId;
    @FXML
    public TextField textFieldFirstName;
    @FXML
    public TextField textFieldLastName;
    @FXML
    public TextField textFieldAge;

    @FXML
    private void initialize() {
        initUsersFromDatabase();

        //Filtered by user first name
        searchByFirstName();

        //Initialize TableView
        userTableView.setItems(users);
        userTableView.setEditable(true);

        idColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getId()));

        //Update cell firstName
        firstNameColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getFirstName()));
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameColumn.setOnEditCommit(event -> {
            User user = event.getTableView().getItems().get(event.getTablePosition().getRow());
            user.setFirstName(event.getNewValue());
            userService.update(user);
        });

        //Update cell lastName
        lastNameColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getLastName()));
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setOnEditCommit(event -> {
            User user = event.getTableView().getItems().get(event.getTablePosition().getRow());
            user.setLastName(event.getNewValue());
            userService.update(user);
        });

        //Update cell age
        ageColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getAge()));
        ageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(Integer age) {
                return String.valueOf(age);
            }

            @Override
            public Integer fromString(String age) {
                return Integer.parseInt(age);
            }
        }));
        ageColumn.setOnEditCommit(event -> {
            User user = event.getTableView().getItems().get(event.getTablePosition().getRow());
            user.setAge(event.getNewValue());
            userService.update(user);
        });

        //Выделение строки в таблице, подключение слушателя и выведение детальной информации в правой части окна
        listenerTabUserDetails(null);
        userTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listenerTabUserDetails(newValue);
        });

        //Data count
        countLbl.setText(String.valueOf(userTableView.getItems().size()));
    }

    private void initUsersFromDatabase() {
        users.addAll(userService.findAll());
    }

    @FXML
    public void updateUserButton(ActionEvent actionEvent) {
        int selectedIndex = userTableView.getSelectionModel().getSelectedIndex();
        final User user = userTableView.getSelectionModel().getSelectedItem();
        user.setFirstName(textFieldFirstName.getText());
        user.setLastName(textFieldLastName.getText());
        user.setAge(Integer.parseInt(textFieldAge.getText()));
        userTableView.getItems().set(selectedIndex, user);
        userService.update(user);
        cleanTextField();
        countLbl.setText(String.valueOf(userTableView.getItems().size()));
    }

    @FXML
    public void deleteUserButton(ActionEvent actionEvent) {
        final User user = userTableView.getSelectionModel().getSelectedItem();
        userService.delete(user);
        userTableView.getItems().remove(user);
        cleanTextField();
        System.out.println("Delete user: " + user);
        countLbl.setText(String.valueOf(userTableView.getItems().size()));
    }

    @FXML
    public void saveUserButton(ActionEvent actionEvent) {
        //Создаем пользователя, в качестве значений принимаем данные с текстовых полей.
        User user = new User(textFieldFirstName.getText(), textFieldLastName.getText(), Integer.parseInt(textFieldAge.getText()));
        //Сохраняем данные в базу данных
        userService.save(user);
        //Добавляем в коллекцию для моментального обновления
        userTableView.getItems().add(user);
        //Очищаем поля
        cleanTextField();
        //Счетчик количества записей в БД
        countLbl.setText(String.valueOf(userTableView.getItems().size()));
    }

    private void listenerTabUserDetails(User user) {
        if (user != null) {
            labelId.setText(String.valueOf(user.getId()));
            textFieldFirstName.setText(user.getFirstName());
            textFieldLastName.setText(user.getLastName());
            textFieldAge.setText(String.valueOf(user.getAge()));
        } else {
            labelId.setText("");
            textFieldFirstName.setText("");
            textFieldLastName.setText("");
            textFieldAge.setText("");
        }
    }

    private void searchByFirstName() {
        searchTxt.textProperty().addListener((obs, old, newValue) -> {
            FilteredList<User> userFilteredList = new FilteredList<>(users,
                    s -> s.getFirstName().toLowerCase().contains(newValue.toLowerCase().trim()));
            userTableView.setItems(userFilteredList);
            countLbl.setText(String.valueOf(userFilteredList.size()));
        });
    }

    private void cleanTextField() {
        textFieldFirstName.clear();
        textFieldLastName.clear();
        textFieldAge.clear();
        labelId.setText("");
    }
}