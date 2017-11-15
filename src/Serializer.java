import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.jdom2.Document;
import org.jdom2.Element;

public class Serializer {

    private IdentityHashMap<Integer, Object> objects = new IdentityHashMap<>();

    private ArrayList<IdentifiedObject> queue = new ArrayList<>();

    private Object obj;

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
                        if (value.getClass().equals(Character.class) && (value.equals('\u0000')))
                            value = 0;
                        objElement.addContent(new Element("value").setText(value.toString()));
                    }
                } else {
                    for (int i = 0; i < arrayLen; i++) {
                        Object value = Array.get(this.obj, i);
                        if (value != null) {
                            int fieldObjId = processObject(value);
                            objElement.addContent(new Element("reference").setText(Integer.toString(fieldObjId)));
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
                    if (fieldCls.isPrimitive()) {
                        try {
                            Object value = f.get(this.obj);
                            if (value.getClass().equals(Character.class) && (value.equals('\u0000')))
                                value = 0;
                            fieldElement.addContent(new Element("value").setText(value.toString()));
                            objElement.addContent(fieldElement);
                        } catch (IllegalAccessException e) { }
                    } else {
                        try {
                            Object value = f.get(this.obj);
                            if (value != null) {
                                int fieldObjId = processObject(value);
                                fieldElement.addContent(new Element("reference").setText(Integer.toString(fieldObjId)));
                                objElement.addContent(fieldElement);
                            }
                        } catch (IllegalAccessException e) { }
                    }

                }
            }


        }

        return document;
    }

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

    private class IdentifiedObject {
        int id;
        Object obj;
        IdentifiedObject(int id, Object obj) {
            this.id = id;
            this.obj = obj;
        }
    }

}
