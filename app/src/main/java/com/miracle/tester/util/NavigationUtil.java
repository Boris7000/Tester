package com.miracle.tester.util;

import android.content.Context;

import com.miracle.engine.activity.tabs.TabsActivity;
import com.miracle.engine.context.ContextExtractor;
import com.miracle.tester.fragment.FragmentTest;
import com.miracle.tester.fragment.FragmentTestResults;
import com.miracle.tester.model.Test;

public class NavigationUtil {

    public static void goToTest(Test test, Context context){
        TabsActivity tabsActivity = ContextExtractor.extractTabsActivity(context);
        if(tabsActivity!=null) {
            if(test.getTestResult()==null){
                FragmentTest fragmentTest = new FragmentTest();
                fragmentTest.setTest(test);
                tabsActivity.addFragment(fragmentTest);
            } else {
                FragmentTestResults fragmentTestResults = new FragmentTestResults();
                fragmentTestResults.setTest(test);
                tabsActivity.addFragment(fragmentTestResults);
            }
        }
    }


}
