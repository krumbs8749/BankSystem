package ui;

import bank.PrivateBank;
import bank.Transaction;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionAttributeException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.beans.binding.Bindings;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AccountViewController {
     @FXML private ListView transactionsView;

     @FXML private Button goBack;
     @FXML private ComboBox<String> sortType;
     @FXML private ComboBox<String> direction;
     @FXML private  Button newTransaction;



     @FXML private ResourceBundle resources;
     @FXML private URL location;
    private Set<String> stringSet = new HashSet<>();
    private String account;
    private LinkedHashMap<String, Transaction> transactionList;
    private String SortOrByType;
    private boolean ASCOrPositive = true;
    ObservableList<String> observableList = FXCollections.observableArrayList();

    PrivateBank bank = new PrivateBank("Main Bank", 0.5, 0.5);

    public AccountViewController(String account) throws IOException {
        this.account = account;
    }

    @FXML
     void initialize() {
         assert transactionsView != null : "fx:id=\"transactionsView\" was not injected: check your FXML file 'mainPage.fxml'.";
         assert account != null : "Failed to get account name";

         transactionList = new LinkedHashMap<>();
         bank.getTransactions(account).forEach(transaction -> {
             transactionList.put(transaction.toString(), transaction);
         });

         setGoBack();
         this.SortOrByType = "type";
         setSortType();
         setDirection();
         setNewTransaction();
     }
    public void setGoBack()
    {
        goBack.setOnAction(e -> {
            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainPage.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            stage.setTitle("Bank");
            stage.setScene(scene);
            stage.show();
        });
    }
    public void setSortType(){
        sortType.getItems().add("type");
        sortType.getItems().add("normal");

        sortType.setOnAction((event) -> {

            this.SortOrByType = sortType.getSelectionModel().getSelectedItem();

            setDirection();
        });
    }
    public void setDirection(){
        direction.getItems().clear();
        if(SortOrByType.equals("normal")){
            direction.getItems().addAll("ASC", "DESC");
            direction.getSelectionModel().clearSelection();
        }
        if(SortOrByType.equals("type")){
            direction.getItems().addAll("Positive", "Negative");
            direction.getSelectionModel().clearSelection();
        }



        direction.setOnAction((event) -> {
            String selectedItem = (String)((ComboBox)event.getTarget()).getSelectionModel().getSelectedItem();


            if(SortOrByType.equals("type")){
                this.ASCOrPositive = selectedItem.equals("Positive");
            }else{
                this.ASCOrPositive = selectedItem.equals("ASC");
            }

            stringSet.clear();



            setListView();
        });
        setListView();
    }
    public void setListView()
    {
        transactionList.clear();
        if(SortOrByType.equals("type")){
            bank.getTransactionsByType(account, this.ASCOrPositive).forEach(transaction -> {
                transactionList.put(transaction.toString(), transaction);
            });
        }else{

            bank.getTransactionsSorted(account, this.ASCOrPositive).forEach(transaction -> {
                transactionList.put(transaction.toString(), transaction);
            });
        }

        this.transactionsView.getItems().clear();
        this.observableList.setAll(this.transactionList.keySet());
        transactionsView.setItems(this.observableList);
        transactionsView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

                @Override
                public ListCell<String> call(ListView<String> arg0) {
                    return new ListCell<>() {

                        private final Label transaction;
                        private final ContextMenu contextMenu;
                        private final MenuItem loschen;
                        private final Dialog<ButtonType>  dialog;
                        private final ButtonType yes;
                        private final ButtonType no;

                        {
                            setContentDisplay(ContentDisplay.CENTER);
                            transaction = new Label();
                            contextMenu = new ContextMenu();
                            loschen = new MenuItem("loschen");
                            //Creating a dialog
                            dialog = new Dialog<ButtonType>();
                            yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                            no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);


                        }

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setGraphic(null);
                                setContextMenu(null);
                            } else {
                                transaction.setText(item);
                                contextMenu.getItems().clear();
                                loschen.setOnAction(e -> {
                                    //Setting the title
                                    dialog.setTitle("Loschen");
                                    //Setting the content of the dialog
                                    dialog.setContentText("Are you sure you want to delete the transaction");

                                    //Adding buttons to the dialog pane
                                    dialog.getDialogPane().getButtonTypes().addAll(yes,no);
                                    dialog.showAndWait().ifPresent(res-> {
                                        if(res.equals(yes)){

                                            try {
                                                bank.removeTransaction(account, transactionList.get(item));
                                            } catch (AccountDoesNotExistException | TransactionDoesNotExistException ex) {
                                                System.out.println(e.getClass() +":"+e.getSource());

                                            }
                                            AccountViewController.this.setListView();
                                        }
                                    });


                                });
                                setGraphic(transaction);
                                contextMenu.getItems().add(loschen);
                                setContextMenu(contextMenu);

                            }
                        }
                    };
                }
            });
    }

    public void setNewTransaction() {
        newTransaction.setOnAction(e -> {
            Dialog<Transaction> addTransactionDialog = new TransactionDialog(account);
            addTransactionDialog.showAndWait().ifPresent(res-> {
                try {
                        bank.addTransaction(account, res);
                } catch (AccountDoesNotExistException | TransactionAttributeException | TransactionAlreadyExistException ex) {
                        System.out.println(e.getClass() +":"+e.getSource());
                }
                setListView();
                setNewTransaction();
                addTransactionDialog.close();

            });
            addTransactionDialog.show();
        });
    }
}
