package com.miracle.tester.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.miracle.engine.fragment.FragmentFabric;
import com.miracle.engine.fragment.MiracleFragment;
import com.miracle.engine.fragment.base.BaseRefreshListFragment;
import com.miracle.tester.R;

public class FragmentMisc extends BaseRefreshListFragment {
    @NonNull
    @Override
    public View inflateRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_misc, container, false);
    }

    @Override
    public SwipeRefreshLayout.OnRefreshListener requestOnRefreshListener() {
        return null;
    }

    public static class Fabric implements FragmentFabric {
        @NonNull
        @Override
        public MiracleFragment createFragment() {
            return new FragmentMisc();
        }
    }

}