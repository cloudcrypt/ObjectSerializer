/*
 *  
 * Assignment #2
 * Daniel Dastoor
 *  
 */

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * The Inspector class implements a reflective object inspector that does a complete introspection
 * of an object at runtime.
 * @author Daniel Dastoor
 */
public class Inspector {

    private int indentLevel = 0;

    private ArrayList<Object> inspected = new ArrayList<>();
    private ArrayList<Object> queue;

    private Object obj;

    private boolean recursive;

    /**
     * Default constructor to initialize queue
     */
    public Inspector() {
        queue = new ArrayList<>();
    }

    /**
     * Performs the reflective introspection on an object, printing
     * the result to standard output
     * @param obj Object to perform introspection on
     * @param recursive boolean value to toggle recursive introspection
     */
    public void inspect(Object obj, boolean recursive) {
        System.out.println("---------------------------------------");
        this.recursive = recursive;
        queue.add(obj);
        /*
         * Utilizes an ArrayList as a queue, to keep track of remaining objects
         * that need introspection.
         * To begin, removes first object, and performs introspection.
         */
        while (!queue.isEmpty()) {
            this.obj = queue.remove(0);
            inspected.add(this.obj);
            /*
             * Inspects information about the object, wrapped between increments
             * and decrements of the indentLevel, to set the appropriate amount
             * of indenting in the output.
             */
            Class cls = this.obj.getClass();
            print("Inspecting object: %s %s\n", cls.getName(), System.identityHashCode(this.obj));

            print("Class: %s", cls.getName());
//            print("Immediate Superclass: %s", cls.getSuperclass().getName());
//
//            print("Implemented Interfaces: ");
//            indentAndExecute(1, () -> printNames(cls.getInterfaces()));

//            print("Declared Constructors:");
//            Constructor[] constructors = cls.getDeclaredConstructors();
//            indentLevel++;
//            if (!isEmpty(constructors)) {
//                Arrays.stream(constructors).forEach(this::printConstructor);
//            }
//            indentLevel--;

//            print("Declared Methods: ");
//            Method[] methods = cls.getDeclaredMethods();
//            indentLevel++;
//            if (!isEmpty(methods)) {
//                Arrays.stream(methods).forEach(this::printMethod);
//            }
//            indentLevel--;

            print("Declared Fields:");
            Field[] fields = cls.getDeclaredFields();
            indentLevel++;
            if (!isEmpty(fields)) {
                Arrays.stream(fields).forEach(this::printField);
            }
            indentLevel--;

//            // Traverse the inheritance hierarchy to find all inherited constructors
//            print("Inherited Constructors:");
//            ArrayList<Constructor> inheritedConstructors = new ArrayList<>();
//            Class superClass = cls.getSuperclass();
//            while (superClass != null) {
//                inheritedConstructors.addAll(Arrays.asList(superClass.getDeclaredConstructors()));
//                superClass = superClass.getSuperclass();
//            }
//            indentLevel++;
//            if (!isEmpty(inheritedConstructors)) {
//                inheritedConstructors.forEach(this::printConstructor);
//            }
//            indentLevel--;

//            // Traverse the inheritance hierarchy to find all inherited methods
//            print("Inherited Methods:");
//            ArrayList<Method> inheritedMethods = new ArrayList<>();
//            superClass = cls.getSuperclass();
//            while (superClass != null) {
//                inheritedMethods.addAll(Arrays.asList(superClass.getDeclaredMethods()));
//                superClass = superClass.getSuperclass();
//            }
//            indentLevel++;
//            if (!isEmpty(inheritedMethods)) {
//                inheritedMethods.forEach(this::printMethod);
//            }
//            indentLevel--;

            // Traverse the inheritance hierarchy to find all inherited fields
            print("Inherited Fields:");
            ArrayList<Field> inheritedFields = new ArrayList<>();
            Class superClass = cls.getSuperclass();
            while (superClass != null) {
                inheritedFields.addAll(Arrays.asList(superClass.getDeclaredFields()));
                superClass = superClass.getSuperclass();
            }
            indentLevel++;
            if (!isEmpty(inheritedFields)) {
                inheritedFields.forEach(this::printField);
            }
            indentLevel--;

//            // Traverse the inheritance hierarchy of superclasses and superinterfaces
//            // to find all inherited interfaces
//            print("Inherited Interfaces:");
//            ArrayList<Class> inheritedInterfaces = new ArrayList<>();
//            ArrayList<Class> superInterfaces = new ArrayList<>();
//            Class[] interfaces = cls.getInterfaces();
//            for (Class i : interfaces) {
//                superInterfaces.addAll(Arrays.asList(i.getInterfaces()));
//            }
//            superClass = cls.getSuperclass();
//            while (superClass != null)  {
//                superInterfaces.addAll(Arrays.asList(superClass.getInterfaces()));
//                superClass = superClass.getSuperclass();
//            }
//            while (superInterfaces.size() > 0) {
//                Class i = superInterfaces.remove(0);
//                inheritedInterfaces.add(i);
//                superInterfaces.addAll(Arrays.asList(i.getInterfaces()));
//            }
//            // Ensure list of inherited interfaces is unique to avoid duplicate listings
//            HashSet<Class> uniqueInheritedInterfaces = new HashSet<>(inheritedInterfaces);
//            indentAndExecute(1, () -> printNames(uniqueInheritedInterfaces.toArray(new Class[0])));

            System.out.println("---------------------------------------\n");
        }
    }

