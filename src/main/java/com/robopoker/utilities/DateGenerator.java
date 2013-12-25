package com.robopoker.utilities;

import javax.ejb.Stateless;
import java.util.Date;

/**
 * User: Demishev
 * Date: 25.12.13
 * Time: 12:17
 */
@Stateless
public class DateGenerator {

    public Date getCurrentDate() {
        return new Date();
    }
}
