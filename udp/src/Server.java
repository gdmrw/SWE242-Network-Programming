import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class Server {
    private static String path = "src/test";
    public static void main(String[] args) throws Exception {
        System.out.println("sever up");

        DatagramSocket socket = new DatagramSocket(6666);

        byte[] buffer = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {   //keep opening since server

            socket.receive(packet);

            int len = packet.getLength();
            String command = new String(buffer, 0, len);  //command format finish
            System.out.println(command);

            if ("index".equals(command)){
                get_index(socket,packet);   //method
            }
            else if (command.startsWith("get")) {
                String fileName = command.substring(3).trim(); //silce and get file name
                send_content(fileName,socket,packet); // method
            }
            else if (command.startsWith("change")) {
                String new_path = command.substring(6).trim();
                changeDirectory(new_path,socket,packet);
            }
            else {
                send("error",socket,packet);
            }


        }

    }

    private static void changeDirectory(String newPath,DatagramSocket socket,DatagramPacket packet) throws Exception {
        // Check if the path is valid
        File dir = new File(newPath);

        if (dir.exists() && dir.isDirectory()) {
            path = newPath;
            send("directory change to "+ path, socket,packet);
        } else {
            send("Invalid directory path",socket,packet);
        }
    }


    public static void get_index(DatagramSocket socket,DatagramPacket packet) throws Exception {
        // create file object
        File directory = new File(path);


        // check if existed
        if (directory.isDirectory()) {
            // get index
            String[] contents = directory.list();
            if (contents != null) {
                String index_list = String.join("\n",contents);
                send(index_list,socket,packet);
            } else {
                send("The directory is empty.",socket,packet);
            }
        } else {
            send((path + " is not a directory."),socket,packet);
        }
    }


    public static void send_content(String filename,DatagramSocket socket,DatagramPacket packet) throws Exception {

        final int CHUNK_SIZE = 1024;

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path + "/" + filename))) {
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            int i = 1;
            ArrayList<String> list = new ArrayList<String>();
            send("check_sum",socket,packet);

            list.add("0");
            list.add("ok");
            send_list(list,socket,packet);
            list = new ArrayList<String>();

            while ((bytesRead = bis.read(buffer)) != -1){

                list.add(String.valueOf(i));
                i++;
                String content = new String(buffer, 0, bytesRead);
                list.add(content);
                send_list(list,socket,packet);
                list = new ArrayList<String>();

            }
            list.add("check_sum_end");
            send_list(list,socket,packet);
            list = new ArrayList<String>();

        } catch (Exception e) {
            send("error " + e.getMessage(),socket,packet);
            socket.close();   //not such file or random code, reply error and close connection
            System.exit(0);
        }


    }

    public static void send(String content,DatagramSocket socket,DatagramPacket packet) throws Exception {  //create send method to better write
        byte[] bytes = content.getBytes();
        DatagramPacket re_packet = new DatagramPacket(bytes,bytes.length, packet.getAddress(),packet.getPort());
        socket.send(re_packet);
    }

    public static void send_list(ArrayList<String> li,DatagramSocket socket,DatagramPacket packet) throws Exception {


        //Serialization array list
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(li);
        byte[] bytes = baos.toByteArray();
        DatagramPacket re_packet = new DatagramPacket(bytes,bytes.length, packet.getAddress(),packet.getPort());
        socket.send(re_packet);
    }



}