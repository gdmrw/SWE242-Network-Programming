import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args) throws Exception {

        DatagramSocket socket = new DatagramSocket();

//        byte[] buffer = new byte[1024 * 64];
//        DatagramPacket re_packet = new DatagramPacket(buffer, buffer.length);

        System.out.println("connection established");
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("Command:");
            String msg = sc.nextLine();

            if ("exit".equals(msg)){
                System.out.println("quit");
                socket.close();
                break;
            }

            byte[] bytes = msg.getBytes(); // command sender
            DatagramPacket packet = new DatagramPacket(bytes,bytes.length, InetAddress.getLocalHost(),6666);
            socket.send(packet);

            String respond = paket_receiver(socket);

            if ("check_sum".equals(respond)) {
                check_sum(socket,packet);
            } else {
                System.out.println(respond);
            }



        }

    }

    public static void check_sum(DatagramSocket socket, DatagramPacket packet) throws Exception {
        int i = 0;

        ArrayList<String> checksum_list = new ArrayList<>();
        ArrayList<String> res_list = new ArrayList<>();
        ArrayList<String> content = new ArrayList<>();

        ArrayList<String> reli = li_packet_receiver(socket);

        while (true) {
            while (!"check_sum_end".equals(reli.get(0))){

                checksum_list.add(String.valueOf(i));
                i++;

                res_list.add(reli.get(0));
                content.add(reli.get(1));

                reli = li_packet_receiver(socket);



            }
            if (checksum_list.equals(res_list)) {  //match print
                content.forEach(item -> System.out.println(item+" "));
                break;
            }
            else {
                socket.send(packet);  //unmatch keep sending request
            }
        }


    }

    public static String paket_receiver(DatagramSocket socket) throws Exception {
        byte[] buffer = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        int len = packet.getLength();
        return new String(buffer, 0, len);

    }

    public static ArrayList<String> li_packet_receiver(DatagramSocket socket) throws Exception {
        byte[] buffer = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        byte[] data = packet.getData();
        int length = packet.getLength();
        ByteArrayInputStream bais = new ByteArrayInputStream(data, 0, length);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ArrayList<String> receivedList = (ArrayList<String>) ois.readObject();
        return receivedList;


    }

    
}