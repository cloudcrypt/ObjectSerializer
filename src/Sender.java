/*
 *  
 *  
 * Daniel Dastoor
 */

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * The Sender class gets arbitrary created objects from the user, serializes them into
 * a JDOM document, and sends the document as a stream of bytes over a socket connection.
 * @author Daniel Dastoor
 */
public class Sender {

    public static void main(String[] args) {

        try {
            // Establish socket
            Socket socket = new Socket("localhost", 7777);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            System.out.println("****Connected to receiver\n");

            while (true) {
                // Get object
                System.out.println("Please create an object to transmit:\n");
                Object obj = new ObjectCreator().getObject();

                System.out.println("****Object created");
                System.out.println("****Transmitting object");

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

                System.out.println("****Object transmitted\n");

            }

        } catch (IOException e) { }
    }

}
