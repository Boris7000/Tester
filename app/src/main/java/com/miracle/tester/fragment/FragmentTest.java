package com.miracle.tester.fragment;

import static com.miracle.engine.util.NavigationUtil.back;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.miracle.engine.fragment.NestedMiracleFragmentFabric;
import com.miracle.engine.fragment.side.SideTabsFragment;
import com.miracle.tester.model.Test;
import com.miracle.tester.model.TestResult;
import com.miracle.tester.model.TestTask;
import com.miracle.tester.util.NavigationUtil;
import com.miracle.tester.util.StorageUtil;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentTest extends SideTabsFragment {

    private Test test;
    private ArrayList<TestTask> shuffledTasks;
    private TestResult testResult;

    public OnTaskAnswerListener onTaskAnswerListener = new OnTaskAnswerListener() {
        @Override
        public void onTaskAnswer(TestTask testTask) {
            testResult.setScore(testResult.getScore()+testTask.getComplete());

            TestTask emptyTask = null;
            for(int i=0; i<shuffledTasks.size();i++){
                TestTask task = shuffledTasks.get(i);
                if(task.getComplete()==-1){
                    emptyTask = task;
                    break;
                }
            }
            if(emptyTask==null){
                testResult.setEndTime(System.currentTimeMillis());
                test.setTestResult(testResult);
                StorageUtil.get().updateTestAndSave(test);
                Context context = getContext();
                back(context);
                NavigationUtil.goToTest(test, context);
            } else {
                getTabsFragmentController().getViewPager().setCurrentItem(shuffledTasks.indexOf(emptyTask));
            }
        }
    };

    public void setTest(Test test) {
        this.test = test;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState!=null&&!savedInstanceState.isEmpty()){
            Test test = savedInstanceState.getParcelable("test");
            if(test!=null){
                this.test = test;
                ArrayList<TestTask> shuffledTasks = savedInstanceState.getParcelableArrayList("shuffledTasks");
                if(shuffledTasks!=null){
                    this.shuffledTasks = shuffledTasks;
                } else {
                    shuffleTasks();
                }
                TestResult testResult = savedInstanceState.getParcelable("testResult");
                if(testResult!=null){
                    this.testResult = testResult;
                } else {
                    generateNewTestResult();
                }
            } else {
                back(getContext());
            }
        } else {
            generateNewTestResult();
            shuffleTasks();
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void generateNewTestResult(){
        this.testResult = new TestResult(test.getTasks().size(), System.currentTimeMillis());
    }

    public void shuffleTasks(){
        shuffledTasks = new ArrayList<>();
        for (TestTask testTask:test.getTasks()) {
            shuffledTasks.add(new TestTask(testTask));
        }
        Collections.shuffle(shuffledTasks);
    }

    @Override
    public ArrayList<NestedMiracleFragmentFabric> loadTabs() {
        ArrayList<NestedMiracleFragmentFabric> fabrics = new ArrayList<>();
        for (int i=0; i<shuffledTasks.size(); i++){
            fabrics.add(new FragmentTestTask.Fabric(shuffledTasks.get(i), String.valueOf(i+1), 0));
        }

        return fabrics;
    }

    @Override
    public ArrayList<NestedMiracleFragmentFabric> getErrorTabs() {
        return null;
    }

    @Override
    public String requestTitleText() {
        return test.getTitle();
    }

    @Override
    public void onClearSavedInstance(@NonNull Bundle savedInstanceState) {
        super.onClearSavedInstance(savedInstanceState);
        savedInstanceState.remove("testResult");
        savedInstanceState.remove("shuffledTasks");
        savedInstanceState.remove("test");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(testResult!=null){
            outState.putParcelable("testResult", testResult);
        }
        if(shuffledTasks!=null){
            outState.putParcelableArrayList("shuffledTasks", shuffledTasks);
        }
        if(test!=null){
            outState.putParcelable("test", test);
        }
        super.onSaveInstanceState(outState);
    }
}
