package json_control;

import com.sun.source.tree.Tree;
import models.Coordinates;
import models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * storage people for parser
 */
public class PersonList {
    public final List<Person> personsList;

    public PersonList(TreeSet<Person> persons) {
        personsList = persons.stream().toList();
    }

    /**
     * @return people treeSet collection
     */
    public TreeSet<Person> getTreeSet() {
        TreeSet<Person> result = new TreeSet<>();
        for (Person person : personsList) {
            try {
                Person newPerson = new Person(person.getName(), new Coordinates(person.getCoordinates().getX(), person.getCoordinates().getY()), person.getHeight(), person.getEyeColor(),
                        person.getHairColor(), person.getNationality(), person.getLocation());
                result.add(newPerson);
            } catch (Exception e) {
                System.err.println("One of collection elements is wrong.");
                System.err.println(e.getMessage());
            }
        }

        return result;
    }
}
