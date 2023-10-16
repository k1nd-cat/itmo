package data_base;

import models.*;
import net.data.Response;
import users.UserInfo;
import worker.PersonStorage;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DbConnector {
    static String serverName = "localhost:5432";
    static String dbName = "persons";
    static String dbPath = "jdbc:postgresql://" + serverName + "/" + dbName;
    static String dbDriver = "org.postgresql.Driver";
    static String dbUser = "postgres";
    static String dbPass = "ROOT";

    public Person getPersonById(int id) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("select * from persons where id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Person person = new Person();
                person.setName(resultSet.getString("name"));
                person.setCoordinates(new Coordinates(resultSet.getLong("coordinates_x"), resultSet.getFloat("coordinates_y")));
                person.setHeight(resultSet.getLong("height"));
                person.setCreationDate(resultSet.getTimestamp("creationDate").toInstant().atZone(ZoneId.systemDefault()));
                String eyeColor = resultSet.getString("eyeColor");
                if (eyeColor != null)
                    person.setEyeColor(Color.valueOf(eyeColor));
                String hairColor = resultSet.getString("hairColor");
                if (hairColor != null)
                    person.setHairColor(Color.valueOf(hairColor));
                String nationality = resultSet.getString("nationality");
                if (nationality != null)
                    person.setNationality(Country.valueOf(nationality));
                Integer location_id = resultSet.getInt("location_id");
                if (resultSet.wasNull()) location_id = -1;
                person.setUserId(resultSet.getInt("user_id"));

                if (location_id != -1) {
                    preparedStatement = connection.prepareStatement("select * from person_location where id = ?");
                    preparedStatement.setInt(1, location_id);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next())
                        person.setLocation(new Location(resultSet.getInt("x"), resultSet.getLong("y"), resultSet.getInt("z")));
                }
                person.setId(id);
                return person;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return null;
    }

    public int addPerson(Person person) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
