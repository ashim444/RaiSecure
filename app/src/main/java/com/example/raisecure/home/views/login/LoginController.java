package com.example.raisecure.home.views.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.example.raisecure.R;
import com.example.raisecure.databinding.LoginControllerBinding;
import com.example.raisecure.home.MainActivity;
import com.example.raisecure.home.base.BaseController;
import com.example.raisecure.home.data.remote.RestKey;
import com.example.raisecure.home.views.Const;
import com.example.raisecure.home.views.secrets.SecretController;
import com.example.raisecure.home.views.utils.Algorithm;
import com.example.raisecure.home.views.utils.EncryptionUtils;
import com.example.raisecure.home.views.utils.RaiSaveData;

public class LoginController extends BaseController implements View.OnClickListener {

    private LoginControllerBinding binding;
    private boolean initialPinSet;
    private String confirmPinText;
    private String userName;
    private String password;
    private RaiSaveData raiSaveData;
    private boolean loginPinView;
    private boolean forgetPin;

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
        raiSaveData = new RaiSaveData(getActivity());
        if (raiSaveData.getSavedBooleanData(Const.USER_EXIST) && raiSaveData.getSavedBooleanData(Const.USER_PIN_EXIST)) {
            loginPinView = true;
            showPinLoginView();
            ((MainActivity) getActivity()).initDatabase(true);
        }
    }

    private void setListeners() {
        binding.loginLoginButton.setOnClickListener(this);
        binding.loginChangeNext.setOnClickListener(this);
        binding.loginForgetPin.setOnClickListener(this);
        binding.loginBack.setOnClickListener(this);
        binding.loginPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.loginPassword.setErrorEnabled(false);
                binding.loginPassword.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.loginPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.loginPassword.setErrorEnabled(false);
                binding.loginPassword.setError("");
            }
        });

        binding.loginUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.loginUsername.setErrorEnabled(false);
                binding.loginUsername.setError("");
            }
        });
        binding.loginUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.loginUsername.setErrorEnabled(false);
                binding.loginUsername.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.loginPinEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.loginPinField.setErrorEnabled(false);
                binding.loginPinField.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.loginPinField.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.loginPinField.setErrorEnabled(false);
                binding.loginPinField.setError("");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login_button:
                checkLoginCredential();
                break;
            case R.id.login_change_next:
                if (loginPinView) {
                    checkUserPin();
                } else {
                    if (!initialPinSet) {
                        pinNext();
                    } else {
                        pinConfirmSet();
                    }
                }
                break;
            case R.id.login_forget_pin:
                forgetPin = true;
                setUpPinView(false);
                binding.loginBack.setVisibility(View.VISIBLE);
                binding.loginForgetPin.setVisibility(View.GONE);
                break;

            case R.id.login_back:
                showPinLoginView();
                binding.loginBack.setVisibility(View.GONE);
                break;
        }
    }

    private void checkUserPin() {
        String userPin = binding.loginPinEdit.getText().toString();
        String actualUserPin = EncryptionUtils.decrypt(getActivity(), raiSaveData.getSavedStringData(Const.USER_PIN));
        if (userPin.isEmpty() || !userPin.equals(actualUserPin)) {
            binding.loginPinField.setErrorEnabled(true);
            binding.loginPinEdit.setText("");
            binding.loginPinField.clearFocus();
            binding.loginPinField.setError((getActivity()).getString(R.string.pin_notmatch));
            binding.loginForgetPin.setVisibility(View.VISIBLE);
        } else {
            navigateToSecrets();
        }
    }

    private void pinConfirmSet() {
        String confirmPinAgain = binding.loginPinEdit.getText().toString();
        binding.loginPinEdit.setText("");
        if (confirmPinText.equals(confirmPinAgain)) {
            saveCredential(confirmPinAgain);
            raiSaveData.saveBooleanData(Const.USER_PIN_EXIST, true);
            navigateToSecrets();
        } else {
            binding.loginPinEdit.setText("");
            binding.loginPinField.clearFocus();
            binding.loginPinField.setErrorEnabled(true);
            binding.loginPinField.setError((getActivity()).getString(R.string.pin_confirm_error));
        }
    }

    private void saveCredential(String pin) {
        raiSaveData.saveStringData(Const.USER_PIN, EncryptionUtils.encrypt(getActivity(), pin));
    }

    private void pinNext() {
        String pin = binding.loginPinEdit.getText().toString();
        binding.loginPinEdit.setText("");
        if (Algorithm.checkPinRequirement(pin)) {
            binding.loginPinInfo.setText(getActivity().getString(R.string.pin_confirm_error));
            binding.loginPinField.setHint(getActivity().getString(R.string.pin_confirm));
            binding.loginChangeNext.setImageResource(R.drawable.ic_check);
            initialPinSet = true;
            confirmPinText = pin;
        } else {
            binding.loginPinEdit.setText("");
            binding.loginPinField.clearFocus();
            binding.loginPinField.setErrorEnabled(true);
            binding.loginPinField.setError((getActivity()).getString(R.string.pin_edit_error));
        }
    }

    private void checkLoginCredential() {
        userName = binding.loginUsernameField.getText().toString();
        password = binding.loginPasswordField.getText().toString();
        if (userName.isEmpty() || !userName.equals(Const.USER_NAME)) {
            binding.loginUsername.setErrorEnabled(true);
            binding.loginUsername.setError(getActivity().getString(R.string.username_error));
            binding.loginPassword.clearFocus();
            return;
        }
        if (password.isEmpty() || !password.equals(Const.PASSWORD)) {
            binding.loginPassword.setErrorEnabled(true);
            binding.loginPassword.setError(getActivity().getString(R.string.password_error));
            binding.loginPassword.clearFocus();
            return;
        }
        if (forgetPin) {
            loginPinView = false;
            raiSaveData.removeData(Const.USER_PIN_EXIST);
            raiSaveData.removeData(Const.USER_PIN);
            raiSaveData.removeData(Const.USER_EXIST);
        }
        binding.loginUsernameField.setText("");
        binding.loginPasswordField.setText("");
        getEncryptionKey();
    }

    @SuppressLint("CheckResult")
    private void getEncryptionKey() {
       saveApiKey(new RestKey("34eGe1Shti74g#gsy#"));
//        NetworkRequest networkRequest = new NetworkRequest();
//        networkRequest.getApiKey().subscribe(this::saveApiKey, this::errorResponse);
    }

    private void saveApiKey(RestKey restKey) {
        raiSaveData.saveStringData(Const.SERVER_API_KEY, EncryptionUtils.encrypt(getActivity(), restKey.getKey()));
        ((MainActivity) getActivity()).initDatabase(false);
        raiSaveData.saveBooleanData(Const.USER_EXIST, true);
//        getActivity().runOnUiThread(this::setUpPin);
        setUpPin();
    }

    private void errorResponse(Throwable throwable) {
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show());
    }

    private void setUpPin() {
        setUpPinView(true);
    }

    private void setUpPinView(boolean show) {
        binding.loginLoginButton.setVisibility(show ? View.GONE : View.VISIBLE);
        binding.loginPassword.setVisibility(show ? View.GONE : View.VISIBLE);
        binding.loginUsername.setVisibility(show ? View.GONE : View.VISIBLE);
        binding.loginChangeNext.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.loginPinField.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.loginPinInfo.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.loginCreateAccount.setText(show ? getActivity().getString(R.string.pin_set) : getActivity().getString(R.string.login));
    }

    private void navigateToSecrets() {
        getRouter().setRoot(RouterTransaction.with(new SecretController()).pushChangeHandler(new HorizontalChangeHandler())
                .popChangeHandler(new HorizontalChangeHandler()));
    }

    private void showPinLoginView() {
        binding.loginLoginButton.setVisibility(View.GONE);
        binding.loginPassword.setVisibility(View.GONE);
        binding.loginUsername.setVisibility(View.GONE);
        binding.loginChangeNext.setVisibility(View.VISIBLE);
        binding.loginPinField.setVisibility(View.VISIBLE);
        binding.loginPinInfo.setVisibility(View.VISIBLE);
        binding.loginCreateAccount.setText(getActivity().getString(R.string.enter_pin));
    }
}
