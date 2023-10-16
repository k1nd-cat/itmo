package ui.javafx;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Общие методы отрисовки.
 */
public class UiHelper {

    /**
     * Добавить поле в gridPane по заданным координатам.
     *
     * @return Label, т.к. Label также сожержит само поле.
     */
    public static Label addFieldToGridPane(GridPane gridPane, String i18nKey, int row, int col) {
        return addFieldToGridPane(gridPane, i18nKey, row, col, TextField::new);
    }

    /**
     * Добавить поле в gridPane по заданным координатам.
     *
     * @return Label, т.к. Label также сожержит само поле.
     */
    public static Label addFieldToGridPane(GridPane gridPane, String i18nKey, int row, int col, Supplier<TextInputControl> constructor) {
        Label label = new Label();
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        label.textProperty().bind(UI.getI18n(i18nKey));
        gridPane.add(label, col, row);
        TextInputControl field = constructor.get();
        field.setPrefHeight(40);
        label.setLabelFor(field);
        gridPane.add(field, col + 1,row);
        return label;
    }

    /**
     * Добавить поле выбора в gridPane по заданным координатам.
     */
    public static ComboBox addComboBoxToGridPane(GridPane gridPane, String i18nKey, int row, int col, String ... values) {
        Label label = new Label();
        label.textProperty().bind(UI.getI18n(i18nKey));
        gridPane.add(label, col, row);
        ComboBox comboBox = new ComboBox();
        comboBox.setPrefHeight(40);
        comboBox.getItems().addAll(values);
        comboBox.setEditable(true);
        label.setLabelFor(comboBox);
        gridPane.add(comboBox, col + 1,row);
        return comboBox;
    }

    /**
     * Добавить кнопку в gridPane по заданным координатам.
     */
    public static Button addButtonToGridPane(GridPane gridPane, String i18nKey, int row, int col, int rowSpan, int colSpan) {
        Button button = new Button();
        button.textProperty().bind(UI.getI18n(i18nKey));
        button.setPrefHeight(40);
        button.setDefaultButton(true);
        button.setPrefWidth(100);
        gridPane.add(button, col, row, colSpan, rowSpan);
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setMargin(button, new Insets(20, 0,20,0));
        return button;
    }

    /**
     * Создать кнопку с картинкой внутри и подсказкой.
     */
    public static Button getImageButton(String image, String tooltipKey) {
        ImageView imageView = new ImageView(UiHelper.class.getResource(image).toExternalForm());
        Button button = new Button();
        button.setTooltip(prepareTooltip(tooltipKey));
        button.setMaxHeight(24);
        button.setMaxWidth(24);
        button.setGraphic(imageView);
        return button;
    }

    /**
     * Создать всплывающую подсказку.
     */
    public static Tooltip prepareTooltip() {
        return prepareTooltip(null);
    }

    public static Tooltip prepareTooltip(String tooltipKey) {
        Tooltip tooltip = new Tooltip();
        tooltip.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        if (tooltipKey != null) {
            tooltip.textProperty().bind(UI.getI18n(tooltipKey));
        }
        tooltip.setShowDelay(Duration.ZERO);
        return tooltip;
    }

    /**
     * Подготовить стандартный toolbar для кнопок.
     */
    public static HBox prepareToolbar() {
        HBox toolbar = new HBox();
        toolbar.setAlignment(Pos.CENTER_RIGHT);
        toolbar.setMinHeight(60);
        toolbar.setSpacing(20);
        toolbar.setPadding(new Insets(0, 40, 0, 0));
        return toolbar;
    }

    /**
     * Создать поле для выбора текущего языка.
     */
    public static ComboBox getI18iSelector(Runnable onSuccess) {
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add("русский");
        comboBox.getItems().add("english");
        comboBox.getItems().add("español");
        comboBox.getItems().add("português");
        comboBox.setEditable(true);
        comboBox.setOnAction((event) -> {
            int selectedIndex = comboBox.getSelectionModel().getSelectedIndex();
            Locale locale =
                selectedIndex == 0 ? Locale.forLanguageTag("RU") :
                selectedIndex == 1 ? Locale.forLanguageTag("EN") :
                selectedIndex == 2 ? Locale.forLanguageTag("es-BR") :
                selectedIndex == 3 ? Locale.forLanguageTag("pt-BR") :
                Locale.getDefault();
            UI.i18nManager.setResources(locale);
            UI.i18nManager.lang = (String)comboBox.getValue();
            if (onSuccess != null) {
                onSuccess.run();
            }
        });
        comboBox.setValue(UI.i18nManager.lang);
        return comboBox;
    }

    /**
     * Показать сообщение (информация или ошибка).
     */
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(UI.mainScene().getWindow());
        alert.show();
    }

    /**
     * Показать диалог подтверждения декйствия.
     */
    public static void confirm(String title, String message, Runnable onSuccess) {
        confirm(title, message, onSuccess, null);
    }

    public static void confirm(String title, String message, Runnable onSuccess, Runnable onCancel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        ButtonType buttonContinue = new ButtonType(UI.getI18n("button_continue").get());
        ButtonType buttonCancel = new ButtonType(UI.getI18n("button_cancel").get());
        alert.getButtonTypes().setAll(buttonContinue, buttonCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonContinue) {
            onSuccess.run();
        } else if (onCancel != null) {
            onCancel.run();
        }
    }

    /**
     * Создать GridPane (применяется на форме логина / регистрании, при редактировании person).
     */
    public static GridPane createGridPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

}
