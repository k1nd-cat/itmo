package ui.javafx;

import javafx.beans.binding.StringBinding;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.i18n.I18nManager;

public class UI {

    private static Stage primaryStage;

    public static I18nManager i18nManager = new I18nManager();

    public static StringBinding getI18n(String key) {
        return i18nManager.getStringBinding(key);
    }

    public static Scene mainScene() { return primaryStage.getScene(); }

    /**
     * Запуск JavaFX приложения.
     */
    public static void launch(Stage primaryStage) {
        UI.primaryStage = primaryStage;

        // TODO: uncomment
        Scene scene = new Scene(new LoginForm().prepare(), 600, 600);

        // TODO: delete
        // Identification.getIdentification().setUserInfo();
        // Scene scene = new Scene(new MainForm().prepare(), 1500, 1000);
        // end delete

        primaryStage.titleProperty().bind(getI18n("app_title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Открыть основную форму (после успешного логина / регистрации).
     */
    public static void openMainForm() {
        UI.primaryStage.setScene(new Scene(new MainForm().prepare(), 1500, 1000));
        primaryStage.show();
    }

    /**
     * Выход из приложения.
     */
    public static void exit() {
        System.exit(0);
    }

}
