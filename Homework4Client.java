
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Homework4Client {
    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        String userAdd;
        InetAddress address;
        Scanner scnr = new Scanner(System.in);
        String keepGoing = "yes";

        System.out.println("Please enter the DNS or IP of the server: ");
        userAdd = scnr.next();
        address = InetAddress.getByName(userAdd);
        //System.out.println(address);

        try {
            tcpSocket = new Socket(address, 5100);
            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + userAdd);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "  + userAdd);
            System.exit(1);
        }

        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
        
        
        System.out.println(socketIn.readLine());        

        while (keepGoing.equals("yes")) {
            String sender = "", receiver = "", subject = "", message = "", holder = "", domain = "";
            String[] messageCut;
            
            System.out.print("Please enter the email address of the sender: ");
            sender = sysIn.readLine();
            System.out.print("Please enter the email address of the receiver: ");
            receiver = sysIn.readLine();
            System.out.print("Please enter the subject of your email: ");
            subject = sysIn.readLine();
            System.out.print("Please enter the body of your email(end with a period on its own line): ");
            do {
               holder = sysIn.readLine();
               message += holder + " ";
               System.out.print("|> ");
            } while (!holder.equals("."));
            messageCut = message.split(" ");
            for (int x = sender.length() - 1; x > -1; x--) {
               if (sender.substring(x - 1, x).equals("@")) {
                  break;
               } else {
                  domain = sender.substring(x - 1, x) + domain;
               }
            }
            socketOut.println("HELO " + domain);
            System.out.println(socketIn.readLine());
            socketOut.println("MAIL FROM " + sender);
            System.out.println(socketIn.readLine());
            socketOut.println("RCPT TO " + receiver);
            System.out.println(socketIn.readLine());
            socketOut.println("DATA");
            System.out.println(socketIn.readLine());
            /*System.out.println("To: " + receiver);
            System.out.println("From: " + sender);
            System.out.println("Subject: " + subject + "\r\n");
            for (int y = 0; y < messageCut.length; y++) {
               System.out.println(messageCut[y]);   
            } */
            socketOut.println("To: " + receiver);
            socketOut.println("From: " + sender);
            socketOut.println("Subject: " + subject + "\r\n");
            for (int y = 0; y < messageCut.length; y++) {
               socketOut.println(messageCut[y]);   
            }
            System.out.println(socketIn.readLine()); 
            //System.out.println(socketIn.readLine());           
            System.out.print("Do you wish to continue (yes or no)?: ");
            keepGoing = sysIn.readLine().toLowerCase();
            if (keepGoing.equals("no")) {
               socketOut.println("QUIT");
               socketOut.println("next");
               System.out.println(socketIn.readLine());
               break;
            } else {
            socketOut.println("next");
            //socketOut.println("again");
            }
        }

        socketOut.close();
        socketIn.close();
        sysIn.close();
        tcpSocket.close();
    }
}