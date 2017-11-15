import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class Deserializer {

    private HashMap<Integer, Object> objects = new HashMap<>();

    public Object deserialize(Document document) {
        Element rootElement = document.getRootElement();
        List<Element> objElements = rootElement.getChildren();

        for (Element objElement : objElements) {
            try {
                Class objCls = Class.forName(objElement.getAttribute("class").getValue());
                int id = objElement.getAttribute("id").getIntValue();
                Object obj;

                if (objCls.isArray()) {

                    Class componentType = objCls.getComponentType();
                    int length = objElement.getAttribute("length").getIntValue();
                    obj = Array.newInstance(componentType, length);

                } else {

                    // check if objCls is Integer.class or similar, and if so, get the constructor with
                    // that argument, since no "no arg constructor" seems to exist.
                    // ex: Integer.class.getDeclaredConstructor(new Class[] {int.class});
                    // ex: Character.class.getDeclaredConstructor(new Class[] {char.class});
                    // etc.
                    Constructor c = objCls.getDeclaredConstructor(new Class[] {});
                    c.setAccessible(true);
                    obj = c.newInstance(new Object[] {});

                }

                objects.put(id, obj);

            } catch (Exception e) { }
        }

        for (Element objElement : objElements) {

            try {
                int id = objElement.getAttribute("id").getIntValue();
                Object obj = objects.get(id);
                Class objCls = obj.getClass();

                if (objCls.isArray()) {

//                    Class componentType = objCls.getComponentType();
//                    int length = objElement.getAttribute("length").getIntValue();
//                    obj = Array.newInstance(componentType, length);

                    // find the element type with getComponentType()
                    // iterate through each element of the array
                        // set the element's value using Array.set()
                        // as below, treat primitives differently than references

                } else {

                    List<Element> fieldElements = objElement.getChildren();

                    for (Element fieldElement : fieldElements) {

                        Class declaringCls = Class.forName(fieldElement.getAttribute("declaringclass").getValue());
                        Field field = declaringCls.getDeclaredField(fieldElement.getAttribute("name").getValue());
                        field.setAccessible(true);
                        Class fieldCls = field.getType();

                        if (fieldCls.isPrimitive()) {
                            field.set(obj, stringToPrimitive(fieldElement.getChild("value").getText(), fieldCls));
                        } else {
                            field.set(obj, objects.get(Integer.parseInt(fieldElement.getChild("reference").getText())));
                        }

                    }


                }

            } catch (Exception e) { }
        }

        return objects.get(0);
    }

    private Object stringToPrimitive(String str, Class primitiveType) {
        if (primitiveType.equals(int.class)) {
            return Integer.parseInt(str);
        } else if (primitiveType.equals(char.class)) {
            return str.charAt(0);
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