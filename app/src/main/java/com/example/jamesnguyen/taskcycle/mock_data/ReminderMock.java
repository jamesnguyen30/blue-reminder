package com.example.jamesnguyen.taskcycle.mock_data;

import java.util.Calendar;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderMock {
    private String title;
    //private Date reminderDate;
    private Calendar calendar;

    public ReminderMock(String title) {
        this.title = title;
        calendar = Calendar.getInstance();
    }

    public ReminderMock(String title, Calendar calendar) {
        this.title = title;
        this.calendar = calendar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
