package com.miracle.tester.model;

import static com.miracle.tester.adapter.holder.ViewHolderTypes.TYPE_TEST;

import android.os.Parcel;
import android.os.Parcelable;

import com.miracle.engine.adapter.holder.ItemDataHolder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

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

    public Test(XSSFSheet sheet){
        String title = "";
        tasks = new ArrayList<>();

        String question;
        ArrayList<Integer> rightAnswers = new ArrayList<>();
        ArrayList<String> answers;

        int rowCounter = 0;
        for (Row row : sheet) {
            if(rowCounter==0){
                for (Cell cell : row) {
                    if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                        title = cell.getStringCellValue();
                        break;
                    }
                }
            } else {

                boolean ra = rowCounter%2==1;
                if (ra){

                    rightAnswers = new ArrayList<>();

                    boolean findStart = false;
                    int startFrom = row.getFirstCellNum();

                    for (Cell cell : row) {
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            if (cell.getStringCellValue().equals("{+}") || cell.getStringCellValue().equals("{-}")) {
                                if(!findStart) {
                                    findStart = true;
                                    startFrom = cell.getColumnIndex();
                                }
                                if (cell.getStringCellValue().equals("{+}")) {
                                    rightAnswers.add(cell.getColumnIndex()-startFrom);
                                }

                            }
                        }
                    }
                } else {
                    question = "";
                    answers = new ArrayList<>();

                    boolean firstCells = true;
                    for (Cell cell:row){
                        if(firstCells) {
                            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                firstCells = false;
                                question = cell.getStringCellValue();
                            }
                        } else {
                            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                answers.add(cell.getStringCellValue());
                            }
                        }
                    }

                    /*
                    Log.d("TAG", "right answers: " + rightAnswers);
                    Log.d("TAG", "question: " + question);
                    Log.d("TAG", "answers: " + answers);
                    Log.d("TAG", "--------------------------------- ");
                    */

                    if(!answers.isEmpty()&&!rightAnswers.isEmpty()&&!question.isEmpty()) {
                        TestTask testTask = new TestTask(question, answers, rightAnswers);
                        tasks.add(testTask);
                    }
                }
            }
            rowCounter++;
        }

        this.title = title;
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
