import TestClasses.TestClass1;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializerTest {

    @Test
    public void emptyClassTest() {
        TestClass1 obj = new TestClass1();
        Serializer serializer = new Serializer();
        Document document = serializer.serialize(obj);
        String str = new XMLOutputter(Format.getPrettyFormat()).outputString(document);
        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<serialized>\n" +
                "  <object class=\"TestClasses.TestClass1\" id=\"0\" />\n" +
                "</serialized>\n";
        assertEquals(str.replace("\r\n", "\n"), expected.replace("\r\n", "\n"));
    }

}
