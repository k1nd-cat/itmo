package ui.javafx;

import command_management.CommandManager;
import commands.ExecuteScript;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import lombok.Getter;
import models.Color;
import models.Person;
import net.client.Client;
import net.data.Request;
import net.data.Response;
import net.data.ServerCommand;
import users.Identification;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MainForm {

    /**
     * Основная панель главной формы.
     */
    @Getter
    BorderPane pane;

    /**
     * Контейнеры для управления таблицей persons.
     */
    PersonsTable personsTable;

    /**
     * Контейнеры для управления графическим размещением persons.
     */
    PersonsCanvas personsCanvas;

    /**
     * Подготовить состав главной формы.
     */
    public Pane prepare() {
        BorderPane pane = new BorderPane();
        personsTable = new PersonsTable();
        pane.setTop(createTopToolbar());
        pane.setCenter(personsTable.prepare());
        pane.setBottom(createBottomToolbar());
        personsCanvas = new PersonsCanvas(personsTable);
        personsTable.setPersonsCanvas(personsCanvas);
        personsCanvas.prepare();
        personsTable.reloadPersons();
        return (this.pane = pane);
    }

    /**
     * Верхний toolbar.
     */
    private HBox createTopToolbar() {
        HBox topToolbar = UiHelper.prepareToolbar();
        Label userLabel = new Label(Identification.getIdentification().getUserInfo().getUsername());
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        Button exit = UiHelper.getImageButton("/exit.png", "button_exit");
        exit.setOnAction(event -> UiHelper.confirm(UI.getI18n("form_question").get(), UI.getI18n("confirm_exit").get(), () -> UI.exit()));
        topToolbar.getChildren().addAll(UiHelper.getI18iSelector(personsTable::filterAndShowPersons), userLabel, exit);
        return topToolbar;
    }

    /**
     * Нижний toolbar.
     */
    private HBox createBottomToolbar() {
        Button btnAdd = UiHelper.getImageButton("/add.png", "button_add");
        btnAdd.setOnAction(event -> personsTable.handleEditPerson(new Person()));

        Button btnAddIfMax = UiHelper.getImageButton("/addMax.png", "button_add_max");
        btnAddIfMax.setOnAction(event -> personsTable.handleEditPerson(new Person(), ServerCommand.TYPE.add_if_max));

        Button btnAddIfMin = UiHelper.getImageButton("/addMin.png", "button_add_min");
        btnAddIfMin.setOnAction(event -> personsTable.handleEditPerson(new Person(), ServerCommand.TYPE.add_if_min));

        Button btnAverageHeight = UiHelper.getImageButton("/average_height.png", "average_height");
        btnAverageHeight.setOnAction(event -> personsTable.handleAverageHeight());

        Button btnRemoveGreater = UiHelper.getImageButton("/removeGreater.png", "remove_greater");
        btnRemoveGreater.setDisable(true);
        TextInputControl fldHeight = new TextField();
        fldHeight.setPrefHeight(30);
        fldHeight.textProperty().addListener(getNumberChangeListener(fldHeight, btnRemoveGreater, Long::parseLong));
        btnRemoveGreater.setOnAction(event -> UiHelper.confirm(
                UI.getI18n("form_question").get(), UI.getI18n("confirm_delete_many").get(),
                () -> personsTable.handleRemoveGreater(Long.valueOf(fldHeight.getText()))
        ));

        Button btnClear = UiHelper.getImageButton("/delete.png", "clear");
        btnClear.setOnAction(event -> UiHelper.confirm(UI.getI18n("form_question").get(), UI.getI18n("confirm_delete_many").get(), () -> personsTable.handleClear()));

        Button btnTable = UiHelper.getImageButton("/table.png", "button_table");
        Button btnImage = UiHelper.getImageButton("/image.png", "button_image");
        btnTable.setDisable(true);
        btnTable.setOnAction(event -> {
            btnTable.setDisable(true);
            btnImage.setDisable(false);
            pane.setCenter(personsTable.getPane());
        });
        btnImage.setOnAction(event -> {
            btnTable.setDisable(false);
            btnImage.setDisable(true);
            pane.setCenter(personsCanvas.refreshAndGetCanvas(pane.getWidth(), pane.getHeight() - 120));
        });

        Button btnScript = UiHelper.getImageButton("/script.png", "choose_script");
        FileChooser fileChooser = new FileChooser();
        btnScript.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(UI.mainScene().getWindow());
            if (file != null) {
                new ExecuteScript().execute(Arrays.asList("execute", file.getAbsoluteFile().getAbsolutePath()), null, new CommandManager());
                personsTable.reloadPersons();
            }
        });

        Button btnInfo = UiHelper.getImageButton("/info.png", "button_info");
        btnInfo.setOnAction(event -> {
            try {
                var command = ServerCommand.info(Identification.getIdentification().getUserInfo().getId());
                Response response = Client.getClient().sendRequest(new Request(command));
                System.out.println("INFO from server: " + response.getBody(String.class));
                UiHelper.showAlert(Alert.AlertType.INFORMATION, UI.getI18n("form_info").get(), response.getBody(String.class));
            } catch (Exception exc) {
                UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_unknown").get());
            }
        });

        Button btnHair = UiHelper.getImageButton("/hair.png", "hair_info");
        btnHair.setOnAction(event -> {
            String info = personsTable.getPersons().stream().filter(p -> p.getHairColor() != null).map(Person::getHairColor).distinct().sorted().map(Color::name).collect(Collectors.joining(", "));
            UiHelper.showAlert(Alert.AlertType.INFORMATION, UI.getI18n("form_info").get(), info);
        });

        Button btnLocation = UiHelper.getImageButton("/location.png", "location_min");
        btnLocation.setOnAction(event -> {
            Person person = personsTable.getPersons().stream().filter(p -> p.getLocation() != null).min(Comparator.comparing(Person::getLocationSize)).orElse(null);
            if (person == null) {
                UiHelper.showAlert(Alert.AlertType.INFORMATION, UI.getI18n("form_info").get(), UI.getI18n("person_not_found").get());
            } else {
                personsTable.handleEditPerson(person);
            }
        });

        HBox bottomToolbar = UiHelper.prepareToolbar();
        bottomToolbar.getChildren().addAll(btnHair, btnLocation, btnInfo, btnScript, btnAverageHeight, fldHeight, btnRemoveGreater, btnClear, btnAddIfMin, btnAddIfMax, btnTable, btnImage, btnAdd);
        return bottomToolbar;
    }

    private static ChangeListener<String> getNumberChangeListener(TextInputControl fld, Button btnRemoveGreater, Consumer<String> test) {
        return (observable, oldValue, newValue) -> {
            btnRemoveGreater.setDisable(true);
            if (newValue == null || newValue.equals("")) return;
            // обработать числа типа 100,000,000.00
            // NumberFormat format = NumberFormat.getInstance();
            try {
                // format.parse(newValue);
                test.accept(newValue);
            } catch (NumberFormatException e) {
                try {
                    // format.parse(oldValue);
                    test.accept(oldValue);
                    fld.setText(oldValue);
                } catch (NumberFormatException e1) {
                    fld.setText("");
                }
            }
            btnRemoveGreater.setDisable(fld.getText() == null || fld.getText().equals(""));
        };
    }

}