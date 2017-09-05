package com.mitkoindo.smartcollection.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by W8 on 05/09/2017.
 */

public class RecyclerViewHelper
{
    public static boolean isLastItemDisplaying(RecyclerView recyclerView)
    {
        if (recyclerView.getAdapter().getItemCount() != 0)
        {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }
}
