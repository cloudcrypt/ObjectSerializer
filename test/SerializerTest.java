import TestClasses.*;
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

    @Test
    public void emptyFieldsTest() {
        TestClass2 obj = new TestClass2();
        Serializer serializer = new Serializer();
        Document document = serializer.serialize(obj);
        String str = new XMLOutputter(Format.getPrettyFormat()).outputString(document);
        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<serialized>\n" +
                        "  <object class=\"TestClasses.TestClass2\" id=\"0\">\n" +
                        "    <field name=\"field1\" declaringclass=\"TestClasses.TestClass2\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field3\" declaringclass=\"TestClasses.TestClass2\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "</serialized>\n";
        assertEquals(str.replace("\r\n", "\n"), expected.replace("\r\n", "\n"));
    }

    @Test
    public void emptyInheritedFieldsTest() {
        TestClass3 obj = new TestClass3();
        Serializer serializer = new Serializer();
        Document document = serializer.serialize(obj);
        String str = new XMLOutputter(Format.getPrettyFormat()).outputString(document);
        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<serialized>\n" +
                        "  <object class=\"TestClasses.TestClass3\" id=\"0\">\n" +
                        "    <field name=\"field1\" declaringclass=\"TestClasses.TestClass3\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field3\" declaringclass=\"TestClasses.TestClass3\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field5\" declaringclass=\"TestClasses.TestClass3\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field5\" declaringclass=\"TestClasses.TestClass4\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field7\" declaringclass=\"TestClasses.TestClass4\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "</serialized>\n";
        assertEquals(str.replace("\r\n", "\n"), expected.replace("\r\n", "\n"));
    }

}
