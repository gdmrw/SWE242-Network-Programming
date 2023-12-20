import java.io.File;

public class DirectoryReader {
    public static void main(String[] args) {
        // 替换为你想要读取的目录路径
        String directoryPath = "src/test";

        // 创建 File 对象
        File directory = new File(directoryPath);

        // 检查 directoryPath 是否是目录
        if (directory.isDirectory()) {
            // 获取目录中的所有文件和目录的名称
            String[] contents = directory.list();
            if (contents != null) {
                for (String fileName : contents) {
                    System.out.println(fileName);
                }
            } else {
                System.out.println("The directory is empty.");
            }
        } else {
            System.out.println(directoryPath + " is not a directory.");
        }
    }
}
