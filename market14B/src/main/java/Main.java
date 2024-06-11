import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Integer, String> tst = new HashMap<>();
        tst.put(0, "0");
        System.out.println(tst.computeIfAbsent(0, k -> "Default Value"));
        System.out.println(tst.computeIfAbsent(1, k -> "DEFAULT VALUE FOR 1"));
        System.out.println(tst.get(1));
    }
}