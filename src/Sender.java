import TestClasses.*;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Sender {

    public static void main(String[] args) {

        try {
            // Establish socket
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            // get object from creator

            TestClass5 obj = new TestClass5();

            obj.field1 = 1234;
            obj.field5 = new int[]{7, 7, 7};

            TestClass7 testClass7 = new TestClass7();
            testClass7.field15 = "Test Class 7 Test String";

            obj.field14 = testClass7;

            TestClass5 testClass5 = new TestClass5();
            testClass5.field2 = "Test Class 5 Test String";

            obj.field15 = testClass5;

            while (true) {
                // Serialize
                Serializer serializer = new Serializer();
                Document document = serializer.serialize(obj);

                String docString = new XMLOutputter(Format.getPrettyFormat()).outputString(document);

                // Send docString over socket
                byte[] data = docString.getBytes();
                dataOutputStream.writeInt(data.length);
                dataOutputStream.flush();
                dataOutputStream.write(data);
                dataOutputStream.flush();

            }

        } catch (IOException e) {
            System.out.println();
        }
    }

}
