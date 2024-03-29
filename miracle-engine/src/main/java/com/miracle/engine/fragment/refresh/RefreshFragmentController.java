package com.miracle.engine.fragment.refresh;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.miracle.engine.R;
import com.miracle.engine.fragment.IMiracleFragment;
import com.miracle.engine.fragment.MiracleFragmentController;
import com.miracle.engine.util.FragmentUtil;


public abstract class RefreshFragmentController extends MiracleFragmentController {

    private final IRefreshFragment refreshFragment;
    private SwipeRefreshLayout swipeRefreshLayout;

    protected RefreshFragmentController(IMiracleFragment miracleFragment) {
        super(miracleFragment);
        refreshFragment = (IRefreshFragment) miracleFragment;
    }

    public final IRefreshFragment getRefreshFragment() {
        return refreshFragment;
    }

    @Override
    public void onCreateView(@NonNull View rootView, Bundle savedInstanceState){}

    @Override
    public void findViews(@NonNull View rootView){
        swipeRefreshLayout = rootView.findViewById(R.id.refreshLayout);
    }

    @Override
    public void initViews(){
        if(swipeRefreshLayout!=null) {
            FragmentUtil.setDefaultSwipeRefreshLayoutStyle(
                    swipeRefreshLayout, swipeRefreshLayout.getContext());
            swipeRefreshLayout.setOnRefreshListener(refreshFragment.requestOnRefreshListener());
        }
    }

    public final SwipeRefreshLayout getSwipeRefreshLayout(){
        return swipeRefreshLayout;
    }

    @CallSuper
    public void show(){
        if (swipeRefreshLayout != null){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @CallSuper
    public void hide(){}

}
