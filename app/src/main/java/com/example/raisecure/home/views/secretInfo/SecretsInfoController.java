package com.example.raisecure.home.views.secretInfo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.raisecure.R;
import com.example.raisecure.databinding.SecretsInfoControllerBinding;
import com.example.raisecure.home.base.BaseController;
import com.example.raisecure.home.data.local.SecretRepo;

public class SecretsInfoController extends BaseController implements View.OnClickListener {
    private SecretsInfoControllerBinding binding;
    private long secretId;
    private SecretRepo secretRepo;

    protected SecretsInfoController(@Nullable Bundle args) {
        super(args);
    }

    public SecretsInfoController() {
    }

    public SecretsInfoController(long secretId) {
        this.secretId = secretId;
    }

    @Override
    protected int layoutRes() {
        return R.layout.secrets_info_controller;
    }

    @Override
    protected void onViewBound(View view) {
        binding = (SecretsInfoControllerBinding) dataBinding;
        secretRepo = new SecretRepo();
        setListener();
    }

    private void setListener() {
        binding.secretsInfoBack.setOnClickListener(this);
        binding.secretsInfoDelete.setOnClickListener(this);
        binding.secretsInfoSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.secrets_info_back:
                getRouter().handleBack();
                break;

            case R.id.secrets_info_delete:
                secretRepo.deleteSecrets(secretId);
                getRouter().handleBack();
                break;

            case R.id.secrets_info_save:
                secretRepo.updateItem(secretId, binding.secretsInfoHeadingText.getText().toString(), binding.secretsTextInfo.getText().toString());
                getRouter().handleBack();
                break;
        }
    }
}
