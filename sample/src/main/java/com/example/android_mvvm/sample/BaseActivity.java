package com.example.android_mvvm.sample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.adapters.ItemListActivity;
import com.example.android_mvvm.sample.adapters.MessageHelper;
import com.example.android_mvvm.sample.functional.DataLoadingActivity;
import com.example.android_mvvm.sample.two_way_binding.SearchActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private ViewDataBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        BindingAdapters.defaultBinder.bind(binding, createViewModel());
    }

    @Override
    protected void onDestroy() {
        BindingAdapters.defaultBinder.bind(binding, null);
        binding.executePendingBindings();
        super.onDestroy();
    }

    @NonNull
    public abstract ViewModel createViewModel();

    @LayoutRes
    public abstract int getLayoutId();


    // Common Dependencies

    @NonNull
    protected Navigator getNavigator() {
        return new Navigator() {
            @Override
            public void openDetailsPage(Item item) {
                navigate(ItemDetailsActivity.class);
            }

            @Override
            public void navigateToFunctionalDemo() {
                navigate(DataLoadingActivity.class);
            }

            @Override
            public void navigateToAdapterDemo() {
                navigate(ItemListActivity.class);
            }

            @Override
            public void navigateToTwoWayBindingDemo() {
                navigate(SearchActivity.class);
            }

            private void navigate(Class<?> destination) {
                Intent intent = new Intent(BaseActivity.this, destination);
                startActivity(intent);
            }
        };
    }

    @NonNull
    protected MessageHelper getMessageHelper() {
        return new MessageHelper() {
            @Override
            public void show(String message) {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

}
