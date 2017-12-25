/*
 * DeserializerTest
 * Daniel Dastoor
 */

import TestClasses.*;
import org.jdom2.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Deserializer.
 */
public class DeserializerTest {

    @Test
    public void fieldsTest() {
        TestClass5 obj = new TestClass5();

        obj.field1 = 1234;
        obj.field5 = new int[] { 7, 7, 7 };

        TestClass7 testClass7 = new TestClass7();
        testClass7.field15 = "Test Class 7 Test String";

        obj.field14 = testClass7;

        TestClass5 testClass5 = new TestClass5();
        testClass5.field2 = "Test Class 5 Test String";

        obj.field15 = testClass5;

        Serializer serializer = new Serializer();
        Document document = serializer.serialize(obj);
        Deserializer deserializer = new Deserializer();
        TestClass5 deserialized = (TestClass5)deserializer.deserialize(document);

        assertEquals(obj.field1, deserialized.field1);
        assertEquals(obj.field2, deserialized.field2);
        assertEquals(obj.field3, deserialized.field3);
        assertArrayEquals(obj.field5, deserialized.field5);
        assertEquals(obj.field6, deserialized.field6);
        assertEquals(obj.field7, deserialized.field7);
        assertEquals(obj.field8, deserialized.field8);
        assertEquals(obj.field9, deserialized.field9);
        assertEquals(obj.field10, deserialized.field10);
        assertEquals(obj.field11, deserialized.field11);
        assertEquals(obj.field12, deserialized.field12);
        assertEquals(obj.field13, deserialized.field13);
        assertEquals(obj.field14.field15, deserialized.field14.field15);
        assertEquals(obj.field15.field2, deserialized.field15.field2);
    }

    @Test
    public void inheritedFieldsTest() {
        TestClass6 obj = new TestClass6();

        obj.field1 = 1234;
        obj.field15 = "TEST TEST TEST";
        obj.field5 = new int[] { 7, 7, 7 };

        Serializer serializer = new Serializer();
        Document document = serializer.serialize(obj);
        Deserializer deserializer = new Deserializer();
        TestClass6 deserialized = (TestClass6)deserializer.deserialize(document);

        assertEquals(obj.field1, deserialized.field1);
        assertEquals(obj.field2, deserialized.field2);
        assertEquals(obj.field3, deserialized.field3);
        assertArrayEquals(obj.field5, deserialized.field5);
        assertEquals(obj.field6, deserialized.field6);
        assertEquals(obj.field7, deserialized.field7);
        assertEquals(obj.field8, deserialized.field8);
        assertEquals(obj.field9, deserialized.field9);
        assertEquals(obj.field10, deserialized.field10);
        assertEquals(obj.field11, deserialized.field11);
        assertEquals(obj.field12, deserialized.field12);
        assertEquals(obj.field13, deserialized.field13);
    }

}
