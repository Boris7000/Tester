package com.miracle.tester.adapter;

import static com.miracle.engine.adapter.holder.ViewHolderTypes.TYPE_ERROR;
import static com.miracle.engine.adapter.holder.ViewHolderTypes.TYPE_LOADING;
import static com.miracle.tester.adapter.holder.ViewHolderTypes.TYPE_ADD_NEW_TEST;
import static com.miracle.tester.adapter.holder.ViewHolderTypes.TYPE_TEST;

import android.util.ArrayMap;

import com.miracle.engine.adapter.MiracleInstantLoadAdapter;
import com.miracle.engine.adapter.holder.ItemDataHolder;
import com.miracle.engine.adapter.holder.ViewHolderFabric;
import com.miracle.engine.adapter.holder.error.ErrorViewHolder;
import com.miracle.engine.adapter.holder.loading.LoadingViewHolder;
import com.miracle.tester.adapter.holder.AddNewTestViewHolder;
import com.miracle.tester.adapter.holder.TestViewHolder;
import com.miracle.tester.model.Test;
import com.miracle.tester.model.TestTask;
import com.miracle.tester.util.StorageUtil;

import java.util.ArrayList;

public class TestsAdapter extends MiracleInstantLoadAdapter {

    @Override
    public void onLoading() {

        ArrayList<ItemDataHolder> holders = getItemDataHolders();

        ArrayList<ItemDataHolder> dataHolders = new ArrayList<>();

        dataHolders.add(() -> TYPE_ADD_NEW_TEST);

        dataHolders.addAll(StorageUtil.get().loadTests());

        holders.clear();
        holders.addAll(dataHolders);
        setAddedCount(dataHolders.size());

        setFinallyLoaded(true);

    }

    @Override
    public ArrayMap<Integer, ViewHolderFabric> getViewHolderFabricMap() {
        ArrayMap<Integer, ViewHolderFabric> arrayMap = super.getViewHolderFabricMap();
        arrayMap.put(TYPE_LOADING, new LoadingViewHolder.Fabric());
        arrayMap.put(TYPE_ERROR, new ErrorViewHolder.Fabric());
        arrayMap.put(TYPE_TEST, new TestViewHolder.Fabric());
        arrayMap.put(TYPE_ADD_NEW_TEST, new AddNewTestViewHolder.Fabric());
        return arrayMap;
    }
}
