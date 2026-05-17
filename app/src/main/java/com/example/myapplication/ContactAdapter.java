package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(@NonNull Context context, @NonNull List<Contact> contacts) {
        super(context, 0, contacts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contact contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvContactName);
        TextView tvPhone = convertView.findViewById(R.id.tvContactPhone);
        Button btnInvite = convertView.findViewById(R.id.btnInvite);

        if (contact != null) {
            tvName.setText(contact.getName());
            tvPhone.setText(contact.getPhoneNumber());
            btnInvite.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Đã mời " + contact.getName() + " tham gia ứng dụng", Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}