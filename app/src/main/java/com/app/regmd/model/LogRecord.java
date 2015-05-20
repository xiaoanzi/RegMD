package com.app.regmd.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 王海 on 2015/4/8.
 */
public class LogRecord implements Serializable {
    private int id;
    private int dayNumber;
    private Date dayDate;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Date getDayDate() {
        return dayDate;
    }

    public void setDayDate(Date dayDate) {
        this.dayDate = dayDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
