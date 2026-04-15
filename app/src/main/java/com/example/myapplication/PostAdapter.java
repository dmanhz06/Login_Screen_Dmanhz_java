package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(@NonNull Context context, @NonNull List<Post> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
        }

        Post currentPost = getItem(position);

        ImageView imgAvatar = convertView.findViewById(R.id.imgAvatar);
        TextView tvUserName = convertView.findViewById(R.id.tvUserName);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvContent = convertView.findViewById(R.id.tvContent);
        TextView tvShowMore = convertView.findViewById(R.id.tvShowMore);

        if (currentPost != null) {
            tvUserName.setText(currentPost.getUserName());
            tvDate.setText(currentPost.getDate());
            tvContent.setText(currentPost.getContent());

            Glide.with(getContext())
                    .load(currentPost.getAvatarUrl())
                    .circleCrop()
                    .into(imgAvatar);

            tvContent.setMaxLines(3);
            tvShowMore.setText("Show more...");

            // 👇 luôn set click ở ngoài (QUAN TRỌNG)
            tvShowMore.setOnClickListener(v -> {
                if (tvShowMore.getText().toString().equals("Show more...")) {
                    tvContent.setMaxLines(Integer.MAX_VALUE);
                    tvShowMore.setText("Show less");
                } else {
                    tvContent.setMaxLines(3);
                    tvShowMore.setText("Show more...");
                }
            });

            tvContent.post(() -> {
                if (tvContent.getLayout() != null && tvContent.getLayout().getLineCount() > 3) {
                    tvShowMore.setVisibility(View.VISIBLE);
                } else {
                    tvShowMore.setVisibility(View.GONE);
                }
            });
        }

        return convertView;
    }
}