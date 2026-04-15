package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private TextInputEditText etPostContent;
    private Button btnPost;
    private ListView lvPosts;

    private List<Post> postList;
    private PostAdapter postAdapter;

    private String userName;
    private String avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        etPostContent = findViewById(R.id.etPostContent);
        btnPost = findViewById(R.id.btnPost);
        lvPosts = findViewById(R.id.lvPosts);

        Intent profileIntent = getIntent();
        if (profileIntent != null) {
            userName = profileIntent.getStringExtra("KEY_NAME");
            avatarUrl = profileIntent.getStringExtra("KEY_AVATAR_URL");
        }

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        lvPosts.setAdapter(postAdapter);
        String currentDate = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());

        postList.add(new Post("System", currentDate, "Welcome to the Home Page!", avatarUrl));
        postAdapter.notifyDataSetChanged();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etPostContent.getText().toString().trim();

                if (content.isEmpty()) {
                    Toast.makeText(Home.this, "Please write something!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String currentDate = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());

                Post newPost = new Post(userName, currentDate, content, avatarUrl);
                postList.add(0, newPost);
                postAdapter.notifyDataSetChanged();
                etPostContent.setText("");

                lvPosts.smoothScrollToPosition(0);
                Toast.makeText(Home.this, "Posted successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}