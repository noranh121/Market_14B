package DomainLayer.backend.AuthenticatorPackage;

public class Authenticator {
    private final static IPasswordEncoder encoder = new BCryptEncoder();

    public static String encodePassword(String password){
        return encoder.encodePassword(password);
    }

    public static boolean matches(String rawPassword, String password){
        return encoder.matches(rawPassword, password);
    }
}
