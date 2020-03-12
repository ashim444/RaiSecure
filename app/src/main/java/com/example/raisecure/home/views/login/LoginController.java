package com.example.raisecure.home.views.login;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.example.raisecure.R;
import com.example.raisecure.databinding.LoginControllerBinding;
import com.example.raisecure.home.base.BaseController;
import com.example.raisecure.home.views.secrets.SecretController;

public class LoginController extends BaseController implements View.OnClickListener {

    private LoginControllerBinding binding;

    public LoginController() {
    }

    protected LoginController(@Nullable Bundle args) {
        super(args);
    }

    @Override
    protected int layoutRes() {
        return R.layout.login_controller;
    }

    @Override
    protected void onViewBound(View view) {
        binding = (LoginControllerBinding) dataBinding;
        setListeners();
    }

    private void setListeners() {
        binding.loginLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login_button:
                navigateToSecrets();
        }
    }

    private void navigateToSecrets() {
        getRouter().pushController(RouterTransaction.with(new SecretController()).pushChangeHandler(new HorizontalChangeHandler())
                .popChangeHandler(new HorizontalChangeHandler()));
    }
}
