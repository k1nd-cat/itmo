package commands;

import java.util.HashMap;
import java.util.Map;

public class Invoker {
    private static final Map<String, Command> commands = new HashMap<String, Command>() {{
        put("help", new Help());
        put("info", new Info());
        put("show", new Show());
        put("add", new Add());
        put("update_id", new Update());
        put("remove_by_id", new RemoveById());
        put("clear", new Clear());
        put("save", new Save());
        put("execute_script", new ExecuteScript());
        put("exit", new Exit());
        put("add_if_max", new AddIfMax());
        put("add_if_min", new AddIfMin());
        put("remove_greater", new RemoveGreater());
        put("average_of_height", new AverageOfHeight());
        put("min_by_location", new MinByLocatioin());
        put("print_unique_hair_color", new PrintUniqueHairColor());
    }};

    public static Map<String, Command> getCommands() {
        return commands;
    }

    public static void invoke() {

    }
}