    /**
     * Introspects and prints the details of a Constructor object
     * @param c Constructor object to introspect
     */
    private void printConstructor(Constructor c) {
        print("Constructor: %s", c.getName());

        printSpecificIndent("Modifiers:", indentLevel+1);
        printSpecificIndent(Modifier.toString(c.getModifiers()), indentLevel+2);

        printSpecificIndent("Parameter Types:", indentLevel+1);
        indentAndExecute(2, () -> printNames(c.getParameterTypes()));
    }

    /**
     * Introspects and prints the details of a Method object
     * @param m Method object to introspect
     */
    private void printMethod(Method m) {
        print("Method: %s, Declared in %s", m.getName(), m.getDeclaringClass().getName());

        printSpecificIndent("Modifiers: ", indentLevel+1);
        printSpecificIndent(Modifier.toString(m.getModifiers()), indentLevel+2);

        printSpecificIndent("Return Type: ", indentLevel+1);
        indentAndExecute(2, () -> printType(m.getReturnType()));

        printSpecificIndent("Parameter Types: ", indentLevel+1);
        indentAndExecute(2, () -> printNames(m.getParameterTypes()));

        printSpecificIndent("Exceptions Thrown: ", indentLevel+1);
        indentAndExecute(2, () -> printNames(m.getExceptionTypes()));
    }

    /**
     * Introspects and prints the details of a Field object
     * @param f Field object to introspect
     */
    private void printField(Field f) {
        print("Field: %s, Declared in %s", f.getName(), f.getDeclaringClass().getName());

        printSpecificIndent("Modifiers:", indentLevel+1);
        printSpecificIndent(Modifier.toString(f.getModifiers()), indentLevel+2);

        printSpecificIndent("Type:", indentLevel+1);
        if (!f.getType().isArray()) {
            printSpecificIndent(f.getType().getName(), indentLevel+2);
        } else {
            printSpecificIndent("Array", indentLevel+2);
            printSpecificIndent("Component Type:", indentLevel+1);
            printSpecificIndent(f.getType().getComponentType().getName(), indentLevel+2);
        }

        printSpecificIndent("Value:", indentLevel+1);
        // Set field to accessible in case it is not
        f.setAccessible(true);
        try {
            Object value = f.get(obj);
            indentAndExecute(2, () -> printObjectValue(value));
        } catch (IllegalAccessException e) { }
    }

