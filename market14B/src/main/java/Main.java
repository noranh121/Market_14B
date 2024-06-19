import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        Map<Integer, String> tst = new ConcurrentHashMap<>();
        tst.put(0, "0");
        System.out.println(tst.computeIfAbsent(0, k -> "Default Value"));
        System.out.println(tst.computeIfAbsent(1, k -> "DEFAULT VALUE FOR 1"));
        System.out.println(tst.get(1));
    }
}