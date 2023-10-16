import javafx.application.Application;
import javafx.stage.Stage;
import ui.javafx.UI;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
        /*
        Identification.getIdentification().readInfo(new BufferedReader(new InputStreamReader(System.in)));
        new CommandManager().readCommand(new BufferedReader(new InputStreamReader(System.in)), false);
        */
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        UI.launch(primaryStage);
    }
}