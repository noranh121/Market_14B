package Web.Requests;


public class ReqUser {
    private String guest;
    private String username;
    private String password;
    private Double age;


    public ReqUser(String guest,String username,String password,Double age){
        this.guest = guest;
        this.username = username;
        this.password = password;
        this.age = age;

    }

    public String getGuest(){
        return this.guest;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPass(){
        return this.password;
    }
    public Double getAge(){
        return this.age;
    }
    
}
