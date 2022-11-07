package com.miracle.tester.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miracle.tester.MainApp;
import com.miracle.tester.model.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class StorageUtil {

    private final String LOG_TAG = "StorageUtil";
    public static final String CACHES_NAME = "Caches";
    public static final String RESULTS_NAME = "Results";

    private static StorageUtil instance;

    public StorageUtil(){
        instance = this;
    }

    public static StorageUtil getInstance(){
        return new StorageUtil();
    }

    public static StorageUtil get(){
        StorageUtil localInstance = instance;
        if (localInstance == null) {
            synchronized (StorageUtil.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = getInstance();
                }
            }
        }
        return localInstance;
    }

    public void initializeDirectories(){
        File cachesDir = createNewDirectory(CACHES_NAME, MainApp.getInstance().getFilesDir());
        createNewFile(RESULTS_NAME, cachesDir);
    }

    ////////////////////////////////////////////////////

    public void removeDirectory(@Nullable File file){
        if(file!=null) {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    removeDirectory(f);
                }
            }
            if(file.delete()){
                Log.d(LOG_TAG, "Deleted "+file.getAbsolutePath());
            } else {
                Log.d(LOG_TAG, "Unable to delete "+file.getAbsolutePath());
            }
        }
    }

    public void writeObject(String path, File parent, Object object){
        ObjectOutputStream fileOutputStream = null;
        try {
            fileOutputStream = getOutputStream(path, parent);
            if(fileOutputStream!=null) {
                fileOutputStream.writeObject(object);
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public File createNewFile(String name, File parent){
        return createNewFile(new File(parent,name));
    }

    public File createNewFile(File file){
        if(!file.exists()) {
            try {
                if (file.createNewFile()) {
                    Log.d(LOG_TAG, "Created file "+file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public boolean deleteFile(String name, File parent){
        return deleteFile(new File(parent,name));
    }

    public boolean deleteFile(File file){
        if(file.exists()) {
            if (file.delete()) {
                Log.d(LOG_TAG, "Deleted file "+file.getAbsolutePath());
                return true;
            }
        }
        return false;
    }

    public File createNewDirectory(String name, File parent){
        File file = new File(parent,name);
        if(!file.exists()) {
            if (file.mkdir()) {
                Log.d(LOG_TAG, "Created directory "+file.getAbsolutePath());
            }
        }
        return file;
    }

    ////////////////////////////////////////////////////

    @NonNull
    private File getRootCachesDir(){
        return new File(MainApp.getInstance().getFilesDir(), CACHES_NAME);
    }

    @Nullable
    private ObjectOutputStream getOutputStream(String filename) throws IOException {
        File currentUserCachesDir = getRootCachesDir();
        return getOutputStream(filename, currentUserCachesDir);
    }

    @Nullable
    private ObjectInputStream getInputStream(String filename) throws IOException {
        File currentUserCachesDir = getRootCachesDir();
        return getInputStream(filename,currentUserCachesDir);
    }

    @Nullable
    private ObjectOutputStream getOutputStream(@Nullable String filename, @Nullable File parent) throws IOException {
        if(filename==null||parent==null){
            return null;
        } else {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(parent,filename));
            return new ObjectOutputStream(fileOutputStream);
        }
    }

    @Nullable
    private ObjectInputStream getInputStream(@Nullable String filename, @Nullable File parent) throws IOException {
        if(filename==null||parent==null){
            return null;
        } else {
            FileInputStream fileInputStream = new FileInputStream(new File(parent, filename));
            return new ObjectInputStream(fileInputStream);
        }
    }

    ////////////////////////////////////////////////////

    public static class ArrayListReader<T>{

        private final StorageUtil storageUtil;

        public ArrayListReader(StorageUtil storageUtil) {
            this.storageUtil = storageUtil;
        }

        public ArrayList<T> read(String path, File parent, AnonymousConverter<T> converter){
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = storageUtil.getInputStream(path, parent);
                if(objectInputStream!=null) {
                    ArrayList<?> objects = (ArrayList<?>) objectInputStream.readObject();
                    objectInputStream.close();
                    if (objects == null) {
                        objects = new ArrayList<>();
                    }
                    ArrayList<T> items = new ArrayList<>();
                    for (Object object : objects) {
                        if(object!=null){
                            items.add(converter.convert(object));
                        }
                    }
                    return items;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return new ArrayList<>();
        }
    }

    public interface AnonymousConverter<T>{
        T convert(Object object);
    }

    ////////////////////////////////////////////////////

    @NonNull
    public ArrayList<Test> loadTests(){
        return new ArrayListReader<Test>(this)
                .read(RESULTS_NAME, getRootCachesDir(), object -> (Test) object);
    }

    public void saveTests(ArrayList<Test> tests){
        writeObject(RESULTS_NAME, getRootCachesDir(), tests);
    }

    public boolean addNewTestAndSave(Test test){
        ArrayList<Test> tests = loadTests();
        for (Test t:tests) {
            if(t.getTitle().equals(test.getTitle())){
                return false;
            }
        }
        tests.add(0, test);
        saveTests(tests);
        return true;
    }

    public void removeTestAndSave(Test test){
        ArrayList<Test> tests = loadTests();
        int pos = -1;
        for (Test t:tests) {
            if(t.getTitle().equals(test.getTitle())){
                pos = tests.indexOf(t);
                break;
            }
        }
        if(pos>-1){
            tests.remove(pos);
        }
        saveTests(tests);
    }

    public void updateTestAndSave(Test test){
        ArrayList<Test> tests = loadTests();
        int pos = -1;
        for (Test t:tests) {
            if(t.getTitle().equals(test.getTitle())){
                pos = tests.indexOf(t);
                break;
            }
        }
        if(pos>-1){
            tests.remove(pos);
            tests.add(pos, test);
        }
        saveTests(tests);
    }

}