//            Добавление location, если оно существует
            int locationId = -1;
            if (person.getLocation() != null) {
                preparedStatement = connection.prepareStatement("insert into person_location(x, y, z) values(?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, person.getLocation().getX());
                preparedStatement.setLong(2, person.getLocation().getY());
                preparedStatement.setInt(3, person.getLocation().getZ());
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
                locationId = resultSet.next() ? resultSet.getInt(1) : -1;
            }

//            Добавление точно ненулевых элементов
            int index = 1;
            preparedStatement = connection.prepareStatement("insert into persons(name, coordinates_x, coordinates_y, height, creationDate, eyeColor, hairColor, nationality, location_id, user_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(index++, person.getName());
            preparedStatement.setLong(index++, person.getCoordinates().getX());
            preparedStatement.setFloat(index++, person.getCoordinates().getY());
            preparedStatement.setLong(index++, person.getHeight());
            preparedStatement.setTimestamp(index++, java.sql.Timestamp.from(person.getCreationDate().toInstant()));
            if (person.getEyeColor() != null)
                preparedStatement.setString(index++, person.getEyeColor().toString());
            else
                preparedStatement.setNull(index++, Types.VARCHAR);

            if (person.getHairColor() != null)
                preparedStatement.setString(index++, person.getHairColor().toString());
            else
                preparedStatement.setNull(index++, Types.VARCHAR);

            if (person.getNationality() != null)
                preparedStatement.setString(index++, person.getNationality().toString());
            else
                preparedStatement.setNull(index++, Types.VARCHAR);

            if (locationId != -1)
                preparedStatement.setInt(index++, locationId);
            else
                preparedStatement.setNull(index++, Types.INTEGER);
            preparedStatement.setInt(index++, person.getUserId());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : -1;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public void updatePerson(Person person) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Person currentPerson = getPersonById(person.getId());
            if (!Objects.equals(person.getUserId(), currentPerson.getUserId())) {
                throw new Exception("Access denied");
            }
            connection = getConnection();
            int locationId = -1;
            if (currentPerson.getLocation() == null && person.getLocation() != null) {
                preparedStatement = connection.prepareStatement("insert into person_location(x, y, z) values(?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, person.getLocation().getX());
                preparedStatement.setLong(2, person.getLocation().getY());
                preparedStatement.setInt(3, person.getLocation().getZ());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                resultSet = preparedStatement.getGeneratedKeys();
                locationId = resultSet.next() ? resultSet.getInt(1) : -1;
                resultSet.close();
            } else if (currentPerson.getLocation() != null && person.getLocation() != null) {
                preparedStatement = connection.prepareStatement("update person_location set x = ?, y = ?, z = ? where id = (select location_id from persons where id = ?)");
                preparedStatement.setInt(1, person.getLocation().getX());
                preparedStatement.setLong(2, person.getLocation().getY());
                preparedStatement.setInt(3, person.getLocation().getZ());
                preparedStatement.setInt(4, person.getId());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }

            preparedStatement = connection.prepareStatement("update persons set name = ?, coordinates_x = ?, coordinates_y = ?, height = ?, creationDate = ?, eyeColor = ?, hairColor = ?, nationality = ?, location_id = ? where id = ?");
            int index = 1;
            preparedStatement.setString(index++, person.getName());
            preparedStatement.setLong(index++, person.getCoordinates().getX());
            preparedStatement.setFloat(index++, person.getCoordinates().getY());
            preparedStatement.setLong(index++, person.getHeight());
            preparedStatement.setTimestamp(index++, java.sql.Timestamp.from(person.getCreationDate().toInstant()));
            if (person.getEyeColor() != null)
                preparedStatement.setString(index++, person.getEyeColor().toString());
            else
                preparedStatement.setNull(index++, Types.VARCHAR);
            if (person.getHairColor() != null)
                preparedStatement.setString(index++, person.getHairColor().toString());
            else
                preparedStatement.setNull(index++, Types.VARCHAR);
            if (person.getNationality() != null)
                preparedStatement.setString(index++, person.getNationality().toString());
            else
                preparedStatement.setNull(index++, Types.VARCHAR);
            if (locationId != -1)
                preparedStatement.setInt(index++, locationId);
            else
                preparedStatement.setNull(index++, Types.INTEGER);
            preparedStatement.setInt(index++, person.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            if (currentPerson.getLocation() != null && person.getLocation() == null) {
                preparedStatement = connection.prepareStatement("delete from person_location where id = (select location_id from persons where id = ?)");
                preparedStatement.setInt(1, person.getId());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public void removeById(Integer id) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("select location_id from persons where id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            int locationId;
            if (resultSet.next()) {
                locationId = resultSet.getInt(1);
                if (resultSet.wasNull()) locationId = -1;
            } else locationId = -1;
            resultSet.close();

            preparedStatement = connection.prepareStatement("delete from persons where id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            if (locationId != -1) {
                preparedStatement = connection.prepareStatement("delete from person_location where id = ?");
                preparedStatement.setInt(1, locationId);
                preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public void initializeCollection() throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("select id from persons");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Person person = getPersonById(resultSet.getInt("id"));
                PersonStorage.getPersons().add(person);
            }
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public boolean checkUserUnique(String userName) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("select id from users where lower(user_name) = ?");
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            boolean result = !resultSet.next();
            return result;
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public Integer registerUser(UserInfo userInfo) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("insert into users(user_name, password) values(?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userInfo.getUsername());
            preparedStatement.setString(2, userInfo.getPassword());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : -1;
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public UserInfo getUserByLogin(String userName) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("select * from users where lower(user_name) = ?");
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            UserInfo userInfo;
            if (resultSet.next()) {
                userInfo = new UserInfo();
                userInfo.setId(resultSet.getInt("id"));
                userInfo.setUsername(resultSet.getString("user_name"));
                userInfo.setPassword(resultSet.getString("password"));
            } else userInfo = null;
            return userInfo;
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    private static void close(AutoCloseable... statements) {
        if(statements == null || statements.length == 0) return;
        for (AutoCloseable statement : statements) {
            if(statement == null) continue;
            try {
                statement.close();
            } catch (Exception ignored) {}
        }
    }

    public Connection getConnection() throws Exception {
        Class.forName(dbDriver);
        return DriverManager.getConnection(dbPath, dbUser, dbPass);
    }
}
