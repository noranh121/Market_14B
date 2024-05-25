public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        int [] a = new int[2];
        a[0] = 1;
        a[1] = 2;
        System.out.println(a[0]);
        int [] b = a;
        b[0] = 6;
        System.out.println(a[0]); 
    }
}