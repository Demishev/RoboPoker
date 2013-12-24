package com.robopoker.restModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Demishev
 * Date: 24.12.13
 * Time: 20:20
 */
@XmlRootElement
public class RegisterResponse {
    private static final RegisterResponse SUSSES_RESPONSE = new RegisterResponse("Susses");
    private static final RegisterResponse USER_DUPLICATES = new RegisterResponse("Duplicated");

    @XmlElement
    private String status;


    public static RegisterResponse getSussesResponse() {
        return SUSSES_RESPONSE;
    }

    public static RegisterResponse getUserDuplicates() {
        return USER_DUPLICATES;
    }

    public RegisterResponse() {
    }

    private RegisterResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
