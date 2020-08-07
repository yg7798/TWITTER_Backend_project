package tech.codingclub.helix.entity;

public class SignUpResponse {
    public String message;
    public boolean user_created;

    public SignUpResponse(String message, boolean user_created) {
        this.message = message;
        this.user_created = user_created;
    }
}
