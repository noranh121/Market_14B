package DomainLayer.backend.AuthenticatorPackage;

public interface IPasswordEncoder {
    public String encodePassword(String password);
    public boolean matches(String rawPassword, String encodedPassword);
}
