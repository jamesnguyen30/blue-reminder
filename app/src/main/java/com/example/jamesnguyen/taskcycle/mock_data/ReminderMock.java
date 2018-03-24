package com.example.jamesnguyen.taskcycle.mock_data;

import java.util.Calendar;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderMock {
    private String title;
    //private Date reminderDate;
    private Calendar calendar;

    private boolean hasDate;
    private boolean hasTime;

    public ReminderMock(String title) {
        this.title = title;
        calendar = Calendar.getInstance();
        hasDate = false;
        hasTime = false;
    }

    public ReminderMock(String title, Calendar calendar) {
        this.title = title;
        this.calendar = calendar;
        hasDate = false;
        hasTime = false;
    }

    public ReminderMock(String title, Calendar calendar, boolean hasDate, boolean hasTime) {
        this.title = title;
        this.calendar = calendar;
        this.hasDate = hasDate;
        this.hasTime = hasTime;
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

    public boolean isHasDate() {
        return hasDate;
    }

    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }

    public boolean isHasTime() {
        return hasTime;
    }

    public void setHasTime(boolean hasTime) {
        this.hasTime = hasTime;
    }
}
