package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private static final int EDIT_POST_REQUEST = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private TextInputEditText etPostContent;
    private Button btnPost;
    private ListView lvPosts;
    private Toolbar toolbar;

    private List<Post> postList;
    private PostAdapter postAdapter;
    private DatabaseHelper dbHelper;

    private String userName;
    private String avatarUrl;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

    // Flag để theo dõi trạng thái sắp xếp
    private boolean isDateAscending = false;
    private boolean isAuthorAscending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        etPostContent = findViewById(R.id.etPostContent);
        btnPost = findViewById(R.id.btnPost);
        lvPosts = findViewById(R.id.lvPosts);

        registerForContextMenu(lvPosts);

        Intent profileIntent = getIntent();
        if (profileIntent != null) {
            userName = profileIntent.getStringExtra("KEY_NAME");
            avatarUrl = profileIntent.getStringExtra("KEY_AVATAR_URL");
        }

        // Load posts from Database
        postList = dbHelper.getAllPosts();
        
        // If empty, add a welcome post
        if (postList.isEmpty()) {
            String currentDate = dateFormat.format(new Date());
            Post welcomePost = new Post("System", currentDate, "Welcome to the Home Page!", avatarUrl);
            dbHelper.addPost(welcomePost);
            postList = dbHelper.getAllPosts();
        }

        postAdapter = new PostAdapter(this, postList);
        lvPosts.setAdapter(postAdapter);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etPostContent.getText().toString().trim();

                if (content.isEmpty()) {
                    Toast.makeText(Home.this, "Please write something!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String currentDate = dateFormat.format(new Date());

                Post newPost = new Post(userName, currentDate, content, avatarUrl);
                long id = dbHelper.addPost(newPost);
                newPost.setId((int) id);
                
                postList.add(0, newPost);
                postAdapter.notifyDataSetChanged();
                etPostContent.setText("");

                lvPosts.smoothScrollToPosition(0);
                Toast.makeText(Home.this, "Posted successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Options Menu ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_profile) {
            Intent intent = new Intent(Home.this, Profile.class);
            intent.putExtra("KEY_NAME", userName);
            intent.putExtra("KEY_AVATAR_URL", avatarUrl);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_sort_author) {
            isAuthorAscending = !isAuthorAscending;
            Collections.sort(postList, (p1, p2) -> {
                int result = p1.getUserName().compareToIgnoreCase(p2.getUserName());
                return isAuthorAscending ? result : -result;
            });
            postAdapter.notifyDataSetChanged();
            String order = isAuthorAscending ? "A to Z" : "Z to A";
            Toast.makeText(this, "Sắp xếp tên tác giả: " + order, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_sort_date) {
            isDateAscending = !isDateAscending;
            Collections.sort(postList, (p1, p2) -> {
                try {
                    Date d1 = dateFormat.parse(p1.getDate());
                    Date d2 = dateFormat.parse(p2.getDate());
                    if (d1 == null || d2 == null) return 0;
                    return isDateAscending ? d1.compareTo(d2) : d2.compareTo(d1);
                } catch (ParseException e) {
                    return 0;
                }
            });
            postAdapter.notifyDataSetChanged();
            String order = isDateAscending ? "Cũ nhất" : "Mới nhất";
            Toast.makeText(this, "Sắp xếp bài đăng: " + order, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_friend_suggestion) {
            checkContactsPermission();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            startFriendSuggestionsActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFriendSuggestionsActivity();
            } else {
                Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startFriendSuggestionsActivity() {
        Intent intent = new Intent(this, FriendSuggestionsActivity.class);
        startActivity(intent);
    }

    // --- Context Menu ---
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.post_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        if (item.getItemId() == R.id.menu_edit) {
            Post postToEdit = postList.get(position);
            Intent intent = new Intent(Home.this, EditPostActivity.class);
            intent.putExtra("POST_CONTENT", postToEdit.getContent());
            intent.putExtra("POST_POSITION", position);
            startActivityForResult(intent, EDIT_POST_REQUEST);
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            Post postToDelete = postList.get(position);
            dbHelper.deletePost(postToDelete);
            postList.remove(position);
            postAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_POST_REQUEST && resultCode == RESULT_OK && data != null) {
            String updatedContent = data.getStringExtra("UPDATED_CONTENT");
            int position = data.getIntExtra("POST_POSITION", -1);

            if (position != -1 && updatedContent != null) {
                Post post = postList.get(position);
                post.setContent(updatedContent);
                dbHelper.updatePost(post);
                postAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}