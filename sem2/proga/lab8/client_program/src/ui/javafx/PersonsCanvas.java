package ui.javafx;

import javafx.animation.FadeTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.util.Duration;
import models.Coordinates;
import models.Person;
import net.client.Client;
import net.data.Request;
import net.data.ServerCommand;
import users.Identification;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonsCanvas {

    private PersonsTable personsTable;

    /**
     * Область для рисования.
     */

    private Group group;

    private Canvas canvas;

    private Pane overlay;

    /**
     * Размеры всей области рисования.
     */
    private double width;
    private double height;

    // кол-во пикселей рабочей области на единицу координаты X
    double ratioX;
    // кол-во пикселей рабочей области на единицу координаты Y
    double ratioY;

    /**
     * Размеры рабочей области рисования (для отображения объектов persons).
     */
    private double workAreaWidth;
    private double workAreaHeight;

    private double minCoordsX;
    private double minCoordsY;
    private double maxCoordsX;
    private double maxCoordsY;

    double centerX;
    double centerY;

    /**
     * Область для рисования.
     */
    GraphicsContext gc;

    /**
     * Конструктор.
     *
     * @param personsTable для доступа к объектам persons.
     */
    public PersonsCanvas(PersonsTable personsTable) {
        this.personsTable = personsTable;
    }

    /**
     * Подготовить объект canvas и уровень overlay для рисования персон.
     */
    public void prepare() {
        group = new Group();
        overlay = new Pane();
        canvas = new Canvas();
        group.getChildren().addAll(canvas, overlay);
        gc = canvas.getGraphicsContext2D();
    }

    public Group refreshAndGetCanvas(double width, double height) {
        canvas.setWidth(this.width = width);
        canvas.setHeight(this.height = height);
        workAreaWidth = width - 30;
        workAreaHeight = height - 30;
        redraw();
        return group;
    }

    int i = 0;

    public void redraw() {
        if (this.width == 0) return; // еще не открывался
        gc.clearRect(0, 0, width, height);
        overlay.getChildren().removeAll(overlay.getChildren());
        draw();
    }

    /**
     * Нарисовать область.
     */
    private void draw() {
        Collection<Person> persons = personsTable.getPersons();
        if (persons != null) {
            maxCoordsX = persons.stream().filter(person -> person.getCoordinates() != null).map(person -> person.getCoordinates().getX().doubleValue()).reduce(0d, Math::max);
            maxCoordsY = persons.stream().filter(person -> person.getCoordinates() != null).map(person -> person.getCoordinates().getY().doubleValue()).reduce(0d, Math::max);
            minCoordsX = persons.stream().filter(person -> person.getCoordinates() != null).map(person -> person.getCoordinates().getX().doubleValue()).reduce(0d, Math::min);
            minCoordsY = persons.stream().filter(person -> person.getCoordinates() != null).map(person -> person.getCoordinates().getY().doubleValue()).reduce(0d, Math::min);
            calculateRatio();
        } else {
            minCoordsX = minCoordsY = maxCoordsX = maxCoordsY = 0d;
        }

        centerX = minCoordsX < 0 ? (workAreaWidth / 2d) : 0;
        centerY = minCoordsY < 0 ? (workAreaHeight / 2d) : 0;

        // System.out.println(maxCoordsX + " x " + maxCoordsY + " - " + minCoordsX + " x " + minCoordsY + " (" + centerX + " - " + centerY + ")" + " (" + ratioX + " - " + ratioY + ")");

        // нарисовать оси
        gc.setLineWidth(1.5);
        // область координат перевернута по вертикали
        // оси
        // X
        gc.strokeLine(X(0), Y(centerY), X(workAreaWidth), Y(0 + centerY));
        // Y
        gc.strokeLine(X(centerX), Y(0), X(centerX), Y(workAreaHeight));
        // стрелки на осях
        // X
        gc.strokeLine(X(workAreaWidth), Y(-1 + centerY), X(workAreaWidth - 15), Y(-10 + centerY));
        gc.strokeLine(X(workAreaWidth), Y(1 + centerY), X(workAreaWidth - 15), Y(10 + centerY));
        // Y
        gc.strokeLine(X(-1 + centerX), Y(workAreaHeight), X(-10 + centerX), Y(workAreaHeight - 15));
        gc.strokeLine(X(1 + centerX), Y(workAreaHeight), X(10 + centerX), Y(workAreaHeight - 15));
        // надписи на осях
        gc.setLineWidth(1);
        gc.setFont(Font.font("Arial", 20));
        // X
        gc.strokeText("X " + maxCoordsX, X(workAreaWidth - 70), Y(15 + centerY));
        // Y
        gc.strokeText("Y" + maxCoordsY, X(15 + centerX), Y(workAreaHeight - 15));

        // Нарисовать persons
        drawPersons();
    }

    /**
     * Нарисовать persons.
     */
    private void drawPersons() {
        Collection<Person> persons = personsTable.getPersons();
        if (persons == null || persons.isEmpty()) return;
        persons.forEach(this::drawPerson);
        // persons.stream().filter(p -> p.getCoordinates().getY() == 3f).forEach(this::drawPerson);
    }

    /**
     * Нарисовать person и добавить события.
     */
    private void drawPerson(Person person) {
        Coordinates coordinates = person.getCoordinates();
        if (coordinates == null) return;

        // Адаптировать координаты для показа
        double personX = coordinates.getX().doubleValue() * ratioX;
        double personY = coordinates.getY().floatValue() * ratioY;

        List<Shape> shapes = drawPersonShapes(personX, personY);
        boolean owner = person.getId() == null || person.getId().intValue() == 0 || person.getUserId().equals(Identification.getIdentification().getUserInfo().getId());
        shapes.forEach(r -> {
            Integer[] colorItemsArr = { 0x44, 0x88, 0xcc };
            // расчет цвета в зависимости от владельца
            List<Integer> colorItems = Stream.of(colorItemsArr).map(colorItem -> owner ? colorItem : colorItem * (person.getUserId() + 1)).toList();
            // строковое значение цвета
            String color = colorToStr(colorItems);
            r.setFill(Color.valueOf("#" + color));
            // события на персону
            // двойной клик
            r.setOnMouseClicked(event -> {
                if (event.getClickCount() != 2) return;
                personsTable.handleEditPerson(person);
            });
            // наведение мыши
            r.setOnMouseEntered(event -> {
                String currentColorStr = r.getFill().toString();
                // для головы и тела
                shapes.forEach(r2 -> {
                    Boolean state = (Boolean)r2.getUserData();
                    if (state != null) return;
                    r2.setUserData(Boolean.TRUE);
                    // сделать цвет посветлее
                    List<Integer> currentColorItems = Arrays.asList(Integer.valueOf(currentColorStr.substring(2, 4), 16) /* R */, Integer.valueOf(currentColorStr.substring(4, 6), 16) /* G */, Integer.valueOf(currentColorStr.substring(6, 8), 16) /* B */);
                    List<Integer> lightColorItems = currentColorItems.stream().map(colorItem -> colorItem + 16 > 255 ? 255 : colorItem + 16).toList();
                    String overColor = colorToStr(lightColorItems);
                    // установить цвет посветлее
                    r2.setFill(Color.valueOf("#" + overColor));
                });
            });
            // уход мыши
            r.setOnMouseExited(event -> {
                shapes.forEach(r2 -> {
                    // вернуть исходный цвет
                    Boolean state = (Boolean) r2.getUserData();
                    if (state == null) return;
                    r2.setUserData(null);
                    r2.setFill(Color.valueOf("#" + color));
                });
            });
            if (person.getUserId().equals(Identification.getIdentification().getUserInfo().getId())) {
                // смена координат - только для своих персон
                r.setCursor(Cursor.OPEN_HAND);
                // контейнеры для использования внутри лямбда фунций
                final ObjectProperty<Boolean> moved = new SimpleObjectProperty<>(Boolean.FALSE);
                final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>();
                final ObjectProperty<Point2D> originalPosition = new SimpleObjectProperty<>();
                // нажали кнопку
                r.setOnMousePressed(event -> {
                    r.setCursor(Cursor.CLOSED_HAND);
                    mousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
                    originalPosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
                });
                // передвинули фигуру
                r.setOnMouseDragged(event -> {
                    // если вышли за рамки экрана, то не выполнять
                    if (event.getSceneX() < 15d || event.getSceneX() > workAreaWidth ||
                            event.getSceneY() < 90 || event.getSceneY() > workAreaHeight + 60) {
                        return;
                    }
                    double deltaX = event.getSceneX() - mousePosition.get().getX();
                    double deltaY = event.getSceneY() - mousePosition.get().getY();
                    shapes.forEach(r2 -> {
                        r2.setLayoutX(r2.getLayoutX() + deltaX);
                        r2.setLayoutY(r2.getLayoutY() + deltaY);
                    });
                    mousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
                    moved.set(Boolean.TRUE);
                });
                // отпустили кнопку
                r.setOnMouseReleased(event -> {
                    r.setCursor(Cursor.OPEN_HAND);
                    if (!moved.get()) return;
                    moved.set(Boolean.FALSE);
                    double deltaX = event.getSceneX() - originalPosition.get().getX();
                    double deltaY = -1 * (event.getSceneY() - originalPosition.get().getY());
                    UiHelper.confirm(UI.getI18n("form_question").get(), UI.getI18n("change_coords").get(),
                            () -> {
                                System.out.println((deltaX / ratioX) + " x " + (deltaY / ratioY));
                                person.getCoordinates().setX(person.getCoordinates().getX() + (long) (deltaX / ratioX));
                                person.getCoordinates().setY(person.getCoordinates().getY() + (float) (deltaY / ratioY));
                                try {
                                    ServerCommand command = ServerCommand.update(person);
                                    Client.getClient().sendRequest(new Request(command));
                                    personsTable.reloadPersons(false);
                                } catch (Exception e) {
                                    UiHelper.showAlert(Alert.AlertType.ERROR, UI.getI18n("form_error").get(), UI.getI18n("error_unknown").get());
                                }
                            },
                            () -> {
                                // возврат в исходное
                                shapes.forEach(r2 -> {
                                    r2.setLayoutX(r2.getLayoutX() - deltaX);
                                    r2.setLayoutY(r2.getLayoutY() + deltaY);
                                });
                            }
                    );
                });
            }
        });
        overlay.getChildren().addAll(shapes);
    }

    /**
     * Нарисовать фигуры для одного персон по заданным координатам.
     */
    private List<Shape> drawPersonShapes(double personX, double personY) {
        double personHalfHeight = 23d;
        // тело
        Polygon polygon = new Polygon(
               Xc(personX), Yc(personY + 36) + personHalfHeight,
                Xc(personX + 29), Yc(personY + 36) + personHalfHeight,
                Xc(personX + 29), Yc(personY + 30) + personHalfHeight,
                Xc(personX + 22), Yc(personY + 30) + personHalfHeight,
                Xc(personX + 22), Yc(personY) + personHalfHeight,
                Xc(personX + 17), Yc(personY) + personHalfHeight,
                Xc(personX + 17), Yc(personY + 15) + personHalfHeight,
                Xc(personX + 14), Yc(personY + 15) + personHalfHeight,
                Xc(personX + 14), Yc(personY) + personHalfHeight,
                Xc(personX + 8), Yc(personY) + personHalfHeight,
                Xc(personX + 8), Yc(personY + 30) + personHalfHeight,
                Xc(personX), Yc(personY + 30) + personHalfHeight
        );
        // голова
        Circle circle = new Circle(Xc(personX + 15), Yc(personY + 44) + personHalfHeight, 6);
        List<Shape> shapes = Stream.of(polygon, circle).toList();
        fadeIn(shapes);
        return shapes;
    }

    /**
     * Показать с анимацией.
     */
    private void fadeIn(List<Shape> shapes) {
        shapes.forEach(shape -> {
            FadeTransition fade = new FadeTransition(Duration.millis(800), shape);
            fade.setFromValue(0.1);
            fade.setToValue(10);
            fade.play();
        });
    }

    /**
     * Перевод компонент цвета RGB в строку.
     */
    private String colorToStr(List<Integer> colorItems) {
        return colorItems.stream().map(Integer::toHexString).map(s -> s.length() > 2 ? s.substring(s.length() - 2) : s.length() == 1 ? "0" + s : s).collect(Collectors.joining());
    }

    /**
     * Найти кол-во пикселей рабочей области на единицу координат.
     */
    private void calculateRatio() {
        // кол-во пикселей рабочей области на единицу координаты X (-32 т.к. размер фигуры = 32)
        // учесть положение центра координат по центру, поэтому берется max от модуля
        ratioX = (workAreaWidth - 32) / (Math.max(Math.abs(maxCoordsX), Math.abs(minCoordsX)) * (minCoordsX < 0d ? 2 : 1));
        // кол-во пикселей рабочей области на единицу координаты Y (-50 т.к. размер фигуры = 50)
        // учесть положение центра координат по центру, поэтому берется max от модуля
        ratioY = (workAreaHeight - 50) / (Math.max(Math.abs(maxCoordsY), Math.abs(minCoordsY)) * (minCoordsY < 0d ? 2 : 1));
    }

    /**
     * Для рассмотрения оси X отступом слева - 15px.
     */
    private double X(double X) {
        return X + 15d;
    }

    /**
     * Для рассмотрения оси X относительно центра.
     */
    private double Xc(double X) {
        return X + centerX;
    }

    /**
     * Для рассмотрения оси Y снизу вверх и отступа снизу - 15px.
     */
    private double Y(double Y) {
        return height - Y - 15d;
    }

    /**
     * Для рассмотрения оси Y относительно центра.
     */
    private double Yc(double Y) {
        // return height - Y - (centerY != 0d ? centerY : 15d);
        return height - Y - centerY - 15d;
    }
}
