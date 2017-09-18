import java.io.IOException;
import java.util.Scanner;
import java.net.*;

/**
 * Created by Haley on 2/6/2017.
 */
public class Assignment2Client {

    public static void main(String[] args) throws IOException {
        Scanner sysIn = new Scanner(System.in);
        DatagramSocket udpSocket = new DatagramSocket();
        int port;
        int id;
        long secs;
        long newer;
        InetAddress address;
        String userAnswer;
        String userAdd;
        String idString;
        String display;

        System.out.println("Please enter the DNS or IP of the server: ");
        userAdd = sysIn.next();
        address = InetAddress.getByName(userAdd);

        do {
            System.out.printf("%1$-10s Item Description\n", "Item ID");
            System.out.printf("%1$-10s New Inspiron 15\n", "00001");
            System.out.printf("%1$-10s New Inspiron 17\n", "00002");
            System.out.printf("%1$-10s New Inspiron 15R\n", "00003");
            System.out.printf("%1$-10s New Inspiron 15z Ultrabook\n", "00004");
            System.out.printf("%1$-10s XPS 14 Ultrabook\n", "00005");
            System.out.printf("%1$-10s New XPS 12 UltrabookXPS\n", "00006");
            do {
                System.out.print("Please input a valid item ID: ");
                id = sysIn.nextInt();
                if (id < 1 || id > 6) {
                    System.out.println("Invalid ID!");
                }
            } while (id < 1 || id > 6);

            idString = id + "";
            byte[] buffer = idString.getBytes();
            DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length, address, 5100);
            udpSocket.send(udpPacket);
            secs = System.currentTimeMillis();

            byte[] hold = new byte[256];
            DatagramPacket udpPacket2 = new DatagramPacket(hold, hold.length);
            udpSocket.receive(udpPacket2);
            newer = System.currentTimeMillis();
            display = new String(udpPacket2.getData(), 0, udpPacket2.getLength());


            System.out.printf("%1$-10s %2$-30s %3$-15s %4$-15s %5$-10s\n", "Item ID", "Item Description", "Unit Price", "Inventory", "RTT of Query");
            System.out.println(display + " " + (newer - secs) + "ms");

            System.out.print("Would you like to continue? ");
            userAnswer = sysIn.next();

        } while (userAnswer.equals("y"));

        udpSocket.close();
    }
}
