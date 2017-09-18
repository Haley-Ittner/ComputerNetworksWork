import java.io.*;
import java.net.ServerSocket;

/**
 * Created by Haley on 2/15/2017.jkkjklj
 */
public class Homework3MultiServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverTCPSocket = null;
        boolean listening = true;

        try {
            serverTCPSocket = new ServerSocket(5100);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5100.");
            System.exit(-1);
        }

        while (listening){
            new Homework3Server(serverTCPSocket.accept()).start();
        }

        serverTCPSocket.close();
    }
}