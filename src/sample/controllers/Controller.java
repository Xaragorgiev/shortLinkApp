package sample.controllers;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.ControllerHelp;
import sample.DB;
import sample.Main;

import static javafx.fxml.FXMLLoader.load;

public class Controller {

    @FXML
    private Button addButton;

    @FXML
    private TextField link_long;

    @FXML
    private TextField link_short;

    @FXML
    private VBox paneVBox;

    @FXML
    private Label help;

    private final DB db = new DB();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException, IOException {

        File file = new File("ControllerHelp.settings");
        boolean exists = file.exists();
        if(exists) {
            FileInputStream fis = new FileInputStream("ControllerHelp.settings");
            ObjectInputStream ois = new ObjectInputStream(fis);

            ControllerHelp controllerHelp = (ControllerHelp) ois.readObject();

            ois.close();
            help.setText(controllerHelp.getHelp());
            help.setStyle(controllerHelp.getHelpStyle());
            link_long.setText(controllerHelp.getLink_long());
            link_short.setText(controllerHelp.getLing_short());
        }

        if (!db.getShortLink(link_short.getCharacters().toString())) {
            help.setStyle("-fx-text-fill: #d70000");
            link_short.setStyle("-fx-border-color: #d70000");
            help.setText("Укажите другое сокращение!");
        }

        ResultSet res = db.getAllLinks();
        while (res.next()){
            Node node = null;
            try {
                node = load(getClass().getResource("/sample/scenes/link.fxml"));

                Hyperlink hyperlink = (Hyperlink) node.lookup("#short_link");
                hyperlink.setText(res.getString("short_link"));
                URI uri = new URI(res.getString("long_link"));
                hyperlink.setOnMouseClicked(mouseEvent -> {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        try {
                            Desktop.getDesktop().browse(uri);
                        } catch (IOException e) {
                            help.setText("Адрес недействителен");
                            help.setStyle("-fx-text-fill: #d70000");
                            e.printStackTrace();
                        }
                    }
                });

                Node delete = node.lookup("#delete");

                int id = res.getInt("id");
                delete.setOnMouseClicked(event -> {
                    try {
                        db.deletelink(id);
                        FileOutputStream fos = new FileOutputStream("ControllerHelp.settings");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);

                        oos.writeObject(new ControllerHelp(link_long.getText(), link_short.getText(), "Успешно удалено", "-fx-text-fill: #1ee63c"));

                        oos.close();
                        updateApp(event);
                    } catch (Exception throwables) {
                        throwables.printStackTrace();
                    }
                });

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
            paneVBox.getChildren().add(node);
        }

        addButton.setOnAction(event -> {
            try {
                if (link_long.getCharacters().length() != 0) {
                    if (link_short.getCharacters().length() != 0) {
                        if (db.getShortLink(link_short.getCharacters().toString())) {
                            db.insertlinks(link_long.getCharacters().toString(), link_short.getCharacters().toString());
                            FileOutputStream fos = new FileOutputStream("ControllerHelp.settings");
                            ObjectOutputStream oos = new ObjectOutputStream(fos);

                            oos.writeObject(new ControllerHelp("", "", "Добавлено!", "-fx-text-fill: #1ee63c"));

                            oos.close();
                            updateApp(event);
                        } else {
                            help.setStyle("-fx-text-fill: #d70000");
                            link_short.setStyle("-fx-border-color: #d70000");
                            help.setText("Укажите другое сокращение!");
                        }
                    } else {
                        help.setStyle("-fx-text-fill: #d70000");
                        help.setText("Заполните все поля");
                        link_short.setStyle("-fx-border-color: #d70000");
                    }
                } else {
                link_long.setStyle("-fx-border-color: #d70000");
                help.setStyle("-fx-text-fill: #d70000");
                help.setText("Заполните все поля");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        link_short.setOnKeyTyped(keyEvent -> {
            try {
                if (!db.getShortLink(link_short.getCharacters().toString())) {
                    help.setStyle("-fx-text-fill: #d70000");
                    link_short.setStyle("-fx-border-color: #d70000");
                    help.setText("Укажите другое сокращение!");
                } else {
                    link_short.setStyle("-fx-border-color: #fafafa");
                    help.setText("");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        link_long.setOnKeyTyped(keyEvent -> {
            if (link_long.getText().length() > 0) {
                link_long.setStyle("-fx-border-color: #fafafa");
                help.setText("");
            }
        });

    }

    private void updateApp(Event event) throws Exception {
        Main app = new Main();
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app.start(primaryStage);
    }

}
