package com.example.liststopwatchjava;

public interface ItemTouchHelperListener {
    boolean onItemMove(int _from, int _to);
    boolean canDrag();
}
