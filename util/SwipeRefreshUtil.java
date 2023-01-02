package com.voltup.customer.util;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.voltup.customer.R;

public class SwipeRefreshUtil {

    public static SwipeRefreshLayout setUp(SwipeRefreshLayout swipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener listener) {
        //SwipeRefreshLayout swipeRefreshLayout = view.findViewById(id);
        setColors(swipeRefreshLayout);
        setListener(listener, swipeRefreshLayout);
        return swipeRefreshLayout;
    }

    private static void setListener(final SwipeRefreshLayout.OnRefreshListener listener, final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    private static void setColors(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.light_green);
    }
}
