package com.robopoker.restModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Demishev
 * Date: 25.11.13
 * Time: 17:48
 */
@XmlRootElement
public class LoginRequest {
    @XmlElement
    private String email;
    @XmlElement
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}