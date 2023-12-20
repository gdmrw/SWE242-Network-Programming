import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();

        list1.add("Element1");
        list1.add("Element2");
        list1.add("Element3");

        list2.add("Element1");
        list2.add("Element2");
        list2.add("Element3");

        if (list1.equals(list2)) {
            System.out.println("The two lists are equal.");
        } else {
            System.out.println("The two lists are not equal.");
        }
    }
}