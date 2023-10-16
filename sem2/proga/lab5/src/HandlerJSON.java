import org.json.*;
import java.util.TreeSet;

public class HandlerJSON {
    public static void save(TreeSet<Person> persons/*, String fileName*/) {
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();
        for (Person person : persons) {
            ja.put("Person");
            jo.put("id", person.getId());
            jo.put("name", person.getName());
            jo.put("coordinateX", person.getCoordinates().getX());
            jo.put("coordinateY", person.getCoordinates().getY());
            jo.put("creationDate", person.getCreationDate());
            jo.put("height", person.getHeight());
            jo.put("eyeColor", person.getEyeColor());
            jo.put("hairColor", person.getHairColor());
            jo.put("nationality", person.getNationality());
            jo.put("locationX", person.getLocation().getX());
            jo.put("locationY", person.getLocation().getY());
            jo.put("locationZ", person.getLocation().getZ());
            ja.put(jo);
        }

        System.out.println(ja);
    }

    public static void get() {

    }
}
