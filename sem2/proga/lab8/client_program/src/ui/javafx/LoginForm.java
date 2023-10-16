package ui.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import users.Identification;

public class LoginForm {

    Pane pane;

    /**
     * Подготовить состав формы логина / регистрации.
     */
    public Pane prepare() {
        TabPane tabPane = new TabPane();
        Tab loginTab = new Tab();
        loginTab.setClosable(false);
        loginTab.textProperty().bind(UI.getI18n("tab_login"));
        GridPane loginPane = UiHelper.createGridPane();
        addLoginControls(loginPane);
        loginTab.setContent(loginPane);
        Tab registerTab = new Tab();
        registerTab.setClosable(false);
        registerTab.textProperty().bind(UI.getI18n("tab_register"));
        GridPane registerPane = UiHelper.createGridPane();
        addRegisterControls(registerPane);
        registerTab.setContent(registerPane);
        tabPane.getTabs().add(loginTab);
        tabPane.getTabs().add(registerTab);
        tabPane.getSelectionModel().getSelectedItem();
        VBox pane = new VBox(tabPane, UiHelper.getI18iSelector(null));
        pane.setAlignment(Pos.TOP_CENTER);
        return (this.pane = pane);
    }

    /**
     * Создать компоненты для формы логина.
     */
    private void addLoginControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label();
        headerLabel.textProperty().bind(UI.getI18n("tab_login"));
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Label loginLabel = UiHelper.addFieldToGridPane(gridPane, "field_login", 1, 0, TextField::new);
        Label passwordLabel = UiHelper.addFieldToGridPane(gridPane, "field_password", 2, 0, PasswordField::new);
        TextInputControl loginField = (TextInputControl)loginLabel.getLabelFor();
        TextInputControl passwordField = (TextInputControl)passwordLabel.getLabelFor();
        Button submitButton = UiHelper.addButtonToGridPane(gridPane, "continue", 4, 0, 1, 2);

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(loginField.getText().isEmpty() || loginField.getText().trim().equals("")) {
                    UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_missing_login").get());
                    return;
                }
                if(passwordField.getText().isEmpty() || passwordField.getText().trim().length() < 5) {
                    UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_missing_password").get());
                    return;
                }
                boolean result = Identification.getIdentification().loginUser(loginField.getText(), passwordField.getText());
                if (result) {
                    // UiHelper.showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), UI.getI18n("form_info").get(), UI.getI18n("welcome").get() + loginField.getText() + "!");
                    UI.openMainForm();
                } else {
                    UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("user_not_found").get());
                }
            }
        });
    }

    /**
     * Создать компоненты для формы регистрации.
     */
    private void addRegisterControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label();
        headerLabel.textProperty().bind(UI.getI18n("tab_register_title"));
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Label loginLabel = UiHelper.addFieldToGridPane(gridPane, "field_login", 1, 0);
        Label passwordLabel = UiHelper.addFieldToGridPane(gridPane, "field_password", 2, 0, PasswordField::new);
        TextInputControl loginField = (TextInputControl)loginLabel.getLabelFor();
        TextInputControl passwordField = (TextInputControl)passwordLabel.getLabelFor();
        Button submitButton = UiHelper.addButtonToGridPane(gridPane, "continue", 4, 0, 1, 2);

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(loginField.getText().isEmpty() || loginField.getText().trim().equals("")) {
                    UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_missing_login").get());
                    return;
                }
                if(passwordField.getText().isEmpty() || passwordField.getText().trim().length() < 5) {
                    UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_missing_password").get());
                    return;
                }
                try {
                    boolean result = Identification.getIdentification().checkUserNameIsUnique(loginField.getText());
                    if (!result) {
                        UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("user_already_exists").get());
                        return;
                    }
                } catch (Exception exc) {
                    UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("register_error").get());
                    return;
                }
                boolean result = Identification.getIdentification().registerUser(loginField.getText(), passwordField.getText());
                if (result) {
                    // UiHelper.showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), UI.getI18n("form_info").get(), UI.getI18n("welcome_new").get() + loginField.getText());
                    UI.openMainForm();
                } else {
                    UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("register_error").get());
                }
            }
        });
    }

}
