package com.example.raisecure.home.views.changePin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.raisecure.R;
import com.example.raisecure.databinding.PinChangeControllerBinding;
import com.example.raisecure.home.base.BaseController;
import com.example.raisecure.home.views.utils.Algorithm;
import com.example.raisecure.home.views.InfoDialog;

public class PinChangeController extends BaseController implements View.OnClickListener, InfoDialog.OkClickListener {
    private boolean confirmPin;
    private String confirmPinText;
    private PinChangeControllerBinding binding;

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
        setListener();
    }

    private void setListener() {
        binding.pinBack.setOnClickListener(this);
        binding.pinChangeNext.setOnClickListener(this);
        binding.pinPasswordEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.pinPasswordField.setError(null);
                binding.pinPasswordField.setErrorEnabled(false);
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
        }
    }

    private void pinConfirmSet() {
        String confirmPinAgain = binding.pinPasswordEdit.getText().toString();
        if (confirmPinText.equals(confirmPinAgain)) {
            saveCredential(confirmPinAgain);
            setUpSuccessDialog();
        } else {
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
        if (Algorithm.checkPinRequirement(pin)) {
            binding.pinInfo.setText(getActivity().getString(R.string.pin_confirm_error));
            binding.pinPasswordField.setHint(getActivity().getString(R.string.pin_confirm));
            binding.pinChangeNext.setImageResource(R.drawable.ic_check);
            confirmPin = true;
            confirmPinText = pin;
        } else {
            binding.pinPasswordField.setErrorEnabled(true);
            binding.pinPasswordField.setError((getActivity()).getString(R.string.pin_edit_error));
        }
    }

    @Override
    public void ok() {
        getRouter().handleBack();
    }
}
