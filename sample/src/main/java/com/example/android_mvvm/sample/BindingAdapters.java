package com.example.android_mvvm.sample;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.adapters.ViewModelBinder;
import com.example.android_mvvm.adapters.ViewProvider;
import com.example.android_mvvm.utils.BindingUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;

@SuppressWarnings("unused")
public class BindingAdapters {

    @NonNull
    public static final ViewModelBinder defaultBinder = new ViewModelBinder() {
        @Override
        public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
            viewDataBinding.setVariable(BR.vm, viewModel);
        }
    };

    @BindingAdapter("android:visibility")
    public static void bindVisibility(@NonNull View view, @Nullable Boolean visible) {
        int visibility = (visible != null && visible) ? View.VISIBLE : View.GONE;
        view.setVisibility(visibility);
    }


    /**
     * Binding Adapter Wrapper for checking memory leak
     */
    @BindingAdapter({"items", "view_provider"})
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, Observable<List<ViewModel>> items, ViewProvider viewProvider) {
        RecyclerView.Adapter previousAdapter = recyclerView.getAdapter();
        BindingUtils.bindAdapterWithDefaultBinder(recyclerView, items, viewProvider);

        // Previous adapter should get deallocated
        if (previousAdapter != null)
            ExampleApplication.getRefWatcher(recyclerView.getContext()).watch(previousAdapter);
    }

    /**
     * Binding Adapter Wrapper for checking memory leak
     */
    @BindingAdapter({"items", "view_provider"})
    public static void bindViewPagerAdapter(ViewPager viewPager, Observable<List<ViewModel>> items, ViewProvider viewProvider) {
        PagerAdapter previousAdapter = viewPager.getAdapter();
        BindingUtils.bindAdapterWithDefaultBinder(viewPager, items, viewProvider);

        // Previous adapter should get deallocated
        if (previousAdapter != null)
            ExampleApplication.getRefWatcher(viewPager.getContext()).watch(previousAdapter);
    }

    @BindingConversion
    public static View.OnClickListener toOnClickListener(final Action0 listener) {
        if (listener != null) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.call();
                }
            };
        } else {
            return null;
        }
    }
}
