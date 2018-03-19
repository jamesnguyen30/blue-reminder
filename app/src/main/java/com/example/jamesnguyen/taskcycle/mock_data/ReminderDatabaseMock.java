package com.example.jamesnguyen.taskcycle.mock_data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderDatabaseMock {

    private List<ReminderMock> database;

    public ReminderDatabaseMock() {
        database = new ArrayList<>();
    }

    public void addNewReminder(ReminderMock newReminder){
        database.add(newReminder);
    }
    public ReminderMock getReminderAt(int index){

        return database.get(index);
    }

    public int getDbSize(){
        return database.size();
    }

    public void populateMockDatbase(){
        ReminderMock data;
        long milis;
        Random rand = new Random();
        for(int i=0;i<5;i++){
            data = new ReminderMock("Reminder #" + Integer.toString(i));
            milis = rand.nextInt(999999999)+1000000;
            data.setReminderDate(new Date(milis));
            database.add(data);
        }
    }

}
