package com.example.raisecure.home.views.changePin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.raisecure.R;
import com.example.raisecure.databinding.PinChangeControllerBinding;
import com.example.raisecure.home.base.BaseController;
import com.example.raisecure.home.data.remote.RestKey;
import com.example.raisecure.home.networking.NetworkRequest;
import com.example.raisecure.home.views.Const;
import com.example.raisecure.home.views.InfoDialog;
import com.example.raisecure.home.views.utils.Algorithm;
import com.example.raisecure.home.views.utils.EncryptionUtils;
import com.example.raisecure.home.views.utils.RaiSaveData;

public class PinChangeController extends BaseController implements View.OnClickListener, InfoDialog.OkClickListener {
    private boolean confirmPin;
    private String confirmPinText;
    private String userName;
    private String password;
    private PinChangeControllerBinding binding;
    private RaiSaveData raiSaveData;
    public PinChangeController() {
    }

    protected PinChangeController(@Nullable Bundle args) {
        super(args);
    }

    @Override
    protected int layoutRes() {
        return R.layout.pin_change_controller;
    }

    @Override
    protected void onViewBound(View view) {
        binding = (PinChangeControllerBinding) dataBinding;
        raiSaveData = new RaiSaveData(getActivity());
        setListener();
    }

    private void setListener() {
        binding.pinBack.setOnClickListener(this);
        binding.pinChangeNext.setOnClickListener(this);
        binding.pinLoginButton.setOnClickListener(this);
        binding.pinPasswordEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.pinPasswordField.setError(null);
                binding.pinPasswordField.setErrorEnabled(false);
            }
        });
        binding.pinPasswordTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.pinPassword.setErrorEnabled(false);
                binding.pinPassword.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.pinPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.pinPassword.setErrorEnabled(false);
                binding.pinPassword.setError("");
            }
        });

        binding.pinUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.pinUsername.setErrorEnabled(false);
                binding.pinUsername.setError("");
            }
        });
        binding.pinUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.pinUsername.setErrorEnabled(false);
                binding.pinUsername.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        TextWatcher PasswordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.pinPasswordField.setError(null);
                binding.pinPasswordField.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        binding.pinPasswordEdit.addTextChangedListener(PasswordTextWatcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pin_back:
                getRouter().handleBack();
                break;
            case R.id.pin_change_next:
                if (!confirmPin) {
                    pinNext();
                } else {
                    pinConfirmSet();
                }
                break;

            case R.id.pin_login_button:
                checkLoginCredential();
                break;
        }
    }

    private void checkLoginCredential() {
        userName = binding.pinUsernameField.getText().toString();
        password = binding.pinPasswordTextField.getText().toString();
        if (userName.isEmpty() || !userName.equals(Const.USER_NAME)) {
            binding.pinUsername.setErrorEnabled(true);
            binding.pinUsername.setError(getActivity().getString(R.string.username_error));
            binding.pinUsername.clearFocus();
            return;
        }
        if (password.isEmpty() || !password.equals(Const.PASSWORD)) {
            binding.pinPassword.setErrorEnabled(true);
            binding.pinPassword.setError(getActivity().getString(R.string.password_error));
            binding.pinPassword.clearFocus();
            return;
        }
        raiSaveData.removeData(Const.USER_PIN_EXIST);
        raiSaveData.removeData(Const.USER_PIN);
        raiSaveData.removeData(Const.USER_EXIST);
        binding.pinUsernameField.setText("");
        binding.pinPasswordTextField.setText("");
        getEncryptionKey();
    }

    @SuppressLint("CheckResult")
    private void getEncryptionKey() {
        NetworkRequest networkRequest = new NetworkRequest();
        networkRequest.getApiKey().subscribe(this::saveApiKey, this::errorResponse);
    }

    private void saveApiKey(RestKey restKey) {
        raiSaveData.saveStringData(Const.SERVER_API_KEY, EncryptionUtils.encrypt(getActivity(), restKey.getKey()));
        raiSaveData.saveBooleanData(Const.USER_EXIST, true);
        getActivity().runOnUiThread(this::setUpPin);
    }

    private void setUpPin() {
        binding.pinPasswordField.setVisibility(View.VISIBLE);
        binding.pinInfo.setVisibility(View.VISIBLE);
        binding.pinPassword.setVisibility(View.GONE);
        binding.pinUsername.setVisibility(View.GONE);
        binding.pinChangeNext.setVisibility(View.VISIBLE);
        binding.pinLoginButton.setVisibility(View.GONE);
    }

    private void errorResponse(Throwable throwable) {
    }
    private void pinConfirmSet() {
        String confirmPinAgain = binding.pinPasswordEdit.getText().toString();
        binding.pinPasswordEdit.setText("");
        if (confirmPinText.equals(confirmPinAgain)) {
            saveCredential(confirmPinAgain);
            raiSaveData.saveBooleanData(Const.USER_PIN_EXIST, true);
            setUpSuccessDialog();
        } else {
            binding.pinPasswordEdit.clearFocus();
            binding.pinPasswordField.setErrorEnabled(true);
            binding.pinPasswordField.setError((getActivity()).getString(R.string.pin_confirm_error));
        }
    }

    private void setUpSuccessDialog() {
        new InfoDialog(getActivity())
                .setListener(this)
                .nonCancelableDialog()
                .setDialogTitle(getActivity().getString(R.string.pin_set_success))
                .setDialogInfo(getActivity().getString(R.string.pin_set_success_info))
                .hideCancelButton().show();
    }

    private void saveCredential(String confirmPinAgain) {

    }

    private void pinNext() {
        String pin = binding.pinPasswordEdit.getText().toString();
        //if(binding.pinPasswordField.is)
        binding.pinPasswordEdit.setText("");
        if (Algorithm.checkPinRequirement(pin)) {
            binding.pinInfo.setText(getActivity().getString(R.string.pin_confirm_error));
            binding.pinPasswordField.setHint(getActivity().getString(R.string.pin_confirm));
            binding.pinChangeNext.setImageResource(R.drawable.ic_check);
            confirmPin = true;
            confirmPinText = pin;
        } else {
            binding.pinPasswordEdit.clearFocus();
            binding.pinPasswordField.setErrorEnabled(true);
            binding.pinPasswordField.setError((getActivity()).getString(R.string.pin_edit_error));
        }
    }

    @Override
    public void ok() {
        getRouter().handleBack();
    }
}
