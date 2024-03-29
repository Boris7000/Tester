package com.miracle.engine.fragment.side;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.miracle.engine.fragment.base.BaseRefreshListFragment;

public abstract class SideRefreshListFragment extends BaseRefreshListFragment implements ISideFragment {

    private final SideFragmentController sideFragmentController = requestSideFragmentController();

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        getSideFragmentController().onCreateView(rootView, savedInstanceState);

        return rootView;
    }

    @CallSuper
    @Override
    public void findViews(@NonNull View rootView) {
        super.findViews(rootView);
        getSideFragmentController().findViews(rootView);
    }

    @CallSuper
    @Override
    public void initViews() {
        super.initViews();
        getSideFragmentController().initViews();
    }

    @Override
    public SideFragmentController requestSideFragmentController() {
        return new SideFragmentController(this){};
    }

    @Override
    public SideFragmentController getSideFragmentController() {
        return sideFragmentController;
    }

}
