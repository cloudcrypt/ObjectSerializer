import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            System.out.println("*****Waiting for connection");
            // Establish socket
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            while (true) {
                System.out.println("*****Waiting for serialized object");
                int len = dataInputStream.readInt();
                byte[] data = new byte[len];
                int read = 0;
                while (read != len) {
                    read += dataInputStream.read(data, read, len - read);
                }

                SAXBuilder builder = new SAXBuilder();
                Document xmlDoc = builder.build(new ByteArrayInputStream(data));

                Deserializer deserializer = new Deserializer();
                Object deserialized = deserializer.deserialize(xmlDoc);

                System.out.println("**********Receiving serialized object**********");

                new Inspector().inspect(deserialized, true);

                System.out.println("**********End of serialized object*************");
            }

        } catch (IOException | JDOMException e) {
            System.out.println();
        }
    }

}
