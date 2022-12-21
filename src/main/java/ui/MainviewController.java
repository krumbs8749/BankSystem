package ui;

import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class MainviewController {

    @FXML private ListView listView;
    @FXML private Button newAccount;

     @FXML private ResourceBundle resources;

     @FXML private URL location;
    private Set<String> stringSet = new HashSet<>();
    ObservableList<String> observableList = FXCollections.observableArrayList();

    PrivateBank bank = new PrivateBank("Main Bank", 0.5, 0.5);

    public MainviewController() throws IOException {
    }

    @FXML
     void initialize() {
         assert listView != null : "fx:id=\"listView\" was not injected: check your FXML file 'mainPage.fxml'.";

         setListView();
         setNewAccount();
     }


    public void setListView()
    {
        bank.getAllAccounts().forEach(acc -> {
            stringSet.add(acc);
        });
//        label.setText("List of accounts");
        this.observableList.setAll(this.stringSet);
        listView.setItems(this.observableList);
        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

                @Override
                public ListCell<String> call(ListView<String> arg0) {
                    return new ListCell<>() {

                        private final Label account;
                        private final ContextMenu contextMenu;

                        private final MenuItem auswahlen = new MenuItem("Auswahlen");
                        private final MenuItem loschen = new MenuItem("loschen");

                        {
                            setContentDisplay(ContentDisplay.CENTER);
                            account = new Label();
                            contextMenu = new ContextMenu();
                        }

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setGraphic(null);
                                setContextMenu(null);
                            } else {

                                contextMenu.getItems().clear();
                                account.setText(item);
                                setGraphic(account);
                                auswahlen.setOnAction(e -> {
                                    Stage stage = (Stage) ((MenuItem)e.getSource()).getParentPopup().getOwnerWindow();
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("accountView.fxml"));
                                    Scene scene = null;
                                    try {
                                        fxmlLoader.setController(new AccountViewController(item));
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    try {
                                        scene = new Scene(fxmlLoader.load());
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    stage.setTitle("Bank");
                                    stage.setScene(scene);
                                    stage.show();

                                });
                                loschen.setOnAction(e -> {
                                    stringSet.remove(item);
                                    try {
                                        bank.deleteAccount(item);
                                    } catch (Exception ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    listView.getItems().remove(item);
                                });
                                contextMenu.getItems().addAll(auswahlen, loschen);
                                setContextMenu(contextMenu);

                            }
                        }
                    };
                }
            });
    }
    public void setNewAccount() {
        newAccount.setOnAction(e -> {
            Dialog<String> addAccount = new AccountDialog();
            addAccount.showAndWait().ifPresent(res-> {
                try {
                        bank.createAccount(res);
                } catch (AccountAlreadyExistsException ex) {
                        System.out.println(e.getClass() +":"+e.getSource());
                }
                setListView();

            });
            addAccount.show();
        });
    }
}