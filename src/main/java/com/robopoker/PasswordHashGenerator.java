package com.robopoker;

import javax.ejb.Stateless;

/**
 * User: Demishev
 * Date: 24.12.13
 * Time: 20:34
 */
@Stateless
public class PasswordHashGenerator {
    public String generateHashFromPassword(String password) {
        return "Dummy hash";
    }
}
