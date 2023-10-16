package ui.javafx;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import models.Person;
import net.client.Client;
import net.data.Request;
import net.data.Response;
import net.data.ServerCommand;
import users.Identification;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Контейнеры для управления таблицей persons.
 */
public class PersonsTable {

    /**
     * Список персон.
     */
    private Collection<Person> persons;

    /**
     * Контейнер для показа фильра и таблицы.
     */
    private BorderPane pane;

    /**
     * Таблицы для показа persons.
     */
    private TableView table;

    /**
     * Для обновлений списка пользователей.
     */
    @Getter
    @Setter
    PersonsCanvas personsCanvas;

    /**
     * Контейнер полей для фильрации.
     */
    private HBox filter;

    /**
     * Подготовить главную панель.
     */
    public BorderPane prepare() {
        pane = new BorderPane();
        pane.setCenter(createPersonsTable());
        pane.setTop(createFilterPanel());
        return pane;
    }

    public BorderPane getPane() {
        return pane;
    }

    public Collection<Person> getPersons() {
        return persons;
    }

    /**
     * Обновить срисок persons.
     */
    public void reloadPersons() {
        reloadPersons(true);
    }

    public void reloadPersons(boolean withCanvas) {
        try {
            var command = ServerCommand.getAll();
            persons = Client.getClient().sendRequest(new Request(command)).getBody(Collection.class);
            filterAndShowPersons();
            if (withCanvas && personsCanvas != null) {
                personsCanvas.redraw();
            }
        } catch (Exception e) {
            UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_unknown").get());
        }
    }

    /**
     * Выполнить команду добавления / изменения person.
     */
    public void handleEditPerson(Person person) {
        handleEditPerson(person, null);
    }

