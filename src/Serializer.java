/*
 * Serializer
 * Daniel Dastoor
 */

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.jdom2.Document;
import org.jdom2.Element;

/**
 * The Serializer class implements an object to XML Document serializer
 * @author Daniel Dastoor
 */
public class Serializer {

    private IdentityHashMap<Integer, Object> objects = new IdentityHashMap<>();

    private ArrayList<IdentifiedObject> queue = new ArrayList<>();

    private Object obj;

    /**
     * Serialize serializes an object and all of its fields recursively
     * @param obj Object to serialize
     * @return XML Document of serialized object
     */
    public org.jdom2.Document serialize(Object obj) {
        Element root = new Element("serialized");
        Document document = new Document(root);
        processObject(obj);

        while (!queue.isEmpty()) {
            Element objElement = new Element("object");
            root.addContent(objElement);

            IdentifiedObject nextObj = queue.remove(0);
            this.obj = nextObj.obj;
            int id = nextObj.id;

            Class cls = this.obj.getClass();
            objElement.setAttribute("class", cls.getName());
            objElement.setAttribute("id", Integer.toString(id));
            int arrayLen = 0;
            if (cls.isArray()) {
                arrayLen = Array.getLength(this.obj);
                objElement.setAttribute("length", Integer.toString(arrayLen));
            }

            if (cls.isArray()) {
                Class componentType = cls.getComponentType();
                if (componentType.isPrimitive()) {
                    for (int i = 0; i < arrayLen; i++) {
                        Object value = Array.get(this.obj, i);
                        processPrimitiveEntry(value, objElement);
                    }
                } else {
                    for (int i = 0; i < arrayLen; i++) {
                        Object value = Array.get(this.obj, i);
                        if (value != null) {
                            processObjectEntry(value, objElement);
                        }
                    }
                }
            } else {
                ArrayList<Field> fields = new ArrayList<>();
                Class current = cls;
                while (current.getSuperclass() != null) {
                    fields.addAll(Arrays.stream(current.getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList()));
                    current = current.getSuperclass();
                }
                fields.forEach(f -> f.setAccessible(true));

                for (Field f : fields) {
                    Element fieldElement = new Element("field");

                    fieldElement.setAttribute("name", f.getName());
                    fieldElement.setAttribute("declaringclass", f.getDeclaringClass().getName());

                    Class fieldCls = f.getType();
                    try {
                        if (fieldCls.isPrimitive()) {
                            Object value = f.get(this.obj);
                            processPrimitiveEntry(value, fieldElement);
                            objElement.addContent(fieldElement);
                        } else {
                            Object value = f.get(this.obj);
                            if (value != null) {
                                processObjectEntry(value, fieldElement);
                                objElement.addContent(fieldElement);
                            }
                        }
                    } catch (IllegalAccessException e) { }

                }
            }


        }

        return document;
    }

    /**
     * Processes the value of a primitive object, and
     * appropriately serializes it into a "value" element
     * @param value Value object to process
     * @param element Element to append value element to
     */
    private void processPrimitiveEntry(Object value, Element element) {
        if (value.getClass().equals(Character.class) && (value.equals('\u0000')))
            value = 0;
        element.addContent(new Element("value").setText(value.toString()));
    }

    /**
     * Processes the value of an object, and
     * appropriately serializes it into a "reference" element
     * @param value Value object to process
     * @param element Element to append reference element to
     */
    private void processObjectEntry(Object value, Element element) {
        int fieldObjId = processObject(value);
        element.addContent(new Element("reference").setText(Integer.toString(fieldObjId)));
    }

    /**
     * Processes an object, getting it's ID if it has been serialized, and adding it to the
     * serialization queue if not
     * @param obj Object to process
     * @return Assigned ID of object
     */
    private int processObject(Object obj) {
        int id = 0;
        if (objects.containsValue(obj)) {
            for (Entry<Integer, Object> entry : objects.entrySet()) {
                if (entry.getValue() == obj) {
                    id = entry.getKey();
                }
            }
        } else {
            OptionalInt maxInt = objects.keySet().stream().mapToInt(Integer::intValue).max();
            if (maxInt.isPresent()) {
                id = maxInt.getAsInt() + 1;
            }
            objects.put(id, obj);
            queue.add(new IdentifiedObject(id, obj));
        }
        return id;
    }

    /**
     * Simple class to handle ID/Object pairs.
     */
    private class IdentifiedObject {
        int id;
        Object obj;
        IdentifiedObject(int id, Object obj) {
            this.id = id;
            this.obj = obj;
        }
    }

}
