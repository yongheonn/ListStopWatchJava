package com.example.liststopwatchjava;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DoListData> localDataSet;
    private OnItemClickListener mOnItemClickListener;
    private String title;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ADD = 1;
    private final int TYPE_CATEGORY = 2;
    private final int TYPE_ITEM = 3;

    public interface OnItemClickListener {
        public void onCategoryClick(int position, ArrayList<DoListData> dataSet);

        public void onItemClick(int position, ArrayList<DoListData> dataSet);

        public void onAddClick(int position);
    }

    public CustomAdapter(ArrayList<DoListData> dataSet, String _title, OnItemClickListener onItemClickListener) {
        localDataSet = dataSet;
        title = _title;
        mOnItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        if (viewType == TYPE_ITEM)
            return CreateItemHolder(viewGroup);
        if (viewType == TYPE_CATEGORY)
            return CreateCategoryHolder(viewGroup);
        if (viewType == TYPE_HEADER)
            return CreateHeaderHolder(viewGroup);
        if (viewType == TYPE_ADD)
            return CreateAddHolder(viewGroup);
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (viewHolder instanceof DoListViewHolder) {
            ((DoListViewHolder) viewHolder).getTextView().setText(localDataSet.get(position - 1).name);
        }
        else if(viewHolder instanceof CategoryViewHolder) {
            ((CategoryViewHolder) viewHolder).getTextView().setText(localDataSet.get(position - 1).name);
        }
        else if(viewHolder instanceof TitleViewHolder) {
            ((TitleViewHolder) viewHolder).getTextView().setText(title);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        if (isPositionAdd(position))
            return TYPE_ADD;
        if(isPositionCategory(position))
            return TYPE_CATEGORY;
        return TYPE_ITEM;
    }


    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionAdd(int position) {
        return position == getItemCount() - 1;
    }

    private boolean isPositionCategory(int position) {
        return localDataSet.get(position - 1).ioType == CustomIO.IOType.CATEGORY;
    }

    private TitleViewHolder CreateHeaderHolder(final ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_title, viewGroup, false);
        view.setSelected(true);
        final TitleViewHolder viewHolder = new TitleViewHolder(view);
        return viewHolder;
    }

    private DoListViewHolder CreateItemHolder(final ViewGroup viewGroup) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        final DoListViewHolder viewHolder = new DoListViewHolder(view);

        SetClickListener(viewHolder);
        TouchEffect(viewHolder, view);

        return viewHolder;
    }

    private CategoryViewHolder CreateCategoryHolder(final ViewGroup viewGroup) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category, viewGroup, false);
        final CategoryViewHolder viewHolder = new CategoryViewHolder(view);

        SetClickListener(viewHolder);
        TouchEffect(viewHolder, view);

        return viewHolder;
    }

    private CategoryAddViewHolder CreateAddHolder(final ViewGroup viewGroup) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_add_item_button, viewGroup, false);
        final CategoryAddViewHolder viewHolder = new CategoryAddViewHolder(view);

        SetAddClickListener(viewHolder);
        TouchEffect(viewHolder, view);

        return viewHolder;
    }

    private void SetClickListener(final DoListViewHolder viewHolder) {
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(viewHolder.getAdapterPosition(), localDataSet);
            }
        });
    }

    private void SetClickListener(final CategoryViewHolder viewHolder) {
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onCategoryClick(viewHolder.getAdapterPosition(), localDataSet);
            }
        });
    }

    private void SetAddClickListener(final CategoryAddViewHolder viewHolder) {
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onAddClick(viewHolder.getAdapterPosition());
            }
        });
    }

    private void TouchEffect(DoListViewHolder viewHolder, final View view) {
        viewHolder.button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0xe0864800, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    default: {
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void TouchEffect(CategoryViewHolder viewHolder, final View view) {
        viewHolder.button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0xe0144B66, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    default: {
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void TouchEffect(CategoryAddViewHolder viewHolder, final View view) {
        viewHolder.button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0xe0116E46, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    default: {
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
