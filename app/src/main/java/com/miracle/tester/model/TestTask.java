package com.miracle.tester.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class TestTask implements Parcelable, Serializable {

    private final String question;
    private final ArrayList<String> answers;
    private final ArrayList<Integer> rightAnswers;
    private byte complete = -1;

    public TestTask(String question, ArrayList<String> answers, ArrayList<Integer> rightAnswers) {
        this.question = question;
        this.answers = answers;
        this.rightAnswers = rightAnswers;
    }

    public TestTask(TestTask testTask) {
        this.question = testTask.question;
        this.answers = testTask.answers;
        this.rightAnswers = testTask.rightAnswers;
        this.complete = testTask.complete;
    }

    public TestTask(Parcel in) {
        this.question = in.readString();
        this.answers = in.createStringArrayList();
        this.rightAnswers = new ArrayList<>();
        in.readList(this.rightAnswers, Integer.class.getClassLoader());
        this.complete = in.readByte();
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public ArrayList<Integer> getRightAnswers() {
        return rightAnswers;
    }

    public byte getComplete() {
        return complete;
    }

    public void setComplete(byte complete) {
        this.complete = complete;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeStringList(this.answers);
        dest.writeList(this.rightAnswers);
        dest.writeByte(this.complete);
    }

    public static final Creator<TestTask> CREATOR = new Creator<TestTask>() {
        @Override
        public TestTask createFromParcel(Parcel source) {
            return new TestTask(source);
        }

        @Override
        public TestTask[] newArray(int size) {
            return new TestTask[size];
        }
    };
}
