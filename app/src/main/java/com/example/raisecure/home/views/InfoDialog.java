package com.example.raisecure.home.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.raisecure.R;
import com.example.raisecure.databinding.InfoDialogBinding;

public class InfoDialog {
    private InfoDialogBinding binding;
    private OkClickListener listener;
    private Context context;
    private Dialog dialog;

    public InfoDialog setListener(OkClickListener listener) {
        this.listener = listener;
        return this;
    }

    public InfoDialog(Context context) {
        this.context = context;
        setDialog();
    }

    private void setDialog() {
        dialog = new Dialog(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.info_dialog, null, false);
        dialog.setContentView(binding.getRoot());
        binding.infoDialogOk.setOnClickListener(v->{
            listener.ok();
            dialog.dismiss();
        });
        binding.infoDialogCancel.setOnClickListener(v ->dialog.dismiss());
    }

    public InfoDialog setDialogTitle(String dialogTitle){
        binding.infoDialogHeading.setText(dialogTitle);
        return this;
    }

    public InfoDialog setDialogInfo(String dialogInfo){
        binding.infoDialogDetails.setText(dialogInfo);
        return this;
    }

    public InfoDialog hideCancelButton(){
        binding.infoDialogCancel.setVisibility(View.GONE);
        return this;
    }

    public InfoDialog show(){
        dialog.show();
        return this;
    }

    public InfoDialog nonCancelableDialog(){
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return this;
    }

    public interface OkClickListener {
        void ok();
    }
}
