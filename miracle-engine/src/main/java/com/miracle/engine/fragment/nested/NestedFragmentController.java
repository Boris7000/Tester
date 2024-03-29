package com.miracle.engine.fragment.nested;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.miracle.engine.fragment.IMiracleFragment;
import com.miracle.engine.fragment.MiracleFragmentController;
import com.miracle.engine.fragment.tabs.ITabsFragment;


public abstract class NestedFragmentController extends MiracleFragmentController {

    private final INestedFragment nestedFragment;
    private ITabsFragment tabsFragment;

    protected NestedFragmentController(IMiracleFragment miracleFragment) {
        super(miracleFragment);
        nestedFragment = (INestedFragment) miracleFragment;
    }

    public final INestedFragment getNestedFragment() {
        return nestedFragment;
    }

    public final ITabsFragment getTabsFragment() {
        return tabsFragment;
    }

    @Override
    public void onCreateView(@NonNull View rootView, Bundle savedInstanceState){
        Fragment fragment = nestedFragment.getParentFragment();
        if(fragment!=null){
            if(fragment instanceof ITabsFragment){
                tabsFragment = (ITabsFragment) fragment;
            }
        }
    }

    @Override
    public void findViews(@NonNull View rootView){}

    @Override
    public void initViews(){}
}
