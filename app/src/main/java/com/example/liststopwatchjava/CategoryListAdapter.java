package com.example.liststopwatchjava;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperListener{
    private CategoryData categoryData;
    private OnItemClickListener mOnItemClickListener;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ADD = 1;
    private final int TYPE_CATEGORY = 2;
    private final int TYPE_ITEM = 3;
    private boolean canDrag;

    public interface OnItemClickListener {
        public void onTitleClick();

        public void onSettingClick();

        public void onCategoryClick(int position);

        public void onToDoClick(int position);

        public void onAddClick(int position);

        public void onLongClick(Button button);

        public void onCategoryDelete(int _position);

        public void onToDoDelete(int _position);
    }

    public CategoryListAdapter(CategoryData _categoryData, OnItemClickListener onItemClickListener) {
        categoryData = _categoryData;
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
        if (viewHolder instanceof ToDoViewHolder) {
            ((ToDoViewHolder) viewHolder).button.setText(categoryData.loadToDo(position - 1).getName());
        }
        else if(viewHolder instanceof CategoryViewHolder) {
            ((CategoryViewHolder) viewHolder).button.setText(categoryData.loadCategory(position - 1).getName());
        }
        else if(viewHolder instanceof TitleViewHolder) {
            ((TitleViewHolder) viewHolder).textView.setText(categoryData.getName());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categoryData.itemSize() + 2;
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

    @Override
    public boolean canDrag()
    {
        return canDrag;
    }

    @Override
    public boolean onItemMove(int _from, int _to) {
        if(isPositionAdd(_to)) {
            categoryData.swapItem(_from - 1, _to - 2);
            notifyItemMoved(_from, _to - 1);
            return true;
        }
        else if(isPositionHeader(_to)) {
            categoryData.swapItem(_from - 1, _to);
            notifyItemMoved(_from, _to + 1);
            return true;
        }
        else {
            categoryData.swapItem(_from - 1, _to - 1);
            notifyItemMoved(_from, _to);
            return true;
        }
    }

    private boolean isPositionHeader(int _position) {
        return _position == 0;
    }

    private boolean isPositionAdd(int _position) {
        return _position == getItemCount() - 1;
    }

    private boolean isPositionCategory(int _position) {
        if(categoryData.isItemCategory(_position - 1))
            return true;
        return false;
    }

    private TitleViewHolder CreateHeaderHolder(final ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_title, viewGroup, false);
        final TitleViewHolder viewHolder = new TitleViewHolder(view);
        SetClickListener(viewHolder);

        return viewHolder;
    }

    private ToDoViewHolder CreateItemHolder(final ViewGroup viewGroup) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_todo, viewGroup, false);
        final ToDoViewHolder viewHolder = new ToDoViewHolder(view);

        SetClickListener(viewHolder);
        TouchEffect(viewHolder, view);

        return viewHolder;
    }

    private CategoryViewHolder CreateCategoryHolder(final ViewGroup viewGroup) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_category, viewGroup, false);
        final CategoryViewHolder viewHolder = new CategoryViewHolder(view);

        SetClickListener(viewHolder);
        TouchEffect(viewHolder, view);

        return viewHolder;
    }

    private AddItemViewHolder CreateAddHolder(final ViewGroup viewGroup) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_add_item_button, viewGroup, false);
        final AddItemViewHolder viewHolder = new AddItemViewHolder(view);

        SetAddClickListener(viewHolder);
        TouchEffect(viewHolder, view);

        return viewHolder;
    }

    private void SetClickListener(final TitleViewHolder viewHolder) {
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onTitleClick();
            }
        });

        viewHolder.button.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                canDrag = false;
                return false;
            }
        });

        viewHolder.textView.setSelected(true);

        viewHolder.setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onSettingClick();
            }
        });
    }

    private void SetClickListener(final ToDoViewHolder viewHolder) {
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onToDoClick(viewHolder.getAdapterPosition() - 1);
            }
        });
        viewHolder.button.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                canDrag = true;
                viewHolder.deleteButton.setEnabled(true);
                viewHolder.deleteButton.setVisibility(View.VISIBLE);
                mOnItemClickListener.onLongClick(viewHolder.deleteButton);
                return true;
            }
        });
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onToDoDelete(viewHolder.getAdapterPosition() - 1);
            }
        });
    }

    private void SetClickListener(final CategoryViewHolder viewHolder) {
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onCategoryClick(viewHolder.getAdapterPosition() - 1);
            }
        });
        viewHolder.button.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                canDrag = true;
                viewHolder.deleteButton.setEnabled(true);
                viewHolder.deleteButton.setVisibility(View.VISIBLE);
                mOnItemClickListener.onLongClick(viewHolder.deleteButton);
                return true;
            }
        });
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onCategoryDelete(viewHolder.getAdapterPosition() - 1);
            }
        });
    }

    private void SetAddClickListener(final AddItemViewHolder viewHolder) {
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onAddClick(viewHolder.getAdapterPosition() - 1);
            }
        });
        viewHolder.button.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                canDrag = false;
                return false;
            }
        });
    }

    private void TouchEffect(ToDoViewHolder viewHolder, final View view) {
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

    private void TouchEffect(AddItemViewHolder viewHolder, final View view) {
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
