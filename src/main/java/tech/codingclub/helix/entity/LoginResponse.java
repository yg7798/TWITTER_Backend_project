package tech.codingclub.helix.entity;

public class LoginResponse {
    public Long id;
    public boolean isLogin;
    public String message;

   public LoginResponse(){}
    public LoginResponse(Long id, boolean isLogin, String message) {
        this.id = id;
        this.isLogin = isLogin;
        this.message = message;
    }
}
