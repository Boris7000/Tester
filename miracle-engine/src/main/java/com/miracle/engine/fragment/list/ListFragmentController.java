package com.miracle.engine.fragment.list;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.miracle.engine.R;
import com.miracle.engine.fragment.IMiracleFragment;
import com.miracle.engine.fragment.MiracleFragmentController;

public abstract class ListFragmentController extends MiracleFragmentController {

    private final IListFragment listFragment;
    private NestedScrollView scrollView;

    protected ListFragmentController(IMiracleFragment miracleFragment) {
        super(miracleFragment);
        listFragment = (IListFragment) miracleFragment;
    }

    public final IListFragment getListFragment() {
        return listFragment;
    }

    @Override
    public void onCreateView(@NonNull View rootView, Bundle savedInstanceState){}

    @Override
    public void findViews(@NonNull View rootView){
        scrollView = rootView.findViewById(R.id.scrollView);
    }

    @Override
    public void initViews(){}

    public final NestedScrollView getScrollView() {
        return scrollView;
    }

    @CallSuper
    public void setScrollView(NestedScrollView scrollView){
        this.scrollView = scrollView;
    }

    @CallSuper
    public boolean notTop(){
        if (scrollView != null) {
            return scrollView.canScrollVertically(-1);
        }
        return false;
    }

    @CallSuper
    public void scrollToTop() {
        if (scrollView != null) {
            if(scrollView.canScrollVertically(-1)) {
                scrollView.scrollTo(0, 0);
            }
        }
    }

}
