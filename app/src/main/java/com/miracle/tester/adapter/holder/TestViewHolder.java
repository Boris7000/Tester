package com.miracle.tester.adapter.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.miracle.button.TextViewButton;
import com.miracle.engine.adapter.MiracleAdapter;
import com.miracle.engine.adapter.holder.ItemDataHolder;
import com.miracle.engine.adapter.holder.MiracleViewHolder;
import com.miracle.engine.adapter.holder.ViewHolderFabric;
import com.miracle.tester.R;
import com.miracle.tester.dialog.TestDialog;
import com.miracle.tester.dialog.TestDialogActionListener;
import com.miracle.tester.model.Test;
import com.miracle.tester.util.NavigationUtil;
import com.miracle.tester.util.StorageUtil;

import java.util.ArrayList;

public class TestViewHolder extends MiracleViewHolder {

    private final TextViewButton textViewButton;
    private Test test;

    public TestViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewButton = (TextViewButton) itemView;
        textViewButton.setOnClickListener(view -> NavigationUtil.goToTest(test, view.getContext()));
        textViewButton.setOnLongClickListener(view -> {
            Context context = getContext();
            itemView.getParent().requestDisallowInterceptTouchEvent(true);
            TestDialog testDialog = new TestDialog(context, test);
            TestDialogActionListener listener = new TestDialogActionListener() {
                @Override
                public void deleteResult() {
                    test.clearTestResult();
                    StorageUtil.get().updateTestAndSave(test);
                }

                @Override
                public void erase() {
                    MiracleAdapter adapter = getMiracleAdapter();
                    ArrayList<ItemDataHolder> itemDataHolders = adapter.getItemDataHolders();
                    int pos = itemDataHolders.indexOf(test);
                    itemDataHolders.remove(test);
                    adapter.notifyItemRemoved(pos);
                    StorageUtil.get().removeTestAndSave(test);
                }
            };
            testDialog.setTestDialogActionListener(listener);
            testDialog.show(context);
            return true;
        });
    }

    @Override
    public void bind(ItemDataHolder itemDataHolder) {
        super.bind(itemDataHolder);
        test = (Test) itemDataHolder;
        textViewButton.setText(test.getTitle());
    }

    public static class Fabric implements ViewHolderFabric {
        @Override
        public MiracleViewHolder create(LayoutInflater inflater, ViewGroup viewGroup) {
            return new TestViewHolder(inflater.inflate(R.layout.view_test_item, viewGroup, false));
        }
    }

}
