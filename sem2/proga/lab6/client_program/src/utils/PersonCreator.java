package utils;

import command_management.CommandManager;
import exceptions.NullStringException;
import exceptions.WrongCoordinatesException;
import exceptions.WrongStringException;
import models.Color;
import models.Coordinates;
import models.Country;
import models.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersonCreator {
    public static String createName(BufferedReader in, CommandManager manager) {
        String name = null;
        while (name == null) {
            try {
                name = readString("Enter person name", in);
            } catch (IOException exc) {
                System.err.println("Error in entered value" + exc.getMessage());
                if (manager.isFromFile) {
                    throw new RuntimeException("Error executing file script");
                }
            } catch (NoSuchElementException exc) {
                System.err.println("Fatal error, forced shutdown\n" + exc);
                System.exit(-1);
            }
        }

        return name;
    }

    public static long createHeight(BufferedReader in, CommandManager manager) {
        Long height = null;
        while (height == null || height <= 0) {
            try {
                height = readLong("Enter person height", in);
            } catch (IOException | NumberFormatException | WrongCoordinatesException exc) {
                System.err.println("Error in entered value" + exc.getMessage());
                if (manager.isFromFile) {
                    throw new RuntimeException("Error executing file script");
                }
            } catch (NoSuchElementException exc) {
                System.err.println("Fatal error, forced shutdown\n" + exc);
                System.exit(-1);
            }
        }

        return height;
    }

    /**
     * @param in input
     * @param manager command manager
     * @return coordinates for creating / change person
     */
    public static Coordinates createCoordinates(BufferedReader in, CommandManager manager) {
        Coordinates coordinates = null;
        Long xCoordinate = null;
        Float yCoordinate = null;
        boolean flag = false;
        System.out.println("Enter coordinates: ");
        while (xCoordinate == null || xCoordinate > 846) {
            try {
                xCoordinate = readLong("Enter x coordinate (max value is 846): ", in);
                if (xCoordinate == null && manager.isFromFile) throw new RuntimeException("Error executing file script");
            } catch (IOException | NumberFormatException | WrongCoordinatesException exc) {
                System.err.println("Error in entered value" + exc.getMessage());
                if (manager.isFromFile) {
                    throw new RuntimeException("Error executing file script");
                }
            } catch (NoSuchElementException exc) {
                System.err.println("Fatal error, forced shutdown\n" + exc);
                System.exit(-1);
            }
        }

        while (yCoordinate == null || xCoordinate <= -859) {
            try {
                yCoordinate = readFloat("Enter y coordinate (min value not more then -859): ", in);
                if (yCoordinate == null && manager.isFromFile) throw new RuntimeException("Error executing file script");
            } catch (IOException | NumberFormatException | WrongCoordinatesException exc) {
                System.err.println("Error in entered value" + exc.getMessage());
                if (manager.isFromFile) {
                    throw new RuntimeException("Error executing file script");
                }
            } catch (NoSuchElementException exc) {
                System.err.println("Fatal error, forced shutdown\n" + exc);
                System.exit(-1);
            }
        }

        return new Coordinates(xCoordinate, yCoordinate);
    }

    /**
     * @param in input
     * @param clazz enum for creating
     * @param manager command manager
     * @param <E> enum type
     * @return enum for creating / change person
     */
    public static <E extends Enum<E>> E createPersonCharacteristic(BufferedReader in, Class<E> clazz, CommandManager manager) {
        E characteristic = null;
        boolean flag = false;
        while(!flag) {
            try {
                String options = EnumSet.allOf(clazz).stream().map(E::toString).collect(Collectors.joining("\n"));
                System.out.println("Enter characteristic for person (" + clazz.getName() + "): \n" + options
                        + "\nor press Enter if person does not have this characteristic");
                String characteristicStr = in.readLine();
                if (Objects.equals(characteristicStr, "")) {
                    return null;
                }

                if (characteristicStr == null) throw new NullStringException();
                for(E en : EnumSet.allOf(clazz)){
                    if(en.name().equalsIgnoreCase(characteristicStr)) {
                        characteristic = en;
                        break;
                    }
                }
                if (characteristic == null) throw new IllegalArgumentException();
                flag = true;
            } catch (IllegalArgumentException | IOException exc) {
                System.err.println("Error in entered value: " + exc.getMessage());
                if (manager.isFromFile) {
                    throw new RuntimeException("Error executing file script");
                }
            } catch (NullStringException exc) {
                System.err.println("Fatal error, forced shutdown\n" + exc);
                System.exit(-1);
            }
        }

        return characteristic;
    }

    /**
     * @param in input
     * @param manager command manager
     * @return location for create / change person
     */
    public static Location createLocation(BufferedReader in, CommandManager manager) {
        Location location = null;
        Integer xCoordinate = null;
        Long yCoordinate = null;
        Integer zCoordinate = null;
        System.out.println("Enter location: ");
        try {
            System.out.println("If the person has coordinates, press any letter and Enter, otherwise just press Enter");
            String needCoordinates = in.readLine();
            if (needCoordinates == null) throw new WrongStringException();
            if (needCoordinates.equals("")) return null;
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        } catch (WrongStringException exc) {
            System.err.println("Fatal error, forced shutdown\n" + exc);
            System.exit(-1);
        }
        while (xCoordinate == null) {
            try {
                xCoordinate = readInteger("Enter x coordinate (not null): ", in);
                if (xCoordinate == null && manager.isFromFile) throw new RuntimeException("Error executing file script");
            } catch (IOException | NumberFormatException | WrongCoordinatesException exc) {
                System.err.println("Error in entered value" + exc.getMessage());
                if (manager.isFromFile) {
                    throw new RuntimeException("Error executing file script");
                }
            } catch (NoSuchElementException exc) {
                System.err.println("Fatal error, forced shutdown\n" + exc);
                System.exit(-1);
            }
        }

        while (yCoordinate == null) {
            try {
                yCoordinate = readLong("Enter y coordinate (not null): ", in);
                if (yCoordinate == null && manager.isFromFile) throw new RuntimeException("Error executing file script");
            } catch (IOException | NumberFormatException | WrongCoordinatesException exc) {
                System.err.println("Error in entered value" + exc.getMessage());
                if (manager.isFromFile) {
                    throw new RuntimeException("Error executing file script");
                }
            } catch (NoSuchElementException exc) {
                System.err.println("Fatal error, forced shutdown\n" + exc);
                System.exit(-1);
            }
        }

        while (zCoordinate == null) {
            try {
                zCoordinate = readInteger("Enter z coordinate: ", in);
                if (zCoordinate == null && manager.isFromFile) throw new RuntimeException("Error executing file script");
            } catch (IOException | NumberFormatException | WrongCoordinatesException exc) {
                zCoordinate = 0;
                System.err.println("Error in entered value" + exc.getMessage());
                if (manager.isFromFile) {
                    throw new RuntimeException("Error executing file script");
                }
            } catch (NoSuchElementException exc) {
                System.err.println("Fatal error, forced shutdown\n" + exc);
                System.exit(-1);
            }
        }


        return new Location(xCoordinate, yCoordinate, zCoordinate);
    }

    /**
     * @param str message for printing
     * @param in input info
     * @return Integer value
     * @throws IOException throws Exception
     */
    private static Integer readInteger(String str, BufferedReader in) throws IOException {
        try {
            System.out.println(str);
            String value = in.readLine();
            if (value == null) throw new NullStringException();
            return Integer.parseInt(value);
        } catch (NumberFormatException exc) {
            System.err.println("Error in entered value" + exc.getMessage());
        } catch (NullStringException exc) {
            System.err.println("Fatal error, forced shutdown\n" + exc);
            System.exit(-1);
        }

        return null;
    }

    /**
     * @param str message for printing
     * @param in input info
     * @return Long value
     * @throws IOException throws Exception
     */
    private static Long readLong(String str, BufferedReader in) throws IOException {
        try {
            System.out.println(str);
            String value = in.readLine();
            if (value == null) throw new NullStringException();
            return Long.parseLong(value);
        } catch (NumberFormatException exc) {
            System.err.println("Error in entered value" + exc.getMessage());
        } catch (NullStringException exc) {
            System.err.println("Fatal error, forced shutdown\n" + exc);
            System.exit(-1);
        }

        return null;
    }

    /**
     * @param str message for printing
     * @param in input info
     * @return Float value
     * @throws IOException throws Exception
     */
    private static Float readFloat(String str, BufferedReader in) throws IOException {
        try {
            System.out.println(str);
            String value = in.readLine();
            if (value == null) throw new NullStringException();
            return Float.parseFloat(value);
        } catch (NumberFormatException exc) {
            System.err.println("Error in entered value" + exc.getMessage());
        } catch (NullStringException exc) {
            System.err.println("Fatal error, forced shutdown\n" + exc);
            System.exit(-1);
        }

        return null;
    }

    public static String readString(String str, BufferedReader in) throws IOException {
        try {
            System.out.println(str);
            String value = in.readLine();
            if (value == null) throw new NullStringException();
            return value;
        } catch (NullStringException exc) {
            System.err.println("Fatal error, forced shutdown\n" + exc);
            System.exit(-1);
        }

        return null;
    }

    public static ReturnerPersonsParameters halfPersonCreation(BufferedReader in, CommandManager manager) {
        var currentParameters = new ReturnerPersonsParameters();
        currentParameters.coordinates = createCoordinates(in, manager);
        System.out.println("Enter eye color");
        currentParameters.eyeColor = createPersonCharacteristic(in, Color.class, manager);
        System.out.println("Enter hair color");
        currentParameters.hairColor = createPersonCharacteristic(in, Color.class, manager);
        currentParameters.nationality = createPersonCharacteristic(in, Country.class, manager);
        currentParameters.location = createLocation(in, manager);

        return currentParameters;
    }
}