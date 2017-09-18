import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Haley on 2/13/2017. xcjiosjdal
 */
public class Homework3Server extends Thread {

    private Socket clientTCPSocket = null;

    public Homework3Server(Socket socket) {
        super("Homework3Server");
        clientTCPSocket = socket;
    }

    public void run() {
        String[] status;
        File check;
	String header = "";
        String fodder = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
            BufferedReader cSocketIn = new BufferedReader(
                    new InputStreamReader(
                            clientTCPSocket.getInputStream()));

            String fromClient, toClient = "";

            while ((fromClient = cSocketIn.readLine()) != null) {
                status = fromClient.split(" ");
                if (status[0].equals("GET")) {
                    check = new File(status[1].substring(1, status[1].length()));
                    if (check.exists() && !check.isDirectory()) {
                        toClient = status[2] + " 200 OK\r\n" + "Date: " + dtf.format(now) + "\r\n" +
                                "Server: NachoAverageServer\r\n" + "\r\n";
                        System.out.println(dtf.format(now));
                        Scanner scnr = new Scanner(check);
                        while (scnr.hasNext()) {
                            toClient += scnr.next();
                        }
			toClient += "\r\n\r\n\r\n\r\n";
			header = fromClient + "\r\n";
                    } else {
                        toClient = status[2] + " 404 Not Found\r\n" + "Date: " + dtf.format(now) + "\r\n" +
                                "Server: NachoAverageServer\r\n";
			header = fromClient + "\r\n";
                        System.out.println(dtf.format(now));
                    }
		} else if (status[0].equals("Host:") || status[0].equals("User-Agent:")) {
			fodder += fromClient + "\r\n";
		} else if (status[0].equals("")) {
			System.out.println(header + fodder);
			cSocketOut.println(toClient);
			fodder = "";
			header = "";
                } else {
                    toClient = status[2] + " 400 Bad Request\r\n" + "Date: " + dtf.format(now) + "\r\n" +
                            "Server: NachoAverageServer\r\n";
		   header = fromClient + "\r\n";
                    System.out.println(dtf.format(now));
                }
                
                if (fromClient.equals("Bye"))
                    break;
            }

            cSocketOut.close();
            cSocketIn.close();
            clientTCPSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

