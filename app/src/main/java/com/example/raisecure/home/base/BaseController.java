package com.example.raisecure.home.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bluelinelabs.conductor.Controller;

public abstract class BaseController extends Controller {
    protected ViewDataBinding dataBinding;

    protected BaseController(@Nullable Bundle args) {
        super(args);
    }

    public BaseController() {
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        dataBinding = DataBindingUtil.inflate(inflater, layoutRes(), container, false);
        onViewBound(dataBinding.getRoot());
        return dataBinding.getRoot();
    }

    @LayoutRes
    protected abstract int layoutRes();

    protected abstract void onViewBound(View view);

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        if (dataBinding != null) {
            dataBinding.unbind();
            dataBinding = null;
        }
    }
}
