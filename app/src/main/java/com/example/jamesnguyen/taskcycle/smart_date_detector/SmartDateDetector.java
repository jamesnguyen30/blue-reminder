package com.example.jamesnguyen.taskcycle.smart_date_detector;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartDateDetector {

    //parse date and time
    //setup regular reminder like: read book every morning at 5am
    private final String REG_PATTERN =
            "(everyday|today|tomorrow|next week|next month|next year)|" +
                    "(on *|next *)?(monday|mon|tuesday|tue|wednesday|wed|thursday|thu|friday|fri|saturday|sat|sunday|sun)|" +
                    "(on *)?(january|jan|february|feb|march|mar|april|may|june|july|jul|august|aug|september|sep|october|oct|november|nov|december|dec) *(\\d{1,2})? (\\d{4})?|" +
                    "(on *)(\\d{1,2})[.](\\d{1,2})[.]?(\\d{4})?|" +
                    "(at *)(\\d{1,2}) {0,1}(: *(\\d{1,2}))? {0,1}(am|pm|in the morning|in morning|morning|in the night|at night|night|in the evening)?";

    private Pattern pattern = Pattern.compile(REG_PATTERN, Pattern.CASE_INSENSITIVE);
    private Matcher matcher;
    private Calendar calendar;
    private String[] groups;

    private boolean hasDate;
    private boolean hasTime;

    ArrayList<MatchedPosition> matchedPositions;

    private String originalText;

    public SmartDateDetector(String originalText) {
        this.originalText = originalText.toLowerCase();
        matchedPositions = new ArrayList<MatchedPosition>();
        hasDate = false;
        hasTime = false;
    }

    public SmartDateDetector() {
        matchedPositions = new ArrayList<MatchedPosition>();
        hasDate = false;
        hasTime = false;
    }

    public void setOriginalText(String originalText){

        this.originalText = originalText.toLowerCase();
    }

    public String[] getGroups(){
        return groups;
    }

    public int getMatchedCount(){
        return matchedPositions.size();
    }

    public ArrayList<MatchedPosition> getMatchedPositions(){
        return matchedPositions;
    }

    public MatchedPosition getMatchPositionAt(int index){
        return matchedPositions.get(index);
    }

    //return a pre-defined matched groups

    public boolean findMatches(){
        matcher = pattern.matcher(originalText);
        groups = new String[matcher.groupCount()+1];
        matchedPositions.clear();
        hasDate = false;
        hasTime = false;
        MatchedPosition matchedPosition;
        boolean isFound = false;
        while(matcher.find()){
            for(int i =0;i<=matcher.groupCount();i++){
                if(matcher.group(i)!=null){
                    groups[i] = matcher.group(i);
                }
            }
            matchedPosition = new MatchedPosition(matcher.start(),matcher.end());
            this.matchedPositions.add(matchedPosition);
            isFound = true;
        }
        return isFound;
    }

    public String[] getMatchedGroups(){
        return groups;
    }

    //this function does not have to be fast in real time
    // because it runs only when user accept their date input
    // ( user press OK when the date were highlighted )

    public Calendar convertToCalendar(){

        //get current date
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR); // get current year;
        int day = calendar.get(Calendar.DAY_OF_MONTH); // current day
        int month = calendar.get(Calendar.MONTH);//get current month
        int hour = calendar.get(Calendar.HOUR)+1; // next hour from current time

        int minute = 0; //0 minute by default

        if(groups.length<=0){
            return null;
        } else{
            //get the date
            if(groups[5]!=null){
                //set has date;
                hasDate = true;
                month = getMonthCodeByName( groups[5]);

                if(groups[6]!=null){
                    day = Integer.parseInt(groups[6]);
                }

                if(groups[7]!=null){
                    year = Integer.parseInt(groups[7]);
                }
            } else if(groups[9]!=null){
                hasDate = true;
                month = Integer.parseInt(groups[9])-1; // the Calendar.MONTH starts with 0 for January, you need to -1 to get the correct month
                if(groups[10]!=null){
                    day = Integer.parseInt(groups[10]);
                }
                if(groups[11]!=null){
                    year = Integer.parseInt(groups[11]);
                }
            }

            //get the time
            if(groups[13]!=null){
                hasTime = true;
                hour = Integer.parseInt(groups[13]);
                if(groups[15]!=null){
                    minute = Integer.parseInt(groups[15]);
                }
                if(groups[16]!=null
                        && (groups[16].equals("pm") || groups[16].equals("at night"))
                        && hour<12){
                    hour+=12;
                }
            }
        }
        calendar = new GregorianCalendar(year, month, day, hour, minute);
        //set day of the week, recognize next week, next month, next year phrase
        if(groups[1]!=null){
            setDayByPhrase(calendar, groups[1]);
        } else if(groups[3]!=null){ //if day is already set, ignore group 3
            boolean isNextWeek = false;
            if(groups[2]!=null && groups[2].replaceAll("\\s","").equals("next"))
                isNextWeek = true;
            calendar.add(Calendar.DAY_OF_WEEK, daysToIncreaseTo(calendar.get(Calendar.DAY_OF_WEEK),groups[3], isNextWeek));
        }
        return calendar;
    }

    //set day by phrase like tomorrow, next week,
    private void setDayByPhrase(Calendar calendar, String phrase){

        if( phrase ==null || phrase.equals("today")|| phrase.length()<=0){
            return;
        } else{
            hasDate = true;
        }

        if (phrase.equals("tomorrow")) {
            calendar.add(Calendar.DAY_OF_WEEK, 1); // day of week = 2 is monday
        } else if(phrase.equals("next week")){ // to monday of next week
            calendar.add(Calendar.WEEK_OF_MONTH, 1);
            calendar.set(Calendar.DAY_OF_WEEK,2);
        } else if(phrase.equals("next month")){ // to the first day of the month
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH,1);
        } else if(phrase.equals("next year")){ // to the first day of the year
            calendar.add(Calendar.YEAR, 1);
            calendar.set(Calendar.DAY_OF_YEAR,1);
        }

        return;
    }

    //return monthCode from 0 to 11, Jan to Dec accordingly
    private int getMonthCodeByName( String monthName){
        int monthCode = 0;
        if(monthName!=null){
            if(monthName.equals("january") || monthName.equals("jan")){
                monthCode = Calendar.JANUARY;
            }else if(monthName.equals("february") || monthName.equals("feb")){
                monthCode = Calendar.FEBRUARY;
            }else if(monthName.equals("march") || monthName.equals("mar")){
                monthCode =  Calendar.MARCH;
            }else if(monthName.equals("april")){
                monthCode = Calendar.APRIL;
            }else if(monthName.equals("may")){
                monthCode = Calendar.MAY;
            }else if(monthName.equals("june")){
                monthCode = Calendar.JUNE;
            }else if(monthName.equals("july") || monthName.equals("jul")){
                monthCode =  Calendar.JULY;
            }else if(monthName.equals("august") || monthName.equals("aug")){
                monthCode = Calendar.AUGUST;
            }else if(monthName.equals("september") || monthName.equals("sep")){
                monthCode = Calendar.SEPTEMBER;
            }else if(monthName.equals("october") || monthName.equals("oct")){
                monthCode = Calendar.OCTOBER;
            }else if(monthName.equals("november") || monthName.equals("nov")){
                monthCode = Calendar.NOVEMBER;
            }else if(monthName.equals("december") || monthName.equals("dec")){
                monthCode = Calendar.DECEMBER;
            }
            return monthCode;
        }
        return 0;
    }

    //return a day to increase the current date
    //eg : today is monday, if user says tomorrow, increase 1 day
    //if user says nothing, today, increase 0;
    //PASSED SOME TEST
    private int daysToIncreaseTo(int today, String dayOfWeek, boolean isNextWeek){
        int setDay = 0;
        hasDate = true;
        if (dayOfWeek.equals("sunday") || dayOfWeek.equals("sun")) {
            setDay =1;
        } else if (dayOfWeek.equals("monday") || dayOfWeek.equals("mon")) {
            setDay = 2;
        } else if (dayOfWeek.equals("tuesday") || dayOfWeek.equals("tue")) {
            setDay = 3;
        } else if (dayOfWeek.equals("wednesday") || dayOfWeek.equals("wed")) {
            setDay = 4;
        }else if (dayOfWeek.equals("thursday") || dayOfWeek.equals("thu")) {
            setDay = 5;
        }else if (dayOfWeek.equals("friday") || dayOfWeek.equals("fri")) {
            setDay = 6;
        }else if(dayOfWeek.equals("saturday") || dayOfWeek.equals("sat")) {
            setDay = 7;
        }


        if(setDay < today){
            if(setDay!=1)
                return (7%today)+setDay;
            else{ // this is a work around solution for sunday, because
                // sun day is new day of the week, according to Calendar object
                // but most people think sunday is the last day of the week
                // so if this is sunday, +7, user will use this intuitively
                return (7%today)+setDay + 7;
            }
        } else {
            if(isNextWeek) // if user says next week
                return (setDay+7) - today;
            return setDay - today;
        }
    }

    public boolean isHasDate() {
        return hasDate;
    }

    public boolean isHasTime() {
        return hasTime;
    }

    private void print(String st){
        System.out.println(st);
    }
}
