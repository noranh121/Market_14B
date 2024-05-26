package DomainLayer.backend.AuthenticatorPackage;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptEncoder implements IPasswordEncoder {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
