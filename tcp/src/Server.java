import java.net.*;
import java.io.*;


public class Server {

    private static String path = "src/test";

    public static void main(String[] args) throws Exception {
        System.out.println("server up");

        ServerSocket serverSocket = new ServerSocket(8888);

        Socket socket = serverSocket.accept();

        InputStream is = socket.getInputStream();

        DataInputStream dis = new DataInputStream(is);

        OutputStream os = socket.getOutputStream();

        DataOutputStream dos = new DataOutputStream(os);



        while (true){
            String command = null;
            try {
                command = dis.readUTF();
                System.out.println(command);
            } catch (Exception e) {
                System.out.println("client down");
                socket.close();
                dis.close();
                dos.close();
                break;
            }
            if ("index".equals(command)){
                sendIndex(dos);

            } else if (command.startsWith("get")) {
                String fileName = command.substring(3).trim();
                sendContent(fileName, dos, socket);
            } else if (command.startsWith("change")) {
                String new_path = command.substring(6).trim();
                changeDirectory(new_path,dos);
            } else {
                dos.writeUTF("error");
                dos.writeUTF("end");
                dos.flush();
            }

        }

    }

    public static void sendContent(String filename,DataOutputStream dos,Socket socket) throws Exception {

        String directoryPath = path + "/";

        try (BufferedReader br = new BufferedReader(new FileReader(directoryPath + filename))) {
            String line;
            dos.writeUTF("ok");
            while ((line = br.readLine()) != null) {
                dos.writeUTF(line);
                dos.flush();
            }
            dos.writeUTF("end"); // Send end-of-content indicator
            dos.flush();
        } catch (Exception e) {
            dos.writeUTF("error " + e.getMessage());
            dos.flush();
            dos.close();
            socket.close();
        }

    }

    public static void sendIndex(DataOutputStream dos) throws Exception {
        String directoryPath = path;

        // 创建 File 对象
        File directory = new File(directoryPath);

        // 检查 directoryPath 是否是目录
        if (directory.isDirectory()) {
            // 获取目录中的所有文件和目录的名称
            String[] contents = directory.list();
            if (contents != null) {
                String index_list = String.join("\n",contents);
                System.out.println(index_list);
                dos.writeUTF(index_list);
                dos.flush();
                dos.writeUTF("end");
                dos.flush();
            } else {
                dos.writeUTF("The directory is empty.");
                dos.flush();
                dos.close();
            }
        } else {
            dos.writeUTF(directoryPath + " is not a directory.");
            dos.flush();
            dos.close();
        }
    }

    private static void changeDirectory(String newPath,DataOutputStream dos) throws Exception {
        // Check if the path is valid
        File dir = new File(newPath);

        if (dir.exists() && dir.isDirectory()) {
            path = newPath;
            dos.writeUTF("directory change to "+ path);
            dos.flush();
            dos.writeUTF("end");
            dos.flush();
        } else {
            dos.writeUTF("Invalid directory path");
            dos.flush();
            dos.writeUTF("end");
            dos.flush();

        }
    }

}


