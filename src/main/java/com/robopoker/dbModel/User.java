package com.robopoker.dbModel;

/**
 * User: Demishev
 * Date: 25.11.13
 * Time: 16:17
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
    @NotNull
    @Column(name = "email")
    @Id
    private String email;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "registration_date")
    private Date registrationDate;

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }


    public User() {
    }

    public User(String email, String name, String passwordHash, Date registrationDate) {
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!email.equals(user.email)) return false;
        if (!name.equals(user.name)) return false;
        if (!passwordHash.equals(user.passwordHash)) return false;
        if (!registrationDate.equals(user.registrationDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + passwordHash.hashCode();
        result = 31 * result + registrationDate.hashCode();
        return result;
    }
}
