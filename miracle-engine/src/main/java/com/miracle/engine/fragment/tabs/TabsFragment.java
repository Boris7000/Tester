package com.miracle.engine.fragment.tabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.miracle.engine.R;
import com.miracle.engine.fragment.FragmentError;
import com.miracle.engine.fragment.MiracleFragment;
import com.miracle.engine.fragment.NestedMiracleFragmentFabric;

import java.util.ArrayList;

public abstract class TabsFragment extends MiracleFragment implements ITabsFragment {

    private final TabsFragmentController tabsFragmentController = requestTabsFragmentController();

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        getTabsFragmentController().onCreateView(rootView, savedInstanceState);

        return rootView;
    }

    @CallSuper
    @Override
    public void findViews(@NonNull View rootView) {
        getTabsFragmentController().findViews(rootView);
    }

    @CallSuper
    @Override
    public void initViews() {
        getTabsFragmentController().initViews();
    }

    @Override
    public TabsFragmentController requestTabsFragmentController() {
        return new TabsFragmentController(this){};
    }

    @Override
    public TabsFragmentController getTabsFragmentController() {
        return tabsFragmentController;
    }

    @Override
    public ArrayList<NestedMiracleFragmentFabric> loadTabs() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<NestedMiracleFragmentFabric> getErrorTabs() {
        ArrayList<NestedMiracleFragmentFabric> fabrics = new ArrayList<>();
        Context context = getContext();
        String title="";
        String text="";
        if(context!=null){
            title = context.getString(R.string.error);
            text = context.getString(R.string.unknownError);
        }
        fabrics.add(new FragmentError.Fabric(text, title,-1));
        return fabrics;
    }

    @Override
    public int customTabItemViewResourceId() {
        return R.layout.view_custom_tab_item;
    }

    @Override
    public int defaultTab() {
        return 0;
    }

    @Override
    public boolean asyncLoadTabs() {
        return false;
    }

    @Override
    public void onPageSelected(int pageIndex) {

    }

    @Override
    public boolean notTop(){
        return getTabsFragmentController().notTop();
    }

    @Override
    public void scrollToTop() {
        getTabsFragmentController().scrollToTop();
    }

    @CallSuper
    @Override
    public void onClearSavedInstance(@NonNull Bundle savedInstanceState) {
        getTabsFragmentController().onClearSavedInstance(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        getTabsFragmentController().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}
