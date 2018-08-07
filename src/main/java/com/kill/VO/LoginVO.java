package com.kill.VO;

import com.kill.annotation.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 登录视图层对象
 *
 * @author Administrator
 * @create 2018-07-14 20:45
 */
@Data
public class LoginVO {

    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 6)
    private String password;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"mobile\":\"").append(mobile).append('\"');
        sb.append(",\"password\":\"").append(password).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