    /**
     * Introspects and prints the value of an object
     * @param value Object to introspect and print the value of
     */
    private void printObjectValue(Object value) {
        if (value == null) {
            print("null");
            return;
        }
        Class cls = value.getClass();

        if (isPrimitiveOrWrapper(cls)) {

            print(value.toString());

        } else if (cls.isArray()) {

            print("Length: %s", Array.getLength(value));
            print("Contents:");
            for (int i = 0; i < Array.getLength(value); i++) {
                Object element = Array.get(value, i);

                printSpecificIndent("Value:", indentLevel+1);
                // Recursively call printObjectValue in case object is of reference or array type
                indentAndExecute(2, () -> printObjectValue(element));
            }

        } else {

            print("Reference Value: %s %s", value.getClass().getName(), System.identityHashCode(value));
            // If in recursive mode, add object to queue for future introspection
            if (recursive) {
                  if (!queue.contains(value)) {
                      if (!inspected.contains(value)) {
                          queue.add(value);
                      }
                  }
            }

        }
    }

    /**
     * Prints the type of a Class object, taking care of array types
     * @param type Class object to print the type of
     */
    private void printType(Class type) {
        if (!type.isArray()) {
            print(type.getName());
        } else {
            print("Array of type %s", type.getComponentType().getName());
        }
    }

    /**
     * Checks if a Class object is of primitive or wrapped primitive types
     * @param type Class object to check
     * @return boolean value representing if class object is of primitive type
     */
    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || (type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class);
    }

    /**
     * Executes printType on a list of Class objects, if the
     * list is not empty
     * @param list List of Class objects to print
     */
    private void printNames(Class[] list) {
        if (!isEmpty(list)) {
            Arrays.stream(list).forEach(this::printType);
        }
    }

    /**
     * Checks if any generic array type is empty
     * @param list Array to check
     * @param <T> Type parameter of arrays
     * @return boolean value representing if array is empty
     */
    private <T> boolean isEmpty(T[] list) {
        boolean empty = list.length == 0;
        if (empty) print("None");
        return empty;
    }

    /**
     * Checks if any generic ArrayList is empty
     * @param list ArrayList to check
     * @param <T> Type parameter of arrays
     * @return boolean value representing if ArrayList is empty
     */
    private <T> boolean isEmpty(ArrayList<T> list) {
        boolean empty = list.size() == 0;
        if (empty) print("None");
        return empty;
    }

    /**
     * Prints a string of tab characters, where the
     * length of the indent string is defined by the field Inspector.indentLevel,
     * and then prints supplied the format string with the given args
     * @param format format string to print
     * @param args arguments for printf
     */
    private void print(String format, Object ... args) {
        System.out.print(getIndentStr());
        System.out.printf(format + "\n", args);
    }

    /**
     * Prints a string of tab characters, where the
     * length of the indent string is defined by the parameter indentLevel,
     * and then prints supplied the format string with the given args
     * @param format format string to print
     * @param indentLevel amount of tab characters to print
     * @param args arguments for printf
     */
    private void printSpecificIndent(String format, int indentLevel, Object ... args) {
        System.out.print(getIndentStr(indentLevel));
        System.out.printf(format + "\n", args);
    }

    /**
     * Generates an indent string of length defined by the field
     * Inspector.indentLevel
     * @return indent string
     */
    private String getIndentStr() {
        return createIndentStr(indentLevel);
    }

    /**
     * Generates an indent string of specific length
     * @param n length of indent string to generate
     * @return indent string
     */
    private String getIndentStr(int n) {
        return createIndentStr(n);
    }

    /**
     * Generates an indent string of specific length using
     * StringBuilder
     * @param n length of indent strings to generate
     * @return indent string
     */
    private String createIndentStr(int n) {
        StringBuilder indentStringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            indentStringBuilder.append('\t');
        }
        return indentStringBuilder.toString();
    }

    /**
     * Executes a lambda function after wrapping it between and increase
     * and corresponding decrease of the current indentLevel
     * @param indentAmount int amount to increase indentLevel to
     * @param action lambda function of the action to perform at given indentation level
     */
    private void indentAndExecute(int indentAmount, Runnable action) {
        indentLevel += indentAmount;
        action.run();
        indentLevel -= indentAmount;
    }
}
