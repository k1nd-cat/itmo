package users;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

public class UserInfo implements Serializable {
    @Getter
    @Setter
    @NonNull
    private String username;
    @Getter
    @Setter
    @NonNull
    private String password;
    @Getter
    @Setter
    private Integer id;
}
