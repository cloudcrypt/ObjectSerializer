import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map.Entry;
import java.util.OptionalInt;
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

            IdentifiedObject nextObj = queue.remove(0);
            this.obj = nextObj.obj;
            int id = nextObj.id;

            Class cls = this.obj.getClass();
            objElement.setAttribute("class", cls.getName());
            objElement.setAttribute("id", Integer.toString(id));


            root.addContent(objElement);
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

}
