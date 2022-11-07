package com.miracle.tester.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.miracle.button.TextViewButton;
import com.miracle.engine.dialog.MiracleBottomDialog;
import com.miracle.engine.util.TimeUtil;
import com.miracle.tester.R;
import com.miracle.tester.model.Test;

public class TestDialog extends MiracleBottomDialog {

    private final Test test;
    private TestDialogActionListener testDialogActionListener;

    public TestDialog(@NonNull Context context, Test test) {
        super(context);
        this.test = test;
    }

    @Override
    public void show(Context context) {
        View rootView;
        setContentView(rootView =  View.inflate(context, R.layout.dialog_test, null));

        TextView title = rootView.findViewById(R.id.title);
        TextView subtitle = rootView.findViewById(R.id.subtitle);

        title.setText(test.getTitle());

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLayout = rootView.findViewById(R.id.buttonsContainer);
        TextViewButton miracleButton;

        if(test.getTestResult()!=null){
            subtitle.setVisibility(View.VISIBLE);
            subtitle.setText("Пройден "+ TimeUtil.getOnlineDateString(context, test.getTestResult().getEndTime()/1000));
            miracleButton = (TextViewButton) inflater.inflate(R.layout.dialog_button_stub, linearLayout, false);
            miracleButton.setText(context.getString(R.string.remove_test_result));
            miracleButton.setIconStartImageResource(R.drawable.ic_text_doc_28);
            miracleButton.setOnClickListener(view -> {
                testDialogActionListener.deleteResult();
                cancel();
            });
            linearLayout.addView(miracleButton);
        }

        miracleButton = (TextViewButton) inflater.inflate(R.layout.dialog_button_stub, linearLayout, false);
        miracleButton.setText(context.getString(R.string.delete));
        miracleButton.setIconStartImageResource(R.drawable.ic_delete_28);
        miracleButton.setOnClickListener(view -> {
            testDialogActionListener.erase();
            cancel();
        });
        linearLayout.addView(miracleButton);

        TextViewButton cancelButton = rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> cancel());

        show();
        expand();
    }

    public void setTestDialogActionListener(TestDialogActionListener testDialogActionListener) {
        this.testDialogActionListener = testDialogActionListener;
    }
}
