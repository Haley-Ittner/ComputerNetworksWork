import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.io.BufferedReader;

/**
 * Created by Haley on 2/6/2017.
 */
public class Assignment2Server {
    static class Data {
        int id;
        String item;
        String price;
        int inventory;

        public Data(int num, String name, String mun, int quant) {
            id = num;
            item = name;
            price = mun;
            inventory = quant;
        }

        public String printDeats() {
            String print = String.format("0000%1$-6s %2$-30s %3$-15s %4$-15s", id, item, price, inventory);
            return print;
        }
    }

    public static void main(String[] args) throws IOException {
        DatagramSocket udpServerSocket = null;
        DatagramPacket udpPacket = null;
        DatagramPacket udpPacketSend = null;
	BufferedReader in = null;
        String received;
        String send;
        int find;
        InetAddress address;
        int port;
        ArrayList<Data> list = new ArrayList<Data>();
        list.add(new Data(1, "New Inspiron 15", "$379.99", 157));
        list.add(new Data(2, "New Inspiron 17", "$449.99", 128));
        list.add(new Data(3, "New Inspiron 15R", "$549.99", 202));
        list.add(new Data(4, "New Inspiron 15z Ultrabook", "$749.99", 315));
        list.add(new Data(5, "XPS 14 Ultrabook", "$999.99", 261));
        list.add(new Data(6, "New XPS 12 UltrabookXPS", "$1199.99", 178));

        boolean morePackets = true;

        byte[] holder = new byte[256];

        udpServerSocket = new DatagramSocket(5100);

         while (morePackets) {
            try {
                udpPacket = new DatagramPacket(holder, holder.length);
                udpServerSocket.receive(udpPacket);
                received = new String(udpPacket.getData(), 0, udpPacket.getLength());
                find = Integer.parseInt(received);
                send = list.get(find-1).printDeats();

                address = udpPacket.getAddress();
                port = udpPacket.getPort();

                byte[] sender = send.getBytes();

                udpPacketSend = new DatagramPacket(sender, sender.length, address, port);
                udpServerSocket.send(udpPacketSend);
            } catch (IOException e) {
		e.printStackTrace();
                morePackets = false;
            }
        }
        udpServerSocket.close();
    }
}

