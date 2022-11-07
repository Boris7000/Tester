package com.miracle.engine.fragment.tabs;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import com.miracle.engine.fragment.NestedMiracleFragmentFabric;

import java.util.ArrayList;

public interface ITabsFragment {

    TabsFragmentController requestTabsFragmentController();

    TabsFragmentController getTabsFragmentController();

    ArrayList<NestedMiracleFragmentFabric> loadTabs();

    ArrayList<NestedMiracleFragmentFabric> getErrorTabs();

    int defaultTab();

    int customTabItemViewResourceId();

    boolean asyncLoadTabs();

    void onPageSelected(int pageIndex);

    FragmentManager getChildFragmentManager();

    FragmentManager getParentFragmentManager();

    Lifecycle getLifecycle();

}
