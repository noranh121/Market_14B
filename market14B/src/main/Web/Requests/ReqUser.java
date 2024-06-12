package main.Web.Requests;


public class ReqUser {
    private String guest;
    private String username;
    private String password;


    public ReqUser(String guest,String username,String password){
        this.guest = guest;
        this.username = username;
        this.password = password;

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
    
}
