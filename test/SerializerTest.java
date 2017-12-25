/*
 * SerializerTest
 * Daniel Dastoor
 */

import TestClasses.*;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Serializer.
 */
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

    @Test
    public void fieldsTest() {
        TestClass5 obj = new TestClass5();
        Serializer serializer = new Serializer();
        Document document = serializer.serialize(obj);
        String str = new XMLOutputter(Format.getPrettyFormat()).outputString(document);
        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<serialized>\n" +
                        "  <object class=\"TestClasses.TestClass5\" id=\"0\">\n" +
                        "    <field name=\"field1\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <value>42</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field2\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <reference>1</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field3\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <value>z</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field5\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <reference>2</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field6\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <value>0.0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field7\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <value>0.0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field8\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field9\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field10\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <value>true</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field11\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <value>15</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field12\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <reference>3</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field13\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <reference>4</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field14\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <reference>5</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field15\" declaringclass=\"TestClasses.TestClass5\">\n" +
                        "      <reference>0</reference>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"java.lang.String\" id=\"1\">\n" +
                        "    <field name=\"value\" declaringclass=\"java.lang.String\">\n" +
                        "      <reference>6</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"hash\" declaringclass=\"java.lang.String\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"[I\" id=\"2\" length=\"5\">\n" +
                        "    <value>1</value>\n" +
                        "    <value>2</value>\n" +
                        "    <value>3</value>\n" +
                        "    <value>4</value>\n" +
                        "    <value>5</value>\n" +
                        "  </object>\n" +
                        "  <object class=\"java.lang.Integer\" id=\"3\">\n" +
                        "    <field name=\"value\" declaringclass=\"java.lang.Integer\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"java.lang.Float\" id=\"4\">\n" +
                        "    <field name=\"value\" declaringclass=\"java.lang.Float\">\n" +
                        "      <value>0.0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"TestClasses.TestClass7\" id=\"5\">\n" +
                        "    <field name=\"field14\" declaringclass=\"TestClasses.TestClass7\">\n" +
                        "      <value>43</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field15\" declaringclass=\"TestClasses.TestClass7\">\n" +
                        "      <reference>7</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field16\" declaringclass=\"TestClasses.TestClass7\">\n" +
                        "      <value>y</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"[C\" id=\"6\" length=\"12\">\n" +
                        "    <value>H</value>\n" +
                        "    <value>e</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>o</value>\n" +
                        "    <value />\n" +
                        "    <value>W</value>\n" +
                        "    <value>o</value>\n" +
                        "    <value>r</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>d</value>\n" +
                        "    <value>!</value>\n" +
                        "  </object>\n" +
                        "  <object class=\"java.lang.String\" id=\"7\">\n" +
                        "    <field name=\"value\" declaringclass=\"java.lang.String\">\n" +
                        "      <reference>8</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"hash\" declaringclass=\"java.lang.String\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"[C\" id=\"8\" length=\"16\">\n" +
                        "    <value>2</value>\n" +
                        "    <value>n</value>\n" +
                        "    <value>d</value>\n" +
                        "    <value />\n" +
                        "    <value>H</value>\n" +
                        "    <value>e</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>o</value>\n" +
                        "    <value />\n" +
                        "    <value>W</value>\n" +
                        "    <value>o</value>\n" +
                        "    <value>r</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>d</value>\n" +
                        "    <value>!</value>\n" +
                        "  </object>\n" +
                        "</serialized>\n";
        assertEquals(str.replace("\r\n", "\n"), expected.replace("\r\n", "\n"));
    }

    @Test
    public void inheritedFieldsTest() {
        TestClass6 obj = new TestClass6();
        Serializer serializer = new Serializer();
        Document document = serializer.serialize(obj);
        String str = new XMLOutputter(Format.getPrettyFormat()).outputString(document);
        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<serialized>\n" +
                        "  <object class=\"TestClasses.TestClass6\" id=\"0\">\n" +
                        "    <field name=\"field1\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <value>42</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field2\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <reference>1</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field3\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <value>z</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field5\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <reference>2</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field6\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <value>0.0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field7\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <value>0.0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field8\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field9\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field10\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <value>true</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field11\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <value>15</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field12\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <reference>3</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field13\" declaringclass=\"TestClasses.TestClass6\">\n" +
                        "      <reference>4</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field14\" declaringclass=\"TestClasses.TestClass7\">\n" +
                        "      <value>43</value>\n" +
                        "    </field>\n" +
                        "    <field name=\"field15\" declaringclass=\"TestClasses.TestClass7\">\n" +
                        "      <reference>5</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"field16\" declaringclass=\"TestClasses.TestClass7\">\n" +
                        "      <value>y</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"java.lang.String\" id=\"1\">\n" +
                        "    <field name=\"value\" declaringclass=\"java.lang.String\">\n" +
                        "      <reference>6</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"hash\" declaringclass=\"java.lang.String\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"[I\" id=\"2\" length=\"5\">\n" +
                        "    <value>1</value>\n" +
                        "    <value>2</value>\n" +
                        "    <value>3</value>\n" +
                        "    <value>4</value>\n" +
                        "    <value>5</value>\n" +
                        "  </object>\n" +
                        "  <object class=\"java.lang.Integer\" id=\"3\">\n" +
                        "    <field name=\"value\" declaringclass=\"java.lang.Integer\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"java.lang.Float\" id=\"4\">\n" +
                        "    <field name=\"value\" declaringclass=\"java.lang.Float\">\n" +
                        "      <value>0.0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"java.lang.String\" id=\"5\">\n" +
                        "    <field name=\"value\" declaringclass=\"java.lang.String\">\n" +
                        "      <reference>7</reference>\n" +
                        "    </field>\n" +
                        "    <field name=\"hash\" declaringclass=\"java.lang.String\">\n" +
                        "      <value>0</value>\n" +
                        "    </field>\n" +
                        "  </object>\n" +
                        "  <object class=\"[C\" id=\"6\" length=\"12\">\n" +
                        "    <value>H</value>\n" +
                        "    <value>e</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>o</value>\n" +
                        "    <value />\n" +
                        "    <value>W</value>\n" +
                        "    <value>o</value>\n" +
                        "    <value>r</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>d</value>\n" +
                        "    <value>!</value>\n" +
                        "  </object>\n" +
                        "  <object class=\"[C\" id=\"7\" length=\"16\">\n" +
                        "    <value>2</value>\n" +
                        "    <value>n</value>\n" +
                        "    <value>d</value>\n" +
                        "    <value />\n" +
                        "    <value>H</value>\n" +
                        "    <value>e</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>o</value>\n" +
                        "    <value />\n" +
                        "    <value>W</value>\n" +
                        "    <value>o</value>\n" +
                        "    <value>r</value>\n" +
                        "    <value>l</value>\n" +
                        "    <value>d</value>\n" +
                        "    <value>!</value>\n" +
                        "  </object>\n" +
                        "</serialized>\n";
        assertEquals(str.replace("\r\n", "\n"), expected.replace("\r\n", "\n"));
    }

}
