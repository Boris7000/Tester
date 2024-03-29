package com.miracle.engine.fragment.side;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.miracle.engine.fragment.IMiracleFragment;
import com.miracle.engine.fragment.MiracleFragmentController;
import com.miracle.engine.fragment.base.IBaseFragment;
import com.miracle.engine.util.NavigationUtil;


public abstract class SideFragmentController extends MiracleFragmentController {

    private final IBaseFragment baseFragment;
    private final ISideFragment sideFragment;

    protected SideFragmentController(IMiracleFragment miracleFragment) {
        super(miracleFragment);
        baseFragment = (IBaseFragment) miracleFragment;
        sideFragment = (ISideFragment) miracleFragment;
    }

    public final IBaseFragment getBaseFragment() {
        return baseFragment;
    }

    public final ISideFragment getSideFragment() {
        return sideFragment;
    }

    @Override
    public void onCreateView(@NonNull View rootView, Bundle savedInstanceState){

    }

    @Override
    public void findViews(@NonNull View rootView){

    }

    @Override
    public void initViews(){
        Toolbar toolbar = baseFragment.getToolBar();
        if(toolbar!=null) {
            toolbar.setNavigationOnClickListener(v -> NavigationUtil.back(toolbar.getContext()));
        }
    }

}
