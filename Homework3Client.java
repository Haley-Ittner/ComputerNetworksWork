import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Created by Haley on 2/13/2017
 */
public class Homework3Client {

    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        Scanner scnr = new Scanner(System.in);
        String userAdd;
        InetAddress address;
        long secs;
        long end;

        System.out.println("Please enter the DNS or IP of the server: ");
        userAdd = scnr.next();
        address = InetAddress.getByName(userAdd);
        // System.out.println(address);

        try {
            secs = System.currentTimeMillis();
            tcpSocket = new Socket(address, 5100);
            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            end = System.currentTimeMillis();
            System.out.println((end - secs) + " RTT");
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
        String keepGoing = "yes";
        String command;
        String item;
        String host;
        String user;
        String http;
        String line = "";
	String body = "";
        FileWriter filer;
        PrintWriter writer;
        int count = 0;

        while (keepGoing.equals("yes")) {
            System.out.print("Please enter the HTTP command you want to execute(Must be uppercase): ");
            command = sysIn.readLine();
            System.out.print("Please enter the item you wish to receive: ");
            item = sysIn.readLine();
            System.out.print("Please enter what version of HTTP you are using(ex: 1.2): ");
            http = sysIn.readLine();
            System.out.print("Please enter the host name: ");
            host = sysIn.readLine();
            System.out.print("Please enter the user-agent being used: ");
            user = sysIn.readLine();
            fromUser = command + " /" + item + " HTTP/" + http + "\r\n" + "Host: " + host +"\r\n" +
                    "User-Agent: " + user + "\r\n";
            secs = System.currentTimeMillis();
	         System.out.println(fromUser);
            socketOut.println(fromUser);

            if ((fromServer = socketIn.readLine()) != null)
            {
                end = System.currentTimeMillis();
                System.out.println("RTT = " + (end - secs));
                while (!fromServer.equals("")) {
                    	line += fromServer + "\r\n";
                    	fromServer = socketIn.readLine();
                }
		          System.out.println(line);
			  //System.out.println("Substring: " + line.substring(9, 12));
                if (line.substring(9,12).equals("200")) {
			         File holder = new File(item);
                   		 holder.createNewFile();
			         writer = new PrintWriter(holder);
                  while (count < 4) {
                    fromServer = socketIn.readLine();
                    if (fromServer.equals("")) {
                       body += fromServer + "\r\n";
                       count++;
                    } else {
                       body += fromServer + "\r\n";
                    }
                }
                  //System.out.print(body);
			writer.print(body);
			writer.flush();
		            writer.close();
              }
            } else {
            	System.out.println("Server replies nothing!");
                break;
            }

            if (fromUser.equals("Bye.")) {
                break;
            }
	    line = "";
            System.out.print("Do you wish to continue (yes or no)?: ");
            keepGoing = sysIn.readLine().toLowerCase();
        }

        socketOut.close();
        socketIn.close();
        sysIn.close();
        tcpSocket.close();
    }
}
