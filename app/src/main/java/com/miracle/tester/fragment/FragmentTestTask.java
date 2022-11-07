package com.miracle.tester.fragment;

import static com.miracle.engine.util.NavigationUtil.back;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.miracle.button.TextViewButton;
import com.miracle.engine.fragment.MiracleFragment;
import com.miracle.engine.fragment.NestedMiracleFragmentFabric;
import com.miracle.engine.fragment.nested.NestedListFragment;
import com.miracle.engine.fragment.tabs.ITabsFragment;
import com.miracle.tester.R;
import com.miracle.tester.model.TestTask;

import java.util.ArrayList;

public class FragmentTestTask extends NestedListFragment {

    private TestTask testTask;

    private ViewStub multipleAnswerStub;
    private ViewStub singleAnswerStub;

    private LinearLayout answerContainer;
    private LinearLayout multipleAnswerHolder;
    private LinearLayout checkBoxes;
    private LinearLayout singleAnswerHolder;
    private RadioGroup radioGroup;

    private TextViewButton textViewButton;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState!=null&&!savedInstanceState.isEmpty()){
            TestTask testTask = savedInstanceState.getParcelable("testTask");
            if(testTask!=null){
                this.testTask = testTask;
            } else {
                back(getContext());
            }
        }

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        TextView textView = rootView.findViewById(R.id.question);

        textView.setText(testTask.getQuestion());

        if(testTask.getComplete()==-1) {

            if(testTask.getRightAnswers().size()>1){
                if (multipleAnswerHolder == null) {
                    multipleAnswerHolder = (LinearLayout) multipleAnswerStub.inflate();
                    checkBoxes = multipleAnswerHolder.findViewById(R.id.checkboxes);
                }

                CompoundButton.OnCheckedChangeListener changeListener = (compoundButton, b) -> {
                    boolean hasChecked = false;
                    for (int i = 0; i < checkBoxes.getChildCount(); i++) {
                        CheckBox checkBox = (CheckBox) checkBoxes.getChildAt(i);
                        if (checkBox.isChecked()) {
                            hasChecked = true;
                            break;
                        }
                    }
                    textViewButton.setEnabled(hasChecked, true);
                };

                ArrayList<String> answers = testTask.getAnswers();
                for (int i = 0; i < answers.size(); i++) {
                    CheckBox checkBox = (CheckBox) inflater.inflate(R.layout.view_check_box_stub, checkBoxes, false);
                    checkBox.setText(answers.get(i));
                    checkBox.setOnCheckedChangeListener(changeListener);
                    checkBoxes.addView(checkBox);
                }

                textViewButton.setOnClickListener(view -> {
                    if(textViewButton.isEnabled()) {
                        ArrayList<Integer> rightAnswers = testTask.getRightAnswers();
                        int sum = 0;

                        for (int i = 0; i < checkBoxes.getChildCount(); i++) {
                            CheckBox checkBox = (CheckBox) checkBoxes.getChildAt(i);
                            if (checkBox.isChecked()) {
                                if (rightAnswers.contains(i)) {
                                    sum++;
                                } else {
                                    sum = 0;
                                    break;
                                }
                            }
                        }

                        if (sum == rightAnswers.size()) {
                            testTask.setComplete((byte) 1);
                        } else {
                            testTask.setComplete((byte) 0);
                        }

                        sendResult();
                    }
                });
            } else {
                if (singleAnswerHolder == null) {
                    singleAnswerHolder = (LinearLayout) singleAnswerStub.inflate();
                    radioGroup = singleAnswerHolder.findViewById(R.id.radioGroup);
                }

                ArrayList<String> answers = testTask.getAnswers();
                for (int i = 0; i < answers.size(); i++) {
                    RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.view_radio_button_stub, radioGroup, false);
                    radioButton.setText(answers.get(i));
                    radioGroup.addView(radioButton);
                }

                radioGroup.setOnCheckedChangeListener((radioGroup, id) -> textViewButton.setEnabled(true, true));

                textViewButton.setOnClickListener(view -> {
                    if(textViewButton.isEnabled()) {
                        ArrayList<Integer> rightAnswers = testTask.getRightAnswers();
                        int sum = 0;

                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                            if (radioButton.isChecked()) {
                                if (rightAnswers.contains(i)) {
                                    sum++;
                                }
                            }
                        }

                        if (sum == rightAnswers.size()) {
                            testTask.setComplete((byte) 1);
                        } else {
                            testTask.setComplete((byte) 0);
                        }

                        sendResult();
                    }
                });
            }
        } else {
            hideAnswer();
        }

        return rootView;
    }

    @NonNull
    @Override
    public View inflateRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_test_task, container, false);
    }

    @Override
    public void findViews(@NonNull View rootView) {
        super.findViews(rootView);
        textViewButton = rootView.findViewById(R.id.answerButton);
        multipleAnswerStub = rootView.findViewById(R.id.multipleAnswerStub);
        singleAnswerStub = rootView.findViewById(R.id.singleAnswerStub);
        answerContainer = rootView.findViewById(R.id.answerContainer);
    }

    private void hideAnswer(){
        answerContainer.setVisibility(View.GONE);
    }

    private void sendResult(){
        hideAnswer();
        ITabsFragment iTabsFragment = getNestedFragmentController().getTabsFragment();
        if(iTabsFragment!=null) {
            if(iTabsFragment instanceof FragmentTest) {
                FragmentTest fragmentTest = (FragmentTest) iTabsFragment;
                fragmentTest.onTaskAnswerListener.onTaskAnswer(testTask);
            }
        }
    }

    @Override
    public void onClearSavedInstance(@NonNull Bundle savedInstanceState) {
        super.onClearSavedInstance(savedInstanceState);
        savedInstanceState.remove("testTask");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(testTask !=null){
            outState.putParcelable("testTask", testTask);
        }
        super.onSaveInstanceState(outState);
    }

    public static class Fabric extends NestedMiracleFragmentFabric {

        private final TestTask task;

        public Fabric(TestTask task, String title, int icon) {
            super(title, icon);
            this.task = task;
        }

        @NonNull
        @Override
        public MiracleFragment createFragment() {
            FragmentTestTask fragmentTestTask = new FragmentTestTask();
            fragmentTestTask.testTask = task;
            return fragmentTestTask;
        }
    }

}
