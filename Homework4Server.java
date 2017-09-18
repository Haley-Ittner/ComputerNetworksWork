import java.net.*;
import java.io.*;

public class Homework4Server extends Thread {
    private Socket clientTCPSocket = null;

    public Homework4Server(Socket socket) {
		super("Homework4Server");
		clientTCPSocket = socket;
    }

    public void run() {
    int count = 0;
    String[] message;
    boolean quit = false;
		try {
	 	   PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
	  		BufferedReader cSocketIn = new BufferedReader(
				    new InputStreamReader(
				    clientTCPSocket.getInputStream()));

	      String fromClient, toClient = "";
			cSocketOut.println("220 " + clientTCPSocket.getLocalAddress().toString());
           
	 	   while ((fromClient = cSocketIn.readLine()) != null && !quit) {   
                              
            do {
               if (fromClient == null) {
                  break;
               } else {
                  message = fromClient.split(" ");
                  if (message[0].equals("HELO")) {
                     cSocketOut.println("250 " + clientTCPSocket.getLocalAddress().toString() + 
                                        " Hello " + message[1]);
                  }else {
                     cSocketOut.println("503 5.5.2 Send hello first");
                  }
               }
                  fromClient = cSocketIn.readLine();    
            } while (!message[0].equals("HELO"));                  
            
            do {
               if (fromClient == null) {
                  break;
               } else {
                  message = fromClient.split(" ");
                  if (message[0].equals("MAIL") && message[1].equals("FROM")) {
                     cSocketOut.println("250 2.1.0 Sender OK");
                  } else {
                     cSocketOut.println("503 5.5.2 Need mail command");
                  }
               }
               fromClient = cSocketIn.readLine();
            } while (!message[0].equals("MAIL") || !message[1].equals("FROM"));
            
            do {
               if (fromClient == null) {
                  break;
               } else {
                  message = fromClient.split(" ");
                  if (message[0].equals("RCPT") && message[1].equals("TO")) {
                     cSocketOut.println("250 2.1.5 Recipient OK");
                  } else {
                     cSocketOut.println("503 5.5.2 Need rcpt command");
                  } 
               }
               fromClient = cSocketIn.readLine();
            } while (!message[0].equals("RCPT") || !message[1].equals("TO"));
            
            do {
               if (fromClient == null) {
                  break;
               } else {
                  message = fromClient.split(" ");
                  if (message[0].equals("DATA")) {
                     cSocketOut.println("354 Start mail input; end with <CRLF>.<CRLF>");
                     fromClient = cSocketIn.readLine();
                     while (!fromClient.equals(".")) {
                        //In client, do as an array
                        System.out.println(fromClient);
                        fromClient = cSocketIn.readLine();
                     }
                     cSocketOut.println("250 Message received and to be delivered");
                  } else {
                     cSocketOut.println("503 5.5.2 Need data command");
                  }
               }
               fromClient = cSocketIn.readLine();
            } while (!message.equals("DATA"));
				
				if (fromClient.equals("QUIT")) {
               cSocketOut.println("221 " + clientTCPSocket.getLocalAddress().toString() + " closing connection"); 
               quit = true;  
				   break;
            }
            fromClient = cSocketIn.readLine();
	 	   }
			
		   cSocketOut.close();
		   cSocketIn.close();
		   clientTCPSocket.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
}