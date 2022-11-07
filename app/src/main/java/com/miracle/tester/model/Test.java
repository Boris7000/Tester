package com.miracle.tester.model;

import static com.miracle.tester.adapter.holder.ViewHolderTypes.TYPE_TEST;

import android.os.Parcel;
import android.os.Parcelable;

import com.miracle.engine.adapter.holder.ItemDataHolder;

import java.io.Serializable;
import java.util.ArrayList;

public class Test implements ItemDataHolder, Parcelable, Serializable {

    private final String title;
    private final ArrayList<TestTask> tasks;
    private TestResult testResult;

    public Test(String title, ArrayList<TestTask> tasks) {
        this.title = title;
        this.tasks = tasks;
    }

    public Test(Parcel in) {
        this.title = in.readString();
        this.tasks = in.createTypedArrayList(TestTask.CREATOR);
        this.testResult = in.readParcelable(TestResult.class.getClassLoader());
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<TestTask> getTasks() {
        return tasks;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public void clearTestResult(){
        testResult = null;
        for (TestTask testTask: tasks) {
            testTask.setComplete((byte) -1);
        }
    }

    @Override
    public int getViewHolderType() {
        return TYPE_TEST;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeTypedList(this.tasks);
        dest.writeParcelable(this.testResult, flags);
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel source) {
            return new Test(source);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };
}
