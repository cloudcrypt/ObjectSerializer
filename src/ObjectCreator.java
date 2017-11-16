import CreatableClasses.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ObjectCreator {

    private Scanner input = new Scanner(System.in);
    private ArrayList<Object> objects = new ArrayList<>();

    public Object getObject() {

        Object obj = getUserObject();
        objects.add(obj);
        setFields(obj);
        return obj;
    }

    private void displayObjectMenu() {
        System.out.println("Creatable Objects:");
        System.out.println("\t1) Simple object (Primitive fields only)");
        System.out.println("\t2) Object with object references");
        System.out.println("\t3) Object with array of primitives");
        System.out.println("\t4) Object with array of object references");
        System.out.println("\t5) Object with collection class object");
    }

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
//            case 2:
//
//                break;
//
//            case 3:
//
//                break;
//            case 4:
//
//                break;
        }
        return null;
    }

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
                            System.out.printf("Enter value for %s (Type: %s, Class: %s): ", field.getName(), fieldCls, field.getDeclaringClass().getName());
                            String arrayInput = input.nextLine();
                            Array.set(obj, i, stringToPrimitive(arrayInput, componentType));
                        }
                    } else {
                        for (int i = 0; i < 3; i++) {
                            System.out.printf("Creating/Selecting %s object for field '%s':\n", fieldCls, field.getName());
                            Array.set(obj, i, createObject(fieldCls));
                        }
                    }
                    field.set(obj, array);
                } else {
                    System.out.printf("Creating/Selecting %s object for field '%s':\n", fieldCls, field.getName());
                    field.set(obj, createObject(fieldCls));
                }
            }
            System.out.println("--------------------------------------------------------");
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) { }
    }

    private Object createObject(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        System.out.println("-----Select/Create object of type " + cls.getName() + "-----");
        Object[] validObjects = objects.stream().filter(o -> o.getClass().equals(cls)).toArray();
        Object returnObj;
        int n = 0;
        for (Object validObject : validObjects) {
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
        if (choice == n) {
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
