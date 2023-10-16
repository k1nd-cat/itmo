import PersonFeatures.*;
import commands.Invoker;

import java.util.TreeSet;

public class Main {
    public static void main(String[] argc) throws Exception {
//        JSONObject jo = new JSONObject();
//        jo.put("name", "jon doe");
//        jo.put("age", "22");
//        jo.put("city", "chicago");
//        Scanner in = new Scanner(System.in);
//        String text = in.nextLine();
//        Person person1 = new Person("Bob", new Coordinates(2, 4), 185, Color.BROWN, Color.BLUE, Country.JAPAN, new Location(3L, 5L, 7F));
//        Person person2 = new Person("Nekit", new Coordinates(5, 7), 164, Color.ORANGE, Color.BROWN, Country.FRANCE, new Location(10L, 7L, 4F));
//        TreeSet<Person> persons = new TreeSet<Person>();
//        persons.add(person1);
//        persons.add(person2);
//        HandlerJSON.save(persons);
//        System.out.println(person1.toString());
//        String command;
//        while (true) {
//            command = in.next();
//            System.out.println(command);
//        }
//        ArrayList<Double> test = new ArrayList<Double>();
//        System.out.println("1. Orginal Map entries : \n");
//        Invoker.getCommands().forEach((key, value) -> System.out.println(key + "\t" + value));
        Invoker.getCommands().get("help").execute("");
    }
}
