import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class fileReader {
    public static void main(String[] args) {
        String filename = "testfile2.txt";
        String filePath = "src/test/" + filename; // 请替换为实际文件路径

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.err.println("error" + e.getMessage());
        }
    }
}

