package com.example.liststopwatchjava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private DailyTarget data;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    public CategoryInfoListAdapter(DailyTarget _data) {
        data = _data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_HEADER)
            return CreateHeaderHolder(viewGroup);
        if(viewType == TYPE_ITEM)
            return CreateItemHolder(viewGroup);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof  TotalPercentageVH) {
            ((TotalPercentageVH) viewHolder).setTotalPercentage(data.page, data.pageTarget
                    , data.time, data.unitTime);
        }
        if (viewHolder instanceof PercentageViewHolder) {
            int pageProgress = data.page.get(position - 1);
            int pageTotal = data.pageTarget.get(position - 1);
            double time = data.time.get(position - 1);
            String pageName = data.pageName.get(position - 1);
            String unitTime = data.unitTime.get(position - 1);
            ((PercentageViewHolder) viewHolder).setPercentage(pageProgress, pageTotal, time
            , pageName, unitTime);
        }
    }

    @Override
    public int getItemCount() {
        return data.pageName.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        if (position == 0)
            return true;
        return false;
    }

    private TotalPercentageVH CreateHeaderHolder(final ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.percentage_total, viewGroup, false);
        final TotalPercentageVH viewHolder = new TotalPercentageVH(view);

        return viewHolder;
    }

    private PercentageViewHolder CreateItemHolder(final ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.percentage_bar, viewGroup, false);
        final PercentageViewHolder viewHolder = new PercentageViewHolder(view);

        return viewHolder;
    }
}
