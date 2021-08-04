package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthData {

    private Integer id;

    public String getToken() {
        return token;
    }

    private String token;
    private String email;
    private String password;

}