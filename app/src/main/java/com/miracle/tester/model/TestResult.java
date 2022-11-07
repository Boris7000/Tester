package com.miracle.tester.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TestResult implements Parcelable, Serializable {

    private final int totalScore;
    private int score = 0;
    private final long startTime;
    private long endTime = 0;

    public TestResult(int totalScore, long startTime) {
        this.totalScore = totalScore;
        this.startTime = startTime;
    }

    public TestResult(Parcel in) {
        this.totalScore = in.readInt();
        this.score = in.readInt();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalScore);
        dest.writeInt(this.score);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
    }

    public static final Creator<TestResult> CREATOR = new Creator<TestResult>() {
        @Override
        public TestResult createFromParcel(Parcel source) {
            return new TestResult(source);
        }

        @Override
        public TestResult[] newArray(int size) {
            return new TestResult[size];
        }
    };
}
