package com.example.raisecure.home.views.secrets;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.example.raisecure.R;
import com.example.raisecure.databinding.SecretsControllerBinding;
import com.example.raisecure.home.base.BaseController;
import com.example.raisecure.home.data.local.SecretRepo;
import com.example.raisecure.home.model.entity.Secret;
import com.example.raisecure.home.views.InfoDialog;
import com.example.raisecure.home.views.changePin.PinChangeController;
import com.example.raisecure.home.views.secretInfo.SecretsInfoController;

import java.util.List;

public class SecretController extends BaseController implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, SecretAdapter.onSecretItemClickListener, InfoDialog.OkClickListener {
    private SecretsControllerBinding binding;
    private SecretRepo secretRepo;
    private long secretId;
    private int position;
    private SecretAdapter adapter;

    public SecretController() {
    }

    protected SecretController(@Nullable Bundle args) {
        super(args);
    }

    @Override
    protected int layoutRes() {
        return R.layout.secrets_controller;
    }

    @Override
    protected void onViewBound(View view) {
        binding = (SecretsControllerBinding) dataBinding;
        secretRepo = new SecretRepo();
        binding.secretsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.secretsList.setNestedScrollingEnabled(false);
        adapter = new SecretAdapter(this);
        binding.secretsList.setAdapter(adapter);
        setListeners();
        setUpRecycler();
    }

    private void setUpRecycler() {
        adapter.setData(getSecretList());
    }

    private List<Secret> getSecretList() {
        return secretRepo.getAllItem();
    }


    private void setListeners() {
        binding.secretsAdd.setOnClickListener(this);
        binding.secretsMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.secrets_add:
                long secretId = secretRepo.createNewSecret();
                getRouter().pushController(RouterTransaction.with(new SecretsInfoController(secretId))
                        .pushChangeHandler(new HorizontalChangeHandler())
                        .popChangeHandler(new HorizontalChangeHandler()));
                break;
            case R.id.secrets_more:
                showPopupMenu(v);
                break;
        }
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu_secrets);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_secrets_change_pin:
                getRouter().pushController(RouterTransaction.with(new PinChangeController()).pushChangeHandler(new HorizontalChangeHandler()).popChangeHandler(new HorizontalChangeHandler()));
                return true;
            case R.id.menu_secrets_logout:
                return true;
            default:
                return false;
        }
    }


    @Override
    public void onSecretItemClick(long secretId) {
        getRouter().pushController(RouterTransaction.with(new SecretsInfoController(secretId)).popChangeHandler(new HorizontalChangeHandler()).popChangeHandler(new HorizontalChangeHandler()));
    }

    @Override
    public void onSecretItemDelete(long secretId, int position) {
        this.secretId = secretId;
        this.position = position;
        new InfoDialog(getActivity())
                .setListener(this)
                .setDialogTitle(getActivity().getString(R.string.delete))
                .setDialogInfo(getActivity().getString(R.string.delete_info))
                .hideCancelButton()
                .show();
    }

    @Override
    public void ok() {
        secretRepo.deleteSecrets(secretId);
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
        setUpRecycler();
    }
}