    public void handleEditPerson(Person person, ServerCommand.TYPE commandType) {
        Dialog<Person> dialog = new PersonForm(person).createPersonDialog();
        Optional<Person> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get() instanceof Person) { // при нажатии на Close возвращается не объект Person
                Person updatedPerson = result.get();
                boolean isNew = updatedPerson.getId() == null || updatedPerson.getId().intValue() == 0;
                if (isNew) {
                    updatedPerson.setUserId(Identification.getIdentification().getUserInfo().getId());
                }
                try {
                    ServerCommand command =
                            commandType == ServerCommand.TYPE.add_if_max ? ServerCommand.addIfMax(updatedPerson) :
                            commandType == ServerCommand.TYPE.add_if_min ? ServerCommand.addIfMin(updatedPerson) :
                            isNew ? ServerCommand.add(updatedPerson) : ServerCommand.update(updatedPerson);
                    Response response = Client.getClient().sendRequest(new Request(command));
                    if ((commandType == ServerCommand.TYPE.add_if_max || commandType == ServerCommand.TYPE.add_if_min) && response.getCode() == Response.RESULT_ERROR) {
                        UiHelper.showAlert(Alert.AlertType.INFORMATION, UI.getI18n("form_info").get(), UI.getI18n("person_not_added_by_condition").get());
                    } else {
                        UiHelper.showAlert(Alert.AlertType.INFORMATION, UI.getI18n("form_info").get(), UI.getI18n(isNew ? "person_added" : "person_updated").get());
                        reloadPersons();
                    }
                } catch (Exception e) {
                    UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_unknown").get());
                }
            }
        }
    }

    public void handleRemoveGreater(Long height) {
        Person body = new Person();
        body.setHeight(height);
        body.setUserId(Identification.getIdentification().getUserInfo().getId());
        try {
            ServerCommand command = ServerCommand.removeGreater(body);
            Client.getClient().sendRequest(new Request(command));
            UiHelper.showAlert(Alert.AlertType.INFORMATION, UI.getI18n("form_info").get(), UI.getI18n("success").get());
            reloadPersons();
        } catch (Exception e) {
            UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_unknown").get());
        }
    }

    public void handleClear() {
        try {
            ServerCommand command = ServerCommand.clear(Identification.getIdentification().getUserInfo().getId());
            Client.getClient().sendRequest(new Request(command));
            UiHelper.showAlert(Alert.AlertType.INFORMATION, UI.getI18n("form_info").get(), UI.getI18n("success").get());
            reloadPersons();
        } catch (Exception e) {
            UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_unknown").get());
        }
    }

    public void handleAverageHeight() {
        try {
            ServerCommand command = ServerCommand.getAverageValue();
            Response response = Client.getClient().sendRequest(new Request(command));
            float averageValue = response.getBody(float.class);
            UiHelper.showAlert(Alert.AlertType.INFORMATION, UI.getI18n("form_info").get(), UI.getI18n("average_height").get() + ": " + averageValue);
        } catch (Exception e) {
            UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_unknown").get());
        }
    }

    /**
     * Создать панель с полями фильтра.
     */
    private HBox createFilterPanel() {
        filter = new HBox();
        List<String> colorValues = new ArrayList<>(Stream.of(models.Color.values()).map(models.Color::name).toList());
        colorValues.add(0, "");
        List<String> countryValues = new ArrayList<>(Stream.of(models.Country.values()).map(models.Country::name).toList());
        countryValues.add(0, "");
        filter.getChildren().add(prepareFilterTextField("id", 71));
        filter.getChildren().add(prepareFilterTextField("name", 150));
        filter.getChildren().add(prepareFilterTextField("height", 70));
        filter.getChildren().add(prepareFilterCombobox("nationality", 100, countryValues));
        filter.getChildren().add(prepareFilterCombobox("eyeColor", 100, colorValues));
        filter.getChildren().add(prepareFilterCombobox("hairColor", 100, colorValues));
        filter.getChildren().add(prepareFilterTextField("coordsX", 120));
        filter.getChildren().add(prepareFilterTextField("coordsY", 120));
        filter.getChildren().add(prepareFilterTextField("locationX", 120));
        filter.getChildren().add(prepareFilterTextField("locationY", 120));
        filter.getChildren().add(prepareFilterTextField("locationZ", 120));
        filter.getChildren().add(prepareFilterTextField("creationDate", 160));
        return filter;
    }

    /**
     * Подготовить текстовое поле фильтра.
     */
    private TextField prepareFilterTextField(String id, double prefWidth) {
        TextField field = new TextField();
        field.setPrefWidth(prefWidth);
        field.setUserData(id);
        field.textProperty().addListener(getFilterChangeListener(field));
        return field;
    }

    /**
     * Подготовить списочное поле фильтра.
     */
    private ComboBox prepareFilterCombobox(String id, double prefWidth, List<String> values) {
        ComboBox comboBox = new ComboBox();
        comboBox.setPrefWidth(prefWidth);
        comboBox.getItems().addAll(values);
        comboBox.setUserData(id);
        comboBox.valueProperty().addListener(getFilterChangeListener(comboBox));
        return comboBox;
    }

    /**
     * Событие изменения фильтра.
     */
    private ChangeListener<String> getFilterChangeListener(Node fld) {
        return (observable, oldValue, newValue) -> filterAndShowPersons();
    }

    /**
     * Подготовить таблицу со списком persons.
     */
    private TableView<Person> createPersonsTable() {
        table = new TableView<Person>();

        TableColumn idCol = prepareTableColumn("field_id","id", 70);
        TableColumn nameCol = prepareTableColumn("field_name","name", 150);
        TableColumn heightCol = prepareTableColumn("field_height","height", 70);
        TableColumn countryCol = prepareTableColumn("field_country","nationality", 100);
        TableColumn eyeColorCol = prepareTableColumn("field_eye_color","eyeColor", 100);
        TableColumn hairColorCol = prepareTableColumn("field_hair_color","hairColor", 100);
        TableColumn coordXCol = prepareTableColumn("field_coords_x", null, 120);
        // для чтения значения из вложенного объекта
        coordXCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, Long>, ObservableValue>() {
            public ObservableValue<Long> call(TableColumn.CellDataFeatures<Person, Long> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getCoordinates() != null ? p.getValue().getCoordinates().getX() : 0L);
            }
        });

        TableColumn coordYCol = prepareTableColumn("field_coords_y", null, 120);
        // для чтения значения из вложенного объекта
        coordYCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, String>, ObservableValue>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Person, String> p) {
                NumberFormat formatter = NumberFormat.getInstance();
                return new ReadOnlyObjectWrapper<>(p.getValue().getCoordinates() != null ? formatter.format(p.getValue().getCoordinates().getY()) : "");
            }
        });
        // для сортировки float значений
        coordYCol.setComparator((o1, o2) -> {
            NumberFormat format = NumberFormat.getInstance();
            Float n1, n2;
            try {
                n1 = format.parse((String)o1).floatValue();
            } catch (ParseException e) {
                n1 = Float.valueOf(0f);
            }
            try {
                n2 = format.parse((String)o2).floatValue();
            } catch (ParseException e) {
                n2 = Float.valueOf(0f);
            }
            return n1.compareTo(n2);
        });

        TableColumn locationXCol = prepareTableColumn("field_location_x", null, 120);
        // для чтения значения из вложенного объекта
        locationXCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, Integer>, ObservableValue>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Person, Integer> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getLocation() != null ? p.getValue().getLocation().getX() : 0);
            }
        });

        TableColumn locationYCol = prepareTableColumn("field_location_y", null, 120);
        // для чтения значения из вложенного объекта
        locationYCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, Long>, ObservableValue>() {
            public ObservableValue<Long> call(TableColumn.CellDataFeatures<Person, Long> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getLocation() != null ? p.getValue().getLocation().getY() : 0L);
            }
        });

        TableColumn locationZCol = prepareTableColumn("field_location_z", null, 120);
        // для чтения значения из вложенного объекта
        locationZCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, Integer>, ObservableValue>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Person, Integer> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getLocation() != null ? p.getValue().getLocation().getZ() : 0);
            }
        });

        TableColumn creationDateCol = prepareTableColumn("field_creation_date", null, 160);
        creationDateCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, String>, ObservableValue>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Person, String> p) {
                String value = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(p.getValue().getCreationDate().toLocalDate());
                return new ReadOnlyObjectWrapper<>(value);
            }
        });
        creationDateCol.setComparator((o1, o2) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
            LocalDate d1 = LocalDate.parse((String)o1, formatter);
            LocalDate d2 = LocalDate.parse((String)o2, formatter);
            return d1.compareTo(d2);
        });

        table.getColumns().addAll(idCol, nameCol, heightCol, countryCol, eyeColorCol, hairColorCol, coordXCol, coordYCol, locationXCol, locationYCol, locationZCol, creationDateCol);
        table.getColumns().add(getDeleteButtonCol());

        table.setItems(FXCollections.observableArrayList());

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() != 2) return; // не двойной клик
            Person person = (Person)table.getSelectionModel().getSelectedItem();
            if (person == null) return;
            /* Вместо диалога об ошибке форма person показана в readonly режиме
            if (!person.getUserId().equals(Identification.getIdentification().getUserInfo().getId())) {
                UiHelper.showAlert(Alert.AlertType.ERROR, table.getScene().getWindow(), UI.getI18n("form_error").get(), UI.getI18n("person_edit_denied").get());
            } else {
            */
                handleEditPerson(person);
            /*
            }
            */
        });

        return table;
    }

    /**
     * Подготовить столбец таблицы.
     */
    private static TableColumn prepareTableColumn(String i18nKey, String personField, double prefWidth) {
        TableColumn idCol = new TableColumn();
        idCol.textProperty().bind(UI.getI18n(i18nKey));
        idCol.setResizable(false);
        idCol.setReorderable(false);
        idCol.setPrefWidth(prefWidth);
        if (personField != null) {
            idCol.setCellValueFactory(new PropertyValueFactory<>(personField));
        }
        return idCol;
    }

    /**
     * Добавить кнопку удаления записи из таблицы.
     */
    private TableColumn getDeleteButtonCol() {
        TableColumn<Person, Void> colBtn = new TableColumn("");
        colBtn.setMinWidth(40);

        Callback<TableColumn<Person, Void>, TableCell<Person, Void>> cellFactory = new Callback<TableColumn<Person, Void>, TableCell<Person, Void>>() {
            @Override
            public TableCell<Person, Void> call(final TableColumn<Person, Void> param) {
                final TableCell<Person, Void> cell = new TableCell<>() {

                    private final Button btn = UiHelper.getImageButton("/delete.png", "button_delete");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Person person = getTableView().getItems().get(getIndex());
                            UiHelper.confirm(UI.getI18n("form_question").get(), UI.getI18n("confirm_delete").get() + " " + person.getName() + "?",
                                    () -> {
                                        try {
                                            var command = ServerCommand.remove(person.getId());
                                            Client.getClient().sendRequest(new Request(command));
                                            reloadPersons();
                                            UiHelper.showAlert(Alert.AlertType.INFORMATION, UI.getI18n("form_info").get(), UI.getI18n("person_deleted").get());
                                        } catch (Exception e) {
                                            UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_unknown").get());
                                        }
                                    }
                            );
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (this.getTableRow() == null) {
                            setGraphic(null);
                        } else {
                            Person person = this.getTableRow().getItem();
                            if (empty || person == null || !person.getUserId().equals(Identification.getIdentification().getUserInfo().getId())) {
                                setGraphic(null);
                            } else {
                                setGraphic(btn);
                            }
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        colBtn.setSortable(false);
        return colBtn;
    }

    /**
     * Выполнить фильтрацию объектов в списке согласно установкам в полях фильтра.
     */
    public void filterAndShowPersons() {
        ObservableList<Person> data = table.getItems();
        data.clear();
        List<Person> filteredPersons = persons == null ? Collections.emptyList() :
                persons.stream().filter(person -> {
                    for (Node filterField : filter.getChildren()) {
                        Control control = (Control)filterField;
                        String key = (String)control.getUserData();
                        String filterValue = control instanceof TextField ? ((TextField)control).getText() : (String)((ComboBox)control).getValue();
                        if (filterValue == null || filterValue.trim().equals("")) continue;
                        String personValue = person.getStringValue(key);
                        if (personValue == null || personValue.trim().equals("")) return false;
                        if (!personValue.toLowerCase().contains(filterValue.toLowerCase())) return false;
                    }
                    return true;
                }).collect(Collectors.toList());
        data.addAll(filteredPersons);
    }

}
