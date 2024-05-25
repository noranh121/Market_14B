package DomainLayer.backend.AuthenticatorPackage;

public class Authenticator {
    private IPasswordEncoder encoder;

    public Authenticator(){
        this.encoder = new BCryptEncoder();
    }

    public String encodePassword(String password){
        return encoder.encodePassword(password);
    }

    public boolean matches(String rawPassword, String password){
        return encoder.matches(rawPassword, password);
    }
}
