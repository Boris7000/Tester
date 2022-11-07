package com.miracle.engine.fragment.nested;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.miracle.engine.fragment.ScrollAndElevate;
import com.miracle.engine.fragment.base.IBaseFragment;
import com.miracle.engine.fragment.list.ListFragment;
import com.miracle.engine.fragment.tabs.ITabsFragment;

public abstract class NestedListFragment extends ListFragment implements INestedFragment {

    private final NestedFragmentController nestedFragmentController = requestNestedFragmentController();

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        getNestedFragmentController().onCreateView(rootView, savedInstanceState);

        getNestedFragmentController().onCreateView(rootView, savedInstanceState);

        ITabsFragment iTabsFragment = getNestedFragmentController().getTabsFragment();
        if(iTabsFragment!=null) {
            if (iTabsFragment instanceof IBaseFragment) {
                IBaseFragment iBaseFragment = (IBaseFragment) iTabsFragment;
                if (iBaseFragment.scrollAndElevateEnabled()) {
                    ScrollAndElevate.scrollAndElevate(getScrollView(), iBaseFragment.getAppBarLayout());
                }
            }
        }

        return rootView;
    }

    @CallSuper
    @Override
    public void findViews(@NonNull View rootView) {
        super.findViews(rootView);
        getNestedFragmentController().findViews(rootView);
    }

    @CallSuper
    @Override
    public void initViews() {
        super.initViews();
        getNestedFragmentController().initViews();
    }

    @Override
    public NestedFragmentController requestNestedFragmentController() {
        return new NestedFragmentController(this) {};
    }

    @Override
    public NestedFragmentController getNestedFragmentController() {
        return nestedFragmentController;
    }

}
