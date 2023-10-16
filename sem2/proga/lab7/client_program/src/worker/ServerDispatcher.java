package worker;

import data_base.DbConnector;
import json_control.FileDespatcher;
import json_control.Parser;
import json_control.PersonList;
import models.Person;
import net.data.Request;
import net.data.Response;
import net.data.ServerCommand;
import net.data.ServerCommandWithBody;
import users.UserInfo;
import utils.StrJoiner;
import utils.SumCounter;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static worker.PersonStorage.clearPeople;
import static worker.PersonStorage.getPersons;

/**
 * With multithreading.
 */
public class ServerDispatcher {

    private static final Logger logger = Logger.getLogger("lab6");

    private static ServerDispatcher dispatcher;

    public static ServerDispatcher getDispatcher() {
        if (dispatcher == null) {
            dispatcher = new ServerDispatcher();
        }
        return dispatcher;
    }

    public Response perform(Request request) throws Exception {
        Response response = null;
        if (request.getCommand().getType() == ServerCommand.TYPE.add) {
            var command = (ServerCommandWithBody)request.getCommand();
            response = performAdd(command.getBody());
        } else if (request.getCommand().getType() == ServerCommand.TYPE.update) {
            var command = (ServerCommandWithBody)request.getCommand();
            response = performUpdate(command.getBody());
        } else if (request.getCommand().getType() == ServerCommand.TYPE.remove) {
            var command = (ServerCommandWithBody)request.getCommand();
            response = performRemove(command.getBody());
        } else if (request.getCommand().getType() == ServerCommand.TYPE.get_last) {
            response = performGetLast();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.get_first) {
            response = performGetFirst();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.average_of_height) {
            response = performAverageOfHeight();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.clear) {
            response = performClear();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.show) {
            response = performShow();
        } else if (request.getCommand().getType() == ServerCommand.TYPE.find_by_id) {
            var command = (ServerCommandWithBody)request.getCommand();
            response = performFindById((Integer)command.getBody());
        } else if (request.getCommand().getType() == ServerCommand.TYPE.check_user_unique) {
            var command = (ServerCommandWithBody) request.getCommand();
            response = performCheckUserUnique((String)command.getBody());
        } else if (request.getCommand().getType() == ServerCommand.TYPE.register_user) {
            var command = (ServerCommandWithBody) request.getCommand();
            response = performRegisterUser((UserInfo)command.getBody());
        } else if (request.getCommand().getType() == ServerCommand.TYPE.login_user) {
            var command = (ServerCommandWithBody) request.getCommand();
            response = performGetUserByLogin((String)command.getBody());
        }
        if (response == null) response = Response.error("command not found");
        return response;
    }

    public void performSave() {

        var saveFile = new File("D:\\Projects\\workspace_idea_test\\persons_collection.json");
        // var saveFile = new File(System.getenv("LAB5_JSON"));
        var saveDir = saveFile.getParentFile();
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        FileDespatcher.writeInFile(saveFile, Parser.personsToJson(new PersonList(getPersons())));
    }

    private Response performAdd(Object body) throws Exception {
        Person person = (Person)body;
        var dbConnector = new DbConnector();
        int id = dbConnector.addPerson(person);
        getPersons().add(dbConnector.getPersonById(id));
        logger.info("add person: " + person);
        return Response.ok();
    }

    private Response performUpdate(Object body) throws Exception {
        Person person = (Person)body;
        var dbConnector = new DbConnector();
        dbConnector.updatePerson(person);
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

    private Response performRemove(Object body) throws Exception {
        Integer id = (Integer)body;
        var dbConnector = new DbConnector();
        dbConnector.removeById(id);
        Optional<Person> removedPerson = getPersons().stream().filter(p -> p.getId().equals(id)).findFirst();
        removedPerson.ifPresent(value -> PersonStorage.getPersons().remove(value));
        logger.info("remove person, id: " + id);
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

    private Response performFindById(Integer id) {
        var response = Response.ok();
        Optional<Person> person = getPersons().stream().filter(p -> p.getId().equals(id)).findFirst();
        person.ifPresent(response::setBody);
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

    /**
     * With multithreading.
     */
    private Response performAverageOfHeight() {
        var response = Response.ok();
        if (!getPersons().isEmpty()) {
            List<Long> heights = getPersons().stream().map(Person::getHeight).collect(Collectors.toList());
            float averageValue = new ForkJoinPool(4).invoke(new SumCounter(heights));
            averageValue /= (float)heights.size();
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

    /**
     * With multithreading.
     */
    private Response performShow() {
        var response = Response.ok();
        if (!getPersons().isEmpty()) {
            List<String> personStrs = getPersons().stream().map(Person::toString).collect(Collectors.toList());
            String result = new ForkJoinPool(4).invoke(new StrJoiner(personStrs, "\n-----------\n"));
            System.out.println(result);
            response.setBody(result);
        } else {
            response.setBody("-----Collection is empty-----");
        }

        return response;
    }

    private Response performCheckUserUnique(String userName) throws Exception {
        var dbConnector = new DbConnector();
        boolean ok = dbConnector.checkUserUnique(userName);
        return ok ? Response.ok() : Response.error();
    }

    private Response performRegisterUser(UserInfo userInfo) throws Exception {
        var dbConnector = new DbConnector();
        Integer id = dbConnector.registerUser(userInfo);
        Response response;
        if (id != -1) {
            response = Response.ok();
            response.setBody(id);
        } else response = Response.error();
        return response;
    }

    private Response performGetUserByLogin(String userName) throws Exception {
        var dbConnector = new DbConnector();
        UserInfo userInfo = dbConnector.getUserByLogin(userName);
        Response response;
        if (userInfo != null) {
            response = Response.ok();
            response.setBody(userInfo);
        } else response = Response.error();
        return response;
    }

}
