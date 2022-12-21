package ui;

import bank.*;
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

public class AccountDialog extends Dialog<String> {


    private final StringProperty account = new SimpleStringProperty();
    private TextField accountField;

    public AccountDialog(){
        super();
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

            }

            private boolean validateDialog() {
                return (!accountField.getText().isEmpty());
            }
        });

    }
    private void setPropertyBinding() {
		accountField.textProperty().bindBidirectional(account);

	}
    private void setResultConverter() {
		Callback<ButtonType, String> personResultConverter = param -> account.get();
		setResultConverter(personResultConverter);
	}
    private Pane createGridPane() {
		VBox content = new VBox(10);

		Label accountLabel = new Label("Account Name");

        accountField = new TextField();

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(5);
        grid.add(accountLabel, 0, 3);
        grid.add(accountField, 1, 3);
		GridPane.setHgrow(accountField, Priority.ALWAYS);

		content.getChildren().addAll(grid);

		return content;
	}
}
