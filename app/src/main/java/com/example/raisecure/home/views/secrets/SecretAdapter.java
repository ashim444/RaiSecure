package com.example.raisecure.home.views.secrets;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.raisecure.R;
import com.example.raisecure.databinding.SecretsItemBinding;
import com.example.raisecure.home.model.entity.Secret;

import java.util.List;

public class SecretAdapter extends RecyclerView.Adapter<SecretAdapter.ViewHolder> {

    private List<Secret> secretList;
    private onSecretItemClickListener listener;

    public SecretAdapter(List<Secret> secretList, onSecretItemClickListener listener) {
        this.secretList = secretList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SecretsItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.secrets_item, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(secretList.get(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(v -> listener.onSecretItemClick(secretList.get(holder.getAdapterPosition()).getSecretId()));
    }

    @Override
    public int getItemCount() {
        return secretList.size();
    }

    public interface onSecretItemClickListener {
        void onSecretItemClick(long secretId);

        void onSecretItemDelete(long secretId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final SecretsItemBinding itemBinding;

        public ViewHolder(SecretsItemBinding itemBinding) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(Secret secret) {
            itemBinding.secretsItemText.setText(secret.getSecretTitle());
            itemBinding.secretsItemDelete.setOnClickListener(v -> listener.onSecretItemDelete(secret.getSecretId()));
        }
    }
}
