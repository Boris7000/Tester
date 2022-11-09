package com.miracle.tester;

import static com.miracle.engine.view.ActivityRootView.STATE_CLEAR;
import static com.miracle.engine.view.ActivityRootView.STATE_STANDARD;
import static com.miracle.engine.view.ActivityRootView.TYPE_LAND;
import static com.miracle.engine.view.ActivityRootView.TYPE_PORTRAIT;
import static com.miracle.engine.view.ActivityRootView.TYPE_TABLET;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.collection.ArrayMap;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.miracle.engine.activity.tabs.TabsActivity;
import com.miracle.engine.activity.tabs.TabsActivityController;
import com.miracle.engine.fragment.FragmentFabric;
import com.miracle.engine.view.TabsFragmentContainer;
import com.miracle.tester.fragment.FragmentTestsList;
import com.miracle.tester.util.StorageUtil;

public class MainActivity extends TabsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StorageUtil.get().initializeDirectories();

        if(savedInstanceState==null){
            savedInstanceState = savedInstanceStateCrutch();
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public int getRootViewResource() {
        return R.layout.activity_main;
    }

    @Override
    public ArrayMap<Integer, FragmentFabric> loadTabs() {
        ArrayMap<Integer, FragmentFabric> fabrics = new ArrayMap<>();
        fabrics.put(R.id.tab_tests, new FragmentTestsList.Fabric());
        return fabrics;
    }

    @Override
    public ArrayMap<Integer, FragmentFabric> getErrorTabs() {
        ArrayMap<Integer, FragmentFabric> fabrics = new ArrayMap<>();
        fabrics.put(R.id.tab_tests, new FragmentTestsList.Fabric());
        return fabrics;
    }

    @Override
    public int defaultTab() {
        return R.id.tab_tests;
    }

    @CallSuper
    @Override
    public void onNewWindowInsets(WindowInsetsCompat windowInsets){
        updateInsets(windowInsets);
    }

    private void updateInsets(WindowInsetsCompat windowInsets){

        if(windowInsets!=null) {
            Insets systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets systemGesturesInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures());
            Insets imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime());

            //height of player bar and bottom navigation menu
            final int bottomInsets = Math.max(systemGesturesInsets.bottom, Math.max(systemBarsInsets.bottom, imeInsets.bottom));

            TabsActivityController tabsActivityController = getTabsActivityController();
            TabsFragmentContainer tabsFragmentContainer = tabsActivityController.getTabsFragmentContainer();

            switch (getRootView().getType()){
                case TYPE_PORTRAIT: {
                    switch (getRootView().getState()) {
                        case STATE_CLEAR:
                        case STATE_STANDARD: {
                            if(tabsFragmentContainer.getPaddingBottom()!=bottomInsets||
                                    tabsFragmentContainer.getPaddingTop()!=systemBarsInsets.top) {
                                tabsFragmentContainer.setPadding(
                                        tabsFragmentContainer.getPaddingLeft(),
                                        systemBarsInsets.top,
                                        tabsFragmentContainer.getPaddingRight(),
                                        bottomInsets);
                            }
                            break;
                        }
                    }
                    break;
                }
                case TYPE_LAND:
                case TYPE_TABLET: {
                    switch (getRootView().getState()) {
                        case STATE_CLEAR:
                        case STATE_STANDARD: {
                            if(tabsFragmentContainer.getPaddingBottom()!=bottomInsets||
                                    tabsFragmentContainer.getPaddingTop()!=systemBarsInsets.top||
                                    tabsFragmentContainer.getPaddingLeft()!=systemBarsInsets.left||
                                    tabsFragmentContainer.getPaddingRight()!=systemBarsInsets.right) {
                                tabsFragmentContainer.setPadding(
                                        systemBarsInsets.left,
                                        systemBarsInsets.top,
                                        systemBarsInsets.right,
                                        bottomInsets);
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

}