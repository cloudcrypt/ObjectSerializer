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

            ArrayList<Field> fields = new ArrayList<>();
            Class current = cls;
            while (current.getSuperclass() != null) {
                fields.addAll(Arrays.stream(current.getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList()));
                current = current.getSuperclass();
            }
            fields.forEach(f -> f.setAccessible(true));

            for (Field f : fields) {
                Element fieldElement = new Element("field");
                objElement.addContent(fieldElement);

                fieldElement.setAttribute("name", f.getName());
                fieldElement.setAttribute("declaringclass", f.getDeclaringClass().getName());

                Class fieldCls = f.getType();
                if (isPrimitiveOrWrapper(fieldCls)) {
                    try {
                        Object value = f.get(obj);
                        if (value.getClass().equals(Character.class) && (value.equals('\u0000')))
                            value = "null";
                        fieldElement.addContent(new Element("value").setText(value.toString()));
                    } catch (IllegalAccessException e) { }
                } else if (fieldCls.isArray()) {

                } else {

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
                id = maxInt.getAsInt();
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

}
