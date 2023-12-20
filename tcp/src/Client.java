import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8888);

        OutputStream os = socket.getOutputStream();

        DataOutputStream dos = new DataOutputStream(os);

        InputStream is = socket.getInputStream();

        DataInputStream dis = new DataInputStream(is);


        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Connection established");
            while (true){
                System.out.println("command:");
                String msg = sc.nextLine();
                if ("exit".equals(msg)){
                    System.out.println("quit");
                    dos.close();
                    dis.close();
                    socket.close();
                    break;
                }

                dos.writeUTF(msg);
                dos.flush();
                while (true) {
                    String res = dis.readUTF();
                    if ("end".equals(res)) {
                        break; // Break if end-of-content indicator is received
                    }
                    System.out.println(res);
                }

            }
        } catch (Exception e) {
            System.out.println("Connection closed by server");
        }
    }
}