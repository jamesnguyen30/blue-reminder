package com.example.jamesnguyen.taskcycle.mock_data;

import java.util.Date;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderMock {
    private String title;
    private Date reminderDate;
    private boolean isDone;

    public ReminderMock(String title) {
        this.title = title;
        reminderDate = new Date();
        isDone = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
