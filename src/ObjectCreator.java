/*
 * ObjectCreator
 * Daniel Dastoor
 */

import CreatableClasses.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The ObjectCreator allows a user to create arbitrary objects through a text-based
 * menu system.
 * @author Daniel Dastoor
 */
public class ObjectCreator {

    private Scanner input = new Scanner(System.in);
    private ArrayList<Object> objects = new ArrayList<>();

    /**
     * Gets a fully initialized object from the user's choice
     * @return Object
     */
    public Object getObject() {
        Object obj = getUserObject();
        objects.add(obj);
        setFields(obj);
        return obj;
    }

    /**
     * Displays a menu of the possible objects for creation.
     */
    private void displayObjectMenu() {
        System.out.println("Creatable Objects:");
        System.out.println("\t1) Simple object (Primitive fields only)");
        System.out.println("\t2) Object with object references");
        System.out.println("\t3) Object with arrays of primitives");
        System.out.println("\t4) Object with arrays of object references");
        System.out.println("\t5) Object with collection class object");
    }

    /**
     * Gets the object based on the user's menu selection
     * @return Object
     */
    private Object getUserObject() {
        displayObjectMenu();
        int choice = 0;
        do {
            System.out.print("Selection: ");
            while(!input.hasNextInt()) {
                System.out.println("Please enter a number between 1 and 5.");
                input.next();
            }
            choice = input.nextInt();
        } while ((choice < 1) || (choice > 5));
        input.nextLine();
        switch (choice) {
            case 1:
                return new SimpleClass();
            case 2:
                return new ObjReferenceClass();
            case 3:
                return new PrimitiveArrayClass();
            case 4:
                return new ObjectArrayClass();
            case 5:
                return new CollectionReferenceClass();
        }
        return null;
    }

    /**
     * Recursively allows the user to set the value of all fields in the object.
     * Supports fields of primitive type, object type, array type, and ArrayList type.
     * @param obj Object to set the fields in
     */
    private void setFields(Object obj) {
        try {
            System.out.println("-----Setting fields for object " + System.identityHashCode(obj) + " (" + obj.getClass().getName() + ")-----");
            Field[] fields = obj.getClass().getFields();

            for (Field field : fields) {
                Class fieldCls = field.getType();
                if (fieldCls.isPrimitive()) {
                    System.out.printf("Enter value for %s (Type: %s, Class: %s): ", field.getName(), fieldCls, field.getDeclaringClass().getName());
                    String userInput = input.nextLine();
                    field.set(obj, stringToPrimitive(userInput, field.getType()));
                } else if (fieldCls.isArray()) {
                    Class componentType = fieldCls.getComponentType();
                    Object array = Array.newInstance(componentType, 3);
                    if (componentType.isPrimitive()) {
                        for (int i = 0; i < 3; i++) {
                            System.out.printf("Enter value %d/3 for %s (Type: %s, Class: %s): ", i+1, field.getName(), componentType.getName(), field.getDeclaringClass().getName());
                            String arrayInput = input.nextLine();
                            Array.set(array, i, stringToPrimitive(arrayInput, componentType));
                        }
                    } else {
                        for (int i = 0; i < 3; i++) {
                            System.out.printf("Creating/Selecting %s object for index %d/3 of field '%s':\n", componentType.getName(), i+1, field.getName());
                            Array.set(array, i, createObject(componentType));
                        }
                    }
                    field.set(obj, array);
                } else if (fieldCls.equals(ArrayList.class)) {
                    System.out.printf("Creating/Selecting %s object for field '%s':\n", fieldCls, field.getName());
                    ArrayList<Object> arrayListObj = (ArrayList<Object>)createObject(fieldCls);
                    for (int i = 0; i < 3; i++) {
                        System.out.printf("Creating/Selecting object for index %d/3 of field '%s':\n", i+1, field.getName());
                        Object arrayListEntry = createObject();
                        arrayListObj.add(arrayListEntry);
                    }
                    field.set(obj, arrayListObj);
                } else {
                    System.out.printf("Creating/Selecting %s object for field '%s':\n", fieldCls, field.getName());
                    field.set(obj, createObject(fieldCls));
                }
            }
            System.out.println("--------------------------------------------------------");
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) { }
    }

    /**
     * Creates a new object of a specific class type based on the user's selection.
     * User can select from among all previously created objects of the class type, or
     * create a new object of that type.
     * @param cls Type of object to select or be created
     * @return Selected or created object
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object createObject(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        System.out.println("-----Select/Create object of type " + cls.getName() + "-----");
        Object[] validObjects = objects.stream().filter(o -> o.getClass().equals(cls)).toArray();
        Object returnObj;
        int choice = getSelectionFromObjects(validObjects);
        if (choice == (validObjects.length + 1)) {
            Object fieldObj = cls.getDeclaredConstructor(new Class[] {}).newInstance(new Object[] {});
            objects.add(fieldObj);
            setFields(fieldObj);
            returnObj = fieldObj;
        } else {
            returnObj = validObjects[choice - 1];
        }
        System.out.println("-----Selected/Created object of type " + cls.getName() + "-----");
        return returnObj;
    }

    /**
     * Displays a menu for the user to select from a list of objects, or select to create
     * a new object.
     * @param objects Objects to display in list
     * @return Integer menu choice number
     */
    private int getSelectionFromObjects(Object[] objects) {
        int n = 0;
        for (Object validObject : objects) {
            System.out.printf("\t%d) %s (%s)\n", ++n, System.identityHashCode(validObject), validObject.getClass().getName());
        }
        System.out.printf("\t%d) Create new object\n", ++n);
        int choice = 0;
        do {
            System.out.print("Selection: ");
            while(!input.hasNextInt()) {
                System.out.printf("Please enter a number between 1 and %d.\n", n);
                input.next();
            }
            choice = input.nextInt();
        } while ((choice < 1) || (choice > n));
        input.nextLine();
        return choice;
    }

    /**
     * Creates a new object based on the user's selection.
     * User can select from among all previously created objects, or
     * create a new object from the possible object types.
     * @return Selected or created object
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object createObject() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        System.out.println("-----Select/Create object-----");
        Object[] validObjects = objects.toArray();
        Object returnObj;
        int choice = getSelectionFromObjects(validObjects);
        if (choice == (validObjects.length + 1)) {
            Object newObj = getUserObject();
            objects.add(newObj);
            setFields(newObj);
            returnObj = newObj;
        } else {
            returnObj = validObjects[choice - 1];
        }
        System.out.println("-----Selected/Created object-----");
        return returnObj;
    }

    /**
     * Converts a string to a specified primitive object type
     * @param str String to convert
     * @param primitiveType Primitive type to convert string to
     * @return Converted primitive object
     */
    private Object stringToPrimitive(String str, Class primitiveType) {
        if (primitiveType.equals(int.class)) {
            return Integer.parseInt(str);
        } else if (primitiveType.equals(char.class)) {
            return (str.length() == 0) ? ' ' : str.charAt(0);
        } else if (primitiveType.equals(float.class)) {
            return Float.parseFloat(str);
        } else if (primitiveType.equals(boolean.class)) {
            return Boolean.parseBoolean(str);
        } else if (primitiveType.equals(byte.class)) {
            return Byte.parseByte(str);
        } else if (primitiveType.equals(short.class)) {
            return Short.parseShort(str);
        } else if (primitiveType.equals(long.class)) {
            return Long.parseLong(str);
        } else if (primitiveType.equals(double.class)) {
            return Double.parseDouble(str);
        } else {
            return null;
        }
    }

}
