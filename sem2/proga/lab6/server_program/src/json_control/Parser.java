package json_control;

import com.google.gson.GsonBuilder;

import java.time.ZonedDateTime;

/**
 * parser to and from jason
 */
public class Parser {
    /**
     * @param persons people list
     * @return json text with person collection
     */
    public static String personsToJson(PersonList persons) {
        return new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJsonAdapter()).create().toJson(persons);
    }

    /**
     * @param json json text for parsing
     * @return people list from json text
     */
    public static PersonList jsonToPersons(String json) {
        try {
            return new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJsonAdapter()).create().fromJson(json, PersonList.class);
        } catch (com.google.gson.JsonSyntaxException ignore) {
            System.err.println("Error in file with data");
            return null;
        }
    }
}