package ui.javafx;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import models.Container;
import models.Coordinates;
import models.Location;
import models.Person;
import users.Identification;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class PersonForm {

    private Person person;

    public PersonForm(Person person) {
        this.person = person;
    }

    /**
     * Создать диалог для добавления / изменения person.
     */
    public Dialog<Person> createPersonDialog() {

        boolean owner = person.getId() == null || person.getId().intValue() == 0 || person.getUserId().equals(Identification.getIdentification().getUserInfo().getId());

        Dialog<Person> dialog = new Dialog<>();
        boolean isNew = person.getId() == null || person.getId().intValue() == 0;
        dialog.setTitle(UI.getI18n(!owner ? "person_view_title" : isNew ? "person_add_title" : "person_edit_title").get());
        dialog.setHeaderText(UI.getI18n(!owner ? "person_view_intro" : isNew ? "person_add_intro" : "person_edit_intro").get());
        dialog.setResizable(false);

        GridPane grid = UiHelper.createGridPane();

        Label lblName = UiHelper.addFieldToGridPane(grid,"field_name", 0, 0);
        lblName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextInputControl fldName = (TextInputControl)lblName.getLabelFor();
        fldName.setDisable(!owner);
        fldName.setText(person.getName());

        Label lblHeight = UiHelper.addFieldToGridPane(grid,"field_height", 1, 0);
        lblHeight.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextInputControl fldHeight = (TextInputControl)lblHeight.getLabelFor();
        fldHeight.textProperty().addListener(getNumberChangeListener(fldHeight, Long::parseLong));
        fldHeight.setDisable(!owner);
        fldHeight.setText(person.getHeight() > 0L ? String.valueOf(person.getHeight()) : "");

        String[] colorValues = Stream.of(models.Color.values()).map(models.Color::name).toArray(size -> new String[size]);
        ComboBox comboEyeColor = UiHelper.addComboBoxToGridPane(grid,"field_eye_color", 2, 0, colorValues);
        comboEyeColor.setDisable(!owner);
        comboEyeColor.setValue(person.getEyeColor() != null ? person.getEyeColor() : "");
        ComboBox comboHairColor = UiHelper.addComboBoxToGridPane(grid,"field_hair_color", 3, 0, colorValues);
        comboHairColor.setDisable(!owner);
        comboHairColor.setValue(person.getHairColor() != null ? person.getHairColor() : "");

        String[] countryValues = Stream.of(models.Country.values()).map(models.Country::name).toArray(size -> new String[size]);
        ComboBox comboNationality = UiHelper.addComboBoxToGridPane(grid,"field_country", 4, 0, countryValues);
        comboNationality.setDisable(!owner);
        comboNationality.setValue(person.getNationality() != null ? person.getNationality() : "");

        grid.add(new Separator(), 0, 5, 2, 1);

        Label lblCoordX = UiHelper.addFieldToGridPane(grid,"field_coords_x", 6, 0);
        TextInputControl fldCoordX = (TextInputControl)lblCoordX.getLabelFor();
        fldCoordX.textProperty().addListener(getNumberChangeListener(fldCoordX, Long::parseLong));
        fldCoordX.setDisable(!owner);
        fldCoordX.setText(person.getCoordinates() != null ? String.valueOf(person.getCoordinates().getX()) : "");

        Label lblCoordY = UiHelper.addFieldToGridPane(grid,"field_coords_y", 7, 0);
        TextInputControl fldCoordY = (TextInputControl)lblCoordY.getLabelFor();
        // fldCoordY.textProperty().addListener(getFloatChangeListener(fldCoordY));
        fldCoordY.textProperty().addListener(getNumberChangeListener(fldCoordY, Float::parseFloat));
        fldCoordY.setDisable(!owner);
        fldCoordY.setText(person.getCoordinates() != null ? String.valueOf(person.getCoordinates().getY()) : "");

        grid.add(new Separator(), 0, 8, 2, 1);

        Label lblLocationX = UiHelper.addFieldToGridPane(grid,"field_location_x", 9, 0);
        TextInputControl fldLocationX = (TextInputControl)lblLocationX.getLabelFor();
        fldLocationX.textProperty().addListener(getNumberChangeListener(fldLocationX, Integer::parseInt));
        fldLocationX.setDisable(!owner);
        fldLocationX.setText(person.getLocation() != null ? String.valueOf(person.getLocation().getX()) : "");

        Label lblLocationY = UiHelper.addFieldToGridPane(grid,"field_location_y", 10, 0);
        TextInputControl fldLocationY = (TextInputControl)lblLocationY.getLabelFor();
        fldLocationY.textProperty().addListener(getNumberChangeListener(fldLocationY, Integer::parseInt));
        fldLocationY.setDisable(!owner);
        fldLocationY.setText(person.getLocation() != null ? String.valueOf(person.getLocation().getY()) : "");

        Label lblLocationZ = UiHelper.addFieldToGridPane(grid,"field_location_z", 11, 0);
        TextInputControl fldLocationZ = (TextInputControl)lblLocationZ.getLabelFor();
        fldLocationZ.textProperty().addListener(getNumberChangeListener(fldLocationZ, Integer::parseInt));
        fldLocationZ.setDisable(!owner);
        fldLocationZ.setText(person.getLocation() != null ? String.valueOf(person.getLocation().getZ()) : "");

        if (person.getCreationDate() != null) {
            Label lblCreateDate = UiHelper.addFieldToGridPane(grid,"field_creation_date", 12, 0);
            TextInputControl fldCreateDate = (TextInputControl)lblCreateDate.getLabelFor();
            fldCreateDate.setDisable(true);
            fldCreateDate.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(person.getCreationDate().toLocalDate()));
        }
        dialog.getDialogPane().setContent(grid);

        if (owner) {
            ButtonType buttonTypeOk = new ButtonType(UI.getI18n("continue").get(), ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
            dialog.setResultConverter(toPerson(person, lblName, lblHeight, comboEyeColor, comboHairColor, comboNationality, lblCoordX, lblCoordY, fldLocationX, fldLocationY, fldLocationZ, buttonTypeOk));
        } else {
            ButtonType buttonTypeClose = new ButtonType(UI.getI18n("close").get(), ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(buttonTypeClose);
        }
        return dialog;
    }

    /**
     * Заполнить person значениями полей из диалога.
     */
    private Callback<ButtonType, Person> toPerson(Person person, Label lblName, Label lblHeight, ComboBox comboEyeColor, ComboBox comboHairColor, ComboBox comboNationality, Label lblCoordX, Label lblCoordY, TextInputControl fldLocationX, TextInputControl fldLocationY, TextInputControl fldLocationZ, ButtonType buttonTypeOk) {
        return (buttonType) -> {
            if (buttonType == buttonTypeOk) {

                Container<Boolean> error = new Container<>(false);

                TextInputControl fldName = (TextInputControl)lblName.getLabelFor();
                String name = fldName.getText();
                if (name != null && !name.trim().equals("")) {
                    resetFieldError(lblName, fldName);
                } else {
                    setFieldError(lblName, fldName, UI.getI18n("required_field").get());
                    error.setValue(true);
                }
                TextInputControl fldHeight = (TextInputControl)lblHeight.getLabelFor();
                long height = getNumberFieldValue(fldHeight.getText(), Long::parseLong,0L, /*false, true, false,*/ () -> {
                    resetFieldError(lblHeight, fldHeight);
                }, () -> {
                    setFieldError(lblHeight, fldHeight, UI.getI18n("required_field").get());
                    error.setValue(true);
                });
                Coordinates coordinates = person.getCoordinates();
                TextInputControl fldCoordX = (TextInputControl)lblCoordX.getLabelFor();
                TextInputControl fldCoordY = (TextInputControl)lblCoordY.getLabelFor();
                long coordX = getNumberFieldValue(fldCoordX.getText(), Long::parseLong, 0L /*, false, true, false*/);
                if (coordX <= Coordinates.MAX_X_INCL) {
                    resetFieldError(lblCoordX, fldCoordX);
                } else {
                    setFieldError(lblCoordX, fldCoordX, UI.getI18n("max_value_incl").get() + " " + Coordinates.MAX_X_INCL);
                    error.setValue(true);
                }
                float coordY = getNumberFieldValue(fldCoordY.getText(), Float::parseFloat, 0F /*,true, false, false*/);
                if (coordY > Coordinates.MIN_Y_EXCL) {
                    resetFieldError(lblCoordY, fldCoordY);
                } else {
                    setFieldError(lblCoordY, fldCoordY, UI.getI18n("min_value_excl").get() + " " + Coordinates.MIN_Y_EXCL);
                    error.setValue(true);
                }
                if (error.getValue()) {
                    // требуется выкинуть исключения для избежания закрытия диалога.
                    throw new RuntimeException("validation error");
                }
                person.setName(name);
                person.setHeight(height);
                if (coordinates == null) {
                    person.setCoordinates(new Coordinates(coordX, coordY));
                } else {
                    coordinates.setX(coordX);
                    coordinates.setY(coordY);
                }
                Object eyeColor = comboEyeColor.getValue();
                person.setEyeColor(eyeColor != null && !eyeColor.equals("") ? models.Color.valueOf(eyeColor.toString()) : null);
                Object hairColor = comboHairColor.getValue();
                person.setHairColor(hairColor != null && !hairColor.equals("") ? models.Color.valueOf(hairColor.toString()) : null);
                Object nationality = comboNationality.getValue();
                person.setNationality(nationality != null && !nationality.equals("")  ? models.Country.valueOf(nationality.toString()) : null);
                Location location = person.getLocation();
                int locX = getNumberFieldValue(fldLocationX.getText(), Integer::parseInt, 0 /*, false, false, true*/);
                long locY = getNumberFieldValue(fldLocationY.getText(), Long::parseLong, 0L /*, false, true, false*/);
                int locZ = getNumberFieldValue(fldLocationZ.getText(), Integer::parseInt, 0 /*, false, false, true*/);
                if (location == null) {
                    person.setLocation(new Location(locX, locY, locZ));
                } else {
                    location.setX(locX);
                    location.setY(locY);
                    location.setZ(locZ);
                }
                return person;
            }
            return null;
        };
    }

    /**
     * Получить не String значение из текстового поля.
     */
    private <T> T getNumberFieldValue(String strValue, Function<String, T> converter, T defaultVal /*, boolean isFloat, boolean isLong, boolean isInteger*/) {
        return getNumberFieldValue(strValue, converter, defaultVal, /*isFloat, isLong, isInteger,*/ null, null);
    }

    /**
     * Получить не String значение из текстового поля, с выполнением действий в случае успешного, либо ошибочного получения.
     */
    private <T> T getNumberFieldValue(String strValue, Function<String, T> converter, T defaultVal, /* boolean isFloat, boolean isLong, boolean isInteger,*/ Runnable onSuccess, Runnable onError) {
        T value;
        // Вариант с NumberFormatyt не работает корректно при переключении между языками
        // NumberFormat format = NumberFormat.getInstance();
        try {
            /*
            value = (T)format.parse(strValue);
            if (isFloat) {
                if (value instanceof Double) {
                    value = (T) (Float.valueOf(((Double) value).floatValue()));
                } else if (value instanceof Long) {
                    value = (T) (Float.valueOf(((Long) value).floatValue()));
                } else if (value instanceof Integer) {
                    value = (T) (Float.valueOf(((Integer) value).floatValue()));
                }
            } else if (isLong) {
                if (value instanceof Double) {
                    value = (T) (Long.valueOf(((Double) value).longValue()));
                } else if (value instanceof Float) {
                    value = (T) (Long.valueOf(((Float) value).longValue()));
                } else if (value instanceof Integer) {
                    value = (T) (Long.valueOf(((Integer) value).longValue()));
                }
            } else if (isInteger) {
                if (value instanceof Double) {
                    value = (T) (Integer.valueOf(((Double) value).intValue()));
                } else if (value instanceof Float) {
                    value = (T) (Integer.valueOf(((Float) value).intValue()));
                } else if (value instanceof Long) {
                    value = (T) (Integer.valueOf(((Long) value).intValue()));
                }
            }
            */
            value = converter.apply(strValue);
            if (onSuccess != null) {
                onSuccess.run();
            }
        } catch (Exception e) {
            value = defaultVal;
            if (onError != null) {
                onError.run();
            }
        }
        return value;
    }

    @Deprecated
    private static final String float_regexp = "^[\\+\\-]{0,1}[0-9]+[\\.]{0,1}[0-9]*$";

    /**
     * Проверять ввод в поле для ввода float значения, не допускать неправильного ввода.
     *
     * @deprecated использовать getNumberChangeListener
     */
    @Deprecated
    private static ChangeListener<String> getFloatChangeListener(TextInputControl fld) {
        return (observable, oldValue, newValue) -> {
            if (newValue.matches(float_regexp)) return;
            fld.setText(oldValue != null && oldValue.matches(float_regexp) ? oldValue : "");
        };
    }

    /**
     * Проверять ввод в поле для ввода целочисленного или дробного значения, не допускать неправильного ввода.
     */
    private static ChangeListener<String> getNumberChangeListener(TextInputControl fld, Consumer<String> test) {
        return (observable, oldValue, newValue) -> {
            if (newValue == null || newValue.equals("") || newValue.equals("-") || newValue.equals("-.")) return;
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
        };
    }

    private static final String normal_color = "#000000";
    private static final String error_color = "#FF4444";

    /**
     * Обозначить текстовое поле, как содержащее ошибочное значение.
     */
    private void setFieldError(Label label, TextInputControl field, String tooltipText) {
        label.setTextFill(Color.valueOf(error_color));
        if (tooltipText != null) {
            Tooltip tooltip = UiHelper.prepareTooltip();
            tooltip.setText(tooltipText);
            field.setTooltip(tooltip);
        }
        field.setBorder(new Border(new BorderStroke(Color.valueOf(error_color), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    /**
     * Убрать обозначение текстового поля, как содержащее ошибочное значение.
     */
    private void resetFieldError(Label label, TextInputControl field) {
        label.setTextFill(Color.valueOf(normal_color));
        field.setTooltip(null);
        field.setBorder(null);
    }

}
