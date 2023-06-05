package com.example.hm2_chat_ron;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    GoogleSignInAccount account;
    RequestQueue mRequestQue;

    private RecyclerView messageRV;
    private MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                account = (GoogleSignInAccount) extras.get("user");
            }

            TextView userNameTextView = findViewById(R.id.userNameTextViewPanel);
            userNameTextView.setText("Hi, " + account.getGivenName());

            ImageView userImage = findViewById(R.id.profileImageViewPanel);
            Glide.with(this).load(account.getPhotoUrl()).into(userImage);

            messageAdapter = new MessageAdapter(account.getId());

            // Set up the RecyclerView
            messageRV = findViewById(R.id.messageRecyclerView);
            messageRV.setHasFixedSize(false);
            messageRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

            messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    messageRV.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                }
            });

            messageRV.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    if (messageAdapter.getItemCount()>0) {
                        messageRV.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                    }
                }
            });

            messageRV.setAdapter(messageAdapter);

            FloatingActionButton btn = findViewById(R.id.addMessageBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText text = findViewById(R.id.messageET);
                    Message m = new Message(account.getId(),account.getDisplayName(),account.getPhotoUrl().toString(),text.getText().toString());
                    messageAdapter.addMessage(m);
                    text.setText("");
                    sendCloudMessage(m);
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "onCreate: ", e);
        }
    }

    private void sendCloudMessage(Message m) {
        String serverKey = "AAAAi_Y2zOk:APA91bEU1Qv2PZfEBuL4SG35MiZaAV-q3UBPkcKMoCJgqg9h4Ak3NnEJBSNXnTNFCQCxqaCz36cHxnnGJL0RqrtR55Qzug4155Aaj2r7wErHzf0pkl2i3wj0U0-UxQndPgaNb6IdQzPT";
        String topic = "chat";

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + topic);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", m.userName);
            notificationObj.put("body", m.message);
            json.put("notification", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    response -> Log.d("MUR", "onResponse: " + response.toString()),
                    error -> Log.d("MUR", "onError: " + error.networkResponse)
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + serverKey);
                    return header;
                }
            };

            mRequestQue.add(request);

        } catch (Exception e) {

        }
    }
}
