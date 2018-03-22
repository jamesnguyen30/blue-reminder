package com.example.jamesnguyen.taskcycle.smart_date_detector;

import java.util.Calendar;

public class MatchedPosition {
    private int startPos;
    private int endPos;

    public MatchedPosition(int startPos, int endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }
}
