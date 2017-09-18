import java.net.*;
import java.io.*;

public class Homework4MultiServer {
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
	    		new Homework4Server(serverTCPSocket.accept()).start();
		  }
			
        serverTCPSocket.close();
    }
}