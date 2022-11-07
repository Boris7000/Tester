package com.miracle.tester.adapter.holder;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.miracle.button.TextViewButton;
import com.miracle.engine.adapter.holder.ItemDataHolder;
import com.miracle.engine.adapter.holder.MiracleViewHolder;
import com.miracle.engine.adapter.holder.ViewHolderFabric;
import com.miracle.engine.context.ContextExtractor;
import com.miracle.tester.MainActivity;
import com.miracle.tester.R;
import com.miracle.tester.fragment.FragmentTestsList;

public class AddNewTestViewHolder extends MiracleViewHolder {

    public AddNewTestViewHolder(@NonNull View itemView) {
        super(itemView);
        TextViewButton textViewButton = (TextViewButton) itemView;
        textViewButton.setOnClickListener(view -> {
            //TODO добавить открытие из файла
            /*
            Test test = generateFakeTest();
            StorageUtil storageUtil = StorageUtil.get();
            if(storageUtil.addNewTestAndSave(test)){
                MiracleAdapter adapter = getMiracleAdapter();
                ArrayList<ItemDataHolder> itemDataHolders = adapter.getItemDataHolders();
                itemDataHolders.add(1,test);
                adapter.notifyItemInserted(1);
            }
             */


            Activity activity = ContextExtractor.extractActivity(view.getContext());
            if(activity!=null){
                if(android.os.Build.VERSION.SDK_INT<=Build.VERSION_CODES.R) {
                    if (checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("TAG", "Permission is granted");
                        //File write logic here
                        openXlsxFile((FragmentTestsList) getMiracleAdapter().getMiracleFragment());
                    } else {
                        Log.d("TAG", "Permission is not granted");
                        activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PackageManager.PERMISSION_GRANTED);
                    }
                } else {
                    openXlsxFile((FragmentTestsList) getMiracleAdapter().getMiracleFragment());
                }
            } else {
                Log.d("TAG","Context is null");
            }
        });
    }

    public static void openXlsxFile(FragmentTestsList fragmentTestsList){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimeTypes = {"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        fragmentTestsList.openSomeActivityForResult(intent);
    }

    @Override
    public void bind(ItemDataHolder itemDataHolder) {
        super.bind(itemDataHolder);
    }

    public static class Fabric implements ViewHolderFabric {
        @Override
        public MiracleViewHolder create(LayoutInflater inflater, ViewGroup viewGroup) {
            return new AddNewTestViewHolder(inflater.inflate(R.layout.view_add_new_test_item, viewGroup, false));
        }
    }
}
