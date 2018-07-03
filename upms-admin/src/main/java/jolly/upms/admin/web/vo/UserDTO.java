package jolly.upms.admin.web.vo;

import com.jolly.upms.manager.vo.SignVO;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author chenjc
 * @since 2018-02-05
 */
public class UserDTO extends SignVO {

    @NotEmpty
    private String appKey;

    @NotEmpty
    private String userName;

    private String email;

    @NotEmpty
    private String password;

    private String suppCode;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSuppCode() {
        return suppCode;
    }

    public void setSuppCode(String suppCode) {
        this.suppCode = suppCode;
    }
}
