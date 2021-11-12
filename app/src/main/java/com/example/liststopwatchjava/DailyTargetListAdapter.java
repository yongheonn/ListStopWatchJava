package com.example.liststopwatchjava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DailyTargetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Integer> pageTarget;
    private ArrayList<String> pageName;
    private ArrayList<String> unitTime;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(int _position);
    }

    public DailyTargetListAdapter(OnItemClickListener _onItemClickListener) {
        pageTarget = new ArrayList<Integer>();
        pageName = new ArrayList<String>();
        unitTime = new ArrayList<String>();
        onItemClickListener = _onItemClickListener;
    }

    public DailyTargetListAdapter(ArrayList<Integer> _pageTarget, ArrayList<String> _pageName
            , ArrayList<String> _unitTime, OnItemClickListener _onItemClickListener) {
        pageTarget = new ArrayList<Integer>();
        pageName = new ArrayList<String>();
        unitTime = new ArrayList<String>();

        pageTarget.addAll(_pageTarget);
        pageName.addAll(_pageName);
        unitTime.addAll(_unitTime);
        onItemClickListener = _onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {
        return CreateItemHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof DailyTargetViewHolder)
            ((DailyTargetViewHolder) viewHolder).setTextView(pageTarget.get(position), pageName.get(position)
                    , unitTime.get(position));
    }

    @Override
    public int getItemCount() {
        return pageName.size();
    }

    public void addItem(int _pageTarget, String _pageName, String _unitTime) {
        pageTarget.add(_pageTarget);
        pageName.add(_pageName);
        unitTime.add(_unitTime);
        notifyItemInserted(getItemCount() - 1);
    }

    public void deleteItem(int _position) {
        pageTarget.remove(_position);
        pageName.remove(_position);
        unitTime.remove(_position);
        notifyItemRemoved(_position);
    }

    public void editItme(int _position, int _pageTarget, String _pageName, String _unitTime) {
        pageTarget.set(_position, _pageTarget);
        pageName.set(_position, _pageName);
        unitTime.set(_position, _unitTime);
        notifyItemChanged(_position);
    }

    public ArrayList<Integer> getPageTarget() {
        return pageTarget;
    }

    public ArrayList<String> getPageName() {
        return pageName;
    }

    public ArrayList<String> getUnitTime() {
        return unitTime;
    }

    private DailyTargetViewHolder CreateItemHolder(final ViewGroup viewGroup) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.daily_item, viewGroup, false);
        final DailyTargetViewHolder viewHolder = new DailyTargetViewHolder(view);
        setClickListener(viewHolder);

        return viewHolder;
    }

    private void setClickListener(final DailyTargetViewHolder viewHolder) {
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(viewHolder.getAdapterPosition());
            }
        });
    }
}
