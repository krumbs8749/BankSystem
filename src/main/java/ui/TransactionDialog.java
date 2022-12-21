package ui;

import bank.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.Date;
import java.util.Objects;

public class TransactionDialog extends Dialog<Transaction> {

    private Payment payment;
    private Transfer transfer;

    private String type;
    private String account;

    private final String date;
    private final StringProperty amount = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();

    private StringProperty sender = new SimpleStringProperty();
    private StringProperty recipient = new SimpleStringProperty();
    private TextField senderField;
    private TextField recipientField;
    private TextField amountField;
    private TextField descriptionField;

    private RadioButton rb1;
    private RadioButton rb2;
    public TransactionDialog(String account){
        super();
        this.account = account;
        this.type = "transfer";
        this.date = new Date().toString();
        buildUI();
        setPropertyBinding();
        setResultConverter();
    }

    private void buildUI() {
        Pane pane = createGridPane();
        getDialogPane().setContent(pane);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button button = (Button) getDialogPane().lookupButton(ButtonType.OK);
		button.addEventFilter(ActionEvent.ACTION, new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                if (!validateDialog()) {
                    event.consume();
                }
                if (Objects.equals(type, "transfer")) {
                    if(sender.get().equals(account)){
                        transfer = new OutgoingTransfer(date, Double.parseDouble(amount.get()), description.get(), sender.get(),recipient.get());
                    }else{
                        transfer = new IncomingTransfer(date, Double.parseDouble(amount.get()), description.get(), sender.get(),recipient.get());
                    }

                }else if(Objects.equals(type, "payment")){
                    payment = new Payment(date, Double.parseDouble(amount.get()), description.get());
                }

            }

            private boolean validateDialog() {
                if (Objects.equals(type, "transfer")) {
                    return (!amountField.getText().isEmpty()) && (!descriptionField.getText().isEmpty()
                        &&!senderField.getText().isEmpty() && !recipientField.getText().isEmpty());

                }
                return (!amountField.getText().isEmpty()) && (!descriptionField.getText().isEmpty());


            }
        });

    }
    private void setPropertyBinding() {
		amountField.textProperty().bindBidirectional(amount);
		descriptionField.textProperty().bindBidirectional(description);
        recipientField.textProperty().bindBidirectional(recipient);
        senderField.textProperty().bindBidirectional(sender);

	}
    private void setResultConverter() {
		Callback<ButtonType, Transaction> TransactionResultConverter = param -> {
            if (param == ButtonType.OK) {
                if (Objects.equals(type, "transfer")) {
                    return transfer;
                }else {
                    return payment;
                }
            } else {
                return null;
            }
        };
		setResultConverter(TransactionResultConverter);
	}
    private Pane createGridPane() {
		VBox content = new VBox(10);

		Label typeLabel = new Label("Type");
        Label amountLabel = new Label("Amount");
        Label descriptionLabel = new Label("Description");
        Label senderLabel = new Label("Sender");
        Label recipientLabel = new Label("Recipient");


        ToggleGroup group = new ToggleGroup();

        rb1 = new RadioButton("Transfer");
        rb1.setToggleGroup(group);

        rb2 = new RadioButton("Payment");
        rb2.setToggleGroup(group);

        rb1.setOnAction(actionEvent -> {
            this.type = "transfer";
            buildUI();
            setPropertyBinding();
            setResultConverter();
            rb1.setSelected(true);
        });
        rb2.setOnAction(actionEvent -> {
            this.type = "payment";
            buildUI();
            setPropertyBinding();
            setResultConverter();
            rb2.setSelected(true);
        });

        group.selectToggle(rb1);

        amountField = new TextField();
        descriptionField = new TextField();
        senderField = new TextField();
        recipientField = new TextField();

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(5);
        grid.add(typeLabel, 0, 0);
        grid.add(rb1, 1, 0);
        grid.add(rb2, 2, 0);
		grid.add(amountLabel, 0, 1);
		grid.add(descriptionLabel, 0, 2);
		grid.add(amountField, 1, 1);
		GridPane.setHgrow(amountField, Priority.ALWAYS);
		grid.add(descriptionField, 1, 2);
		GridPane.setHgrow(descriptionField, Priority.ALWAYS);

        if(Objects.equals(type, "transfer")){
            grid.add(senderLabel, 0, 3);
            grid.add(senderField, 1, 3);
            GridPane.setHgrow(senderField, Priority.ALWAYS);
            grid.add(recipientLabel, 0, 4);
            grid.add(recipientField, 1, 4);
            GridPane.setHgrow(recipientField, Priority.ALWAYS);
        }

		content.getChildren().addAll(grid);

		return content;
	}
}
