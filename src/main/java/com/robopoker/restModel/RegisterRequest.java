package com.robopoker.restModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Demishev
 * Date: 24.12.13
 * Time: 20:14
 */
@XmlRootElement
public class RegisterRequest {
    @XmlElement
    private String login;
    @XmlElement
    private String email;
    @XmlElement
    private String name;
    @XmlElement
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
