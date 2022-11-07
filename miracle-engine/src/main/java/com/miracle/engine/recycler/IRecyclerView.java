package com.miracle.engine.recycler;

public interface IRecyclerView {

    MiracleViewRecycler getRecycledViewPool();

    void setRecycledViewPool(MiracleViewRecycler recycledViewPool);

}
