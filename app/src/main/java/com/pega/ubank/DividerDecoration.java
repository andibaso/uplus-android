package com.pega.ubank;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v13.view.ViewCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.pega.ubank.data.HeaderModel;

public class DividerDecoration extends DividerItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable divider;

    public DividerDecoration(Context context, int orientation) {
        super(context, orientation);
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        canvas.save();
        int left;
        int right;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            if (parent.getChildViewHolder(child) instanceof HeaderModel.HeaderHolder) {
                continue;
            }

            Rect bounds = new Rect();
            parent.getDecoratedBoundsWithMargins(child, bounds);
            final int bottom = bounds.bottom + Math.round(ViewCompat.getTranslationY(child));
            final int top = bottom - divider.getIntrinsicHeight();

            left = child.getLeft();
            right = child.getRight();
            DrawableCompat.setTintMode(divider, PorterDuff.Mode.SRC);
            DrawableCompat.setTint(divider, Color.WHITE);
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
            left = child.getLeft() + child.getPaddingLeft();
            right = child.getRight() - child.getPaddingRight();
            DrawableCompat.setTint(divider, Color.parseColor("#F2F2F2"));
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //empty to prevent default behaviour
    }
}
