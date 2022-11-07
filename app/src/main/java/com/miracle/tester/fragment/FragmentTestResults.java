package com.miracle.tester.fragment;

import static com.miracle.engine.util.NavigationUtil.back;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.miracle.engine.fragment.side.SideListFragment;
import com.miracle.engine.util.TimeUtil;
import com.miracle.tester.R;
import com.miracle.tester.model.Test;
import com.miracle.tester.model.TestResult;

public class FragmentTestResults extends SideListFragment {

    private Test test;

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
            } else {
                back(getContext());
            }
        }

        TestResult testResult = test.getTestResult();

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        TextView textView = rootView.findViewById(R.id.testTitle);
        textView.setText(test.getTitle());
        textView = rootView.findViewById(R.id.testStart);
        textView.setText(TimeUtil.getPostDateString(getContext(),testResult.getStartTime()/1000));
        textView = rootView.findViewById(R.id.testEnd);
        textView.setText(TimeUtil.getPostDateString(getContext(),testResult.getEndTime()/1000));
        textView = rootView.findViewById(R.id.totalTime);
        textView.setText(TimeUtil.getDurationWithNamesStringMills(getContext(),
                testResult.getEndTime()-testResult.getStartTime()));
        textView = rootView.findViewById(R.id.testScore);
        if(getContext()!=null) {
            textView.setText(getContext().getString(R.string.score_div, testResult.getScore(), testResult.getTotalScore()));
        }



        return rootView;
    }

    @NonNull
    @Override
    public View inflateRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_test_results, container, false);
    }

    @Override
    public void onClearSavedInstance(@NonNull Bundle savedInstanceState) {
        super.onClearSavedInstance(savedInstanceState);
        savedInstanceState.remove("test");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(test !=null){
            outState.putParcelable("test", test);
        }
        super.onSaveInstanceState(outState);
    }

}
