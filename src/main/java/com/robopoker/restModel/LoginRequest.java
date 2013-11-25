package com.robopoker.restModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Demishev
 * Date: 25.11.13
 * Time: 17:48
 */
@XmlRootElement
public class LoginRequest {
    private String login;
    private String password;

    @XmlAttribute
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @XmlAttribute
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
