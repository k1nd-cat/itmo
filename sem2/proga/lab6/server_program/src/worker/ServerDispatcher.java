package worker;

import json_control.FileDespatcher;
import json_control.Parser;
import json_control.PersonList;
import models.Person;
import net.data.Request;
import net.data.Response;
import net.data.ServerCommand;
import net.data.ServerCommandWithBody;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static worker.PersonStorage.clearPeople;
import static worker.PersonStorage.getPersons;

public class ServerDispatcher {

    private static final Logger logger = Logger.getLogger("lab6");

    private static ServerDispatcher dispatcher;

    public static ServerDispatcher getDispatcher() {
        if (dispatcher == null) {
            dispatcher = new ServerDispatcher();
        }
        return dispatcher;
    }

    public Response perform(Request request) {
        Response response = null;
        if (request.getCommand().getType() == ServerCommand.TYPE.add) {
            var command = (ServerCommandWithBody) request.getCommand();
            response = performAdd(command.getBody());
        }else if (request.getCommand().getType() == ServerCommand.TYPE.update) {
            var command = (ServerCommandWithBody)request.getCommand();
            response = performUpdate(command.getBody());
        }  else if (request.getCommand().getType() == ServerCommand.TYPE.get_last) {
            response = performGetLast();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.get_first) {
            response = performGetFirst();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.average_of_height) {
            response = performAverageOfHeight();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.clear) {
            response = performClear();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.show) {
            response = performShow();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.info) {
            response = performInfo();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.min_by_location) {
            response = performMinByLocation();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.remove) {
            var command = (ServerCommandWithBody)request.getCommand();
            response = performRemove(command.getBody());
        } else if (request.getCommand().getType() == ServerCommand.TYPE.remove_greater) {
            var command = (ServerCommandWithBody)request.getCommand();
            response = performRemoveGreater(command.getBody());
        } else if (request.getCommand().getType() == ServerCommand.TYPE.find_by_id) {
            var command = (ServerCommandWithBody) request.getCommand();
            response = performFindById((Integer) command.getBody());
        } else if (request.getCommand().getType() == ServerCommand.TYPE.unique_hair) {
            response = performUniqueHair();
        }

        if (response == null) response = Response.error("command not found");
        return response;
    }

    public void performSave() {
        var saveFile = new File(System.getenv("LAB5_JSON"));
        var saveDir = saveFile.getParentFile();
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        FileDespatcher.writeInFile(saveFile, Parser.personsToJson(new PersonList(getPersons())));
    }

    private Response performAdd(Object body) {
        Person person = (Person)body;
        person.updateId();
        getPersons().add(person);
        logger.info("add person: " + person);
        return Response.ok();
    }


    private Response performUpdate(Object body) {
        Person person = (Person)body;

        Person currentPerson = getPersons().stream().filter(p -> p.getId().equals(person.getId())).findFirst().get();
        currentPerson.setName(person.getName());
        currentPerson.setLocation(person.getLocation());
        currentPerson.setNationality(person.getNationality());
        currentPerson.setCoordinates(person.getCoordinates());
        currentPerson.setHairColor(person.getHairColor());
        currentPerson.setHeight(person.getHeight());
        currentPerson.setEyeColor(person.getEyeColor());

        logger.info("update person: " + person);
        return Response.ok();
    }

    private Response performGetLast() {

        var response = Response.ok();
        if (!getPersons().isEmpty()) {
            var person = getPersons().stream().max(Comparator.comparingLong(Person::getHeight));
            response.setBody(person);
        }

        return response;
    }

    private Response performGetFirst() {
        var response = Response.ok();
        if (!getPersons().isEmpty()) {
            var person = getPersons().stream().min(Comparator.comparingLong(Person::getHeight));
            response.setBody(person);
        }

        return response;
    }

    private Response performAverageOfHeight() {
        var response = Response.ok();
        float averageValue = (float) 0;
        if (!getPersons().isEmpty()) {
            for (Person person : getPersons()) averageValue += person.getHeight();
            averageValue /= getPersons().size();
            response.setBody(averageValue);
        }

        return response;
    }

    private Response performClear() {
        var response = Response.ok();
        if(!getPersons().isEmpty()) {
            var persons = getPersons();
            clearPeople();
            response.setBody(persons);
        }

        return response;
    }

    private Response performGetCollection() {
        var response = Response.ok();
        var persons = getPersons();
        response.setBody(persons);
        return response;
    }

    private Response performFindById(int id) {
        var response = Response.ok();
        Optional<Person> person = getPersons().stream().filter(p -> p.getId().equals(id)).findFirst();
        person.ifPresent(response::setBody);
        return response;
    }

    private Response performShow() {
        var response = Response.ok();
        if (!getPersons().isEmpty()) {
            String result = getPersons().stream().map(Person::toString).collect(Collectors.joining("\n-----------\n"));
            response.setBody(result);
        } else {
            response.setBody("-----Collection is empty-----");
        }

        return response;
    }

    private Response performInfo() {
        var response = Response.ok();
        String info = "type: person\nelements count: " + getPersons().size();
        response.setBody(info);

        return response;
    }

    private Response performMinByLocation() {
        var response = Response.ok();
        String result = String.valueOf(PersonStorage.getPersons().stream().min(Comparator.comparing(Person::getLocationSize)).orElse(null));
        response.setBody(result);

        return response;
    }

    private Response performRemove(Object body) {
        Integer id = (Integer)body;
        Optional<Person> removedPerson = getPersons().stream().filter(p -> p.getId().equals(id)).findFirst();
        removedPerson.ifPresent(value -> PersonStorage.getPersons().remove(value));
        logger.info("remove person, id: " + id);
        return Response.ok();
    }

    private Response performRemoveGreater(Object body) {
        var response = Response.ok();
        String result = String.valueOf(PersonStorage.getPersons().stream().min(Comparator.comparing(Person::getLocationSize)).orElse(null));
        response.setBody(result);

        int id = (int)body;
        try {
            ((TreeSet<Person>)getPersons().clone()).stream()
                    .filter(person -> person.compareTo(PersonStorage.getById(id)) > 0)
                    .forEach(PersonStorage::removePerson);
            System.out.println("----- Deletion completed -----");
        } catch (RuntimeException exc) {
            System.err.println("Error in removing greater people" + exc.getMessage());
        }

        return response;
    }

    private Response performUniqueHair() {
        var response = Response.ok();
        String colors = "";
        try {
            colors = PersonStorage.getPersons().stream().map(x -> x.getHairColor().toString()).distinct().toString();
        } catch (RuntimeException exc) {
            System.err.println("Error in unique hair color" + exc.getMessage());
        }

        response.setBody(colors);
        return response;
    }
}
