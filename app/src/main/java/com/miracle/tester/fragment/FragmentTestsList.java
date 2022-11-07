package com.miracle.tester.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.miracle.engine.adapter.MiracleAdapter;
import com.miracle.engine.adapter.holder.ItemDataHolder;
import com.miracle.engine.fragment.FragmentFabric;
import com.miracle.engine.fragment.MiracleFragment;
import com.miracle.engine.fragment.base.BaseRecyclerFragment;
import com.miracle.tester.R;
import com.miracle.tester.adapter.TestsAdapter;
import com.miracle.tester.model.Test;
import com.miracle.tester.model.TestTask;
import com.miracle.tester.util.StorageUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentTestsList extends BaseRecyclerFragment {

    private final ActivityResultLauncher<Intent> intentActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
                //Log.d("TAG", result.toString());
                if(result.getResultCode() == Activity.RESULT_OK) {
                    if(result.getData()!=null) {
                        Intent data = result.getData();
                        //Log.d("TAG", data.getData().toString());
                        Activity activity = getActivity();
                        if(activity!=null){
                            try {
                                ParcelFileDescriptor pfd = activity.getContentResolver().openFileDescriptor(data.getData(), "r");
                                FileInputStream fileInputStream = new FileInputStream(pfd.getFileDescriptor());

                                // Finds the workbook instance for XLSX file
                                OPCPackage pkg = OPCPackage.open(fileInputStream);
                                XSSFWorkbook myWorkBook = new XSSFWorkbook(pkg);
                                // Return first sheet from the XLSX workbook
                                XSSFSheet mySheet = myWorkBook.getSheetAt(0);
                                // Get iterator to all the rows in current sheet

                                String title = "";
                                ArrayList<TestTask> tasks = new ArrayList<>();
                                ArrayList<Integer> rightAnswers = new ArrayList<>();
                                ArrayList<String> answers;
                                String question = "";

                                int rowCounter = 0;
                                for (Row row : mySheet) {
                                    if(rowCounter==0){
                                        for (Cell cell : row) {
                                            if(cell.getCellType()==Cell.CELL_TYPE_STRING){
                                                title = cell.getStringCellValue();
                                                break;
                                            }
                                        }
                                    } else {
                                        int cellCounter = 0;
                                        boolean ra = rowCounter%2==1;
                                        if (ra){
                                            rightAnswers = new ArrayList<>();
                                            boolean fromStart = false;
                                            int startFrom = 2;
                                            for (Cell cell : row) {
                                                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                                    if(!fromStart) {
                                                        if (cell.getStringCellValue().equals("{+}") || cell.getStringCellValue().equals("{-}")) {
                                                            fromStart = true;
                                                            startFrom = cellCounter;
                                                        }
                                                    }
                                                    if (cell.getStringCellValue().equals("{+}")) {
                                                        rightAnswers.add(cellCounter-startFrom);
                                                    }
                                                }
                                                cellCounter++;
                                            }
                                        } else {
                                            answers = new ArrayList<>();
                                            for (Cell cell : row) {
                                                if(cellCounter==1){
                                                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                                        question = cell.getStringCellValue();
                                                    }
                                                } else {
                                                    if(cellCounter>1) {
                                                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                                            answers.add(cell.getStringCellValue());
                                                        }
                                                    }
                                                }
                                                cellCounter++;
                                            }

                                            TestTask testTask = new TestTask(question, answers, rightAnswers);
                                            tasks.add(testTask);

                                            /*
                                            Log.d("TAG", "right answers: "+rightAnswers);
                                            Log.d("TAG", "question: "+question);
                                            Log.d("TAG", "answers: "+answers);
                                            Log.d("TAG", "--------------------------------- ");
                                            */


                                        }
                                    }
                                    rowCounter++;
                                }
                                pkg.close(); // gracefully closes the underlying zip file

                                Test test = new Test(title, tasks);

                                StorageUtil storageUtil = StorageUtil.get();
                                if(storageUtil.addNewTestAndSave(test)){
                                    RecyclerView.Adapter<?> adapter = getRecyclerFragmentController().getRecyclerView().getAdapter();
                                    if(adapter instanceof MiracleAdapter){
                                        MiracleAdapter miracleAdapter = (MiracleAdapter) adapter;
                                        ArrayList<ItemDataHolder> itemDataHolders = miracleAdapter.getItemDataHolders();
                                        itemDataHolders.add(1,test);
                                        miracleAdapter.notifyItemInserted(1);
                                    }
                                }

                            } catch (IOException | InvalidFormatException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } //else {
                    //Log.d("TAG", "something error");
                //}
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View inflateRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tests, container, false);
    }

    @Override
    public RecyclerView.Adapter<?> onCreateRecyclerAdapter() {
        return new TestsAdapter();
    }

    @Override
    public boolean needChangeTitleText() {
        return false;
    }

    public static class Fabric implements FragmentFabric {
        @NonNull
        @Override
        public MiracleFragment createFragment() {
            return new FragmentTestsList();
        }
    }

    public void openSomeActivityForResult(Intent intent){
        intentActivityResultLauncher.launch(intent);
    }

}
