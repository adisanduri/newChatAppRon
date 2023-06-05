package com.example.hm2_chat_ron;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> messageList = new ArrayList<>();
    private String userId;

    public MessageAdapter(String userId) {
        try {
            this.userId = userId;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        messageList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Message m = new Message(
                                        document.get("userId").toString(),
                                        document.get("userName").toString(),
                                        document.get("userPhoto").toString(),
                                        document.get("message").toString());
                                messageList.add(m);
                            } catch (Exception e) {
                            }
                        }
                        notifyDataSetChanged();
                    }
                }
            });
            db.collection("chat").get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "addOnFailureListener: ", e);

                }
            });
            db.collection("chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    messageList = new ArrayList<>();
                    for (DocumentSnapshot document : value.getDocuments()) {
                        try {
                            Message m = new Message(
                                    document.get("userId").toString(),
                                    document.get("userName").toString(),
                                    document.get("userPhoto").toString(),
                                    document.get("message").toString());
                            messageList.add(m);
                        } catch (Exception e) {
                            Log.d(TAG, "onEvent: ", e);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "MessageAdapter: ", e);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message.userId.equals(userId)) {
            holder.messageCard_m.setVisibility(View.VISIBLE);
            holder.messageCard.setVisibility(View.GONE);
            holder.messageTextView_m.setText(message.message);
            holder.userNameTextView_m.setText(message.userName);
            Glide.with(holder.profileImageView_m.getContext()).load(message.userPhoto).into(holder.profileImageView_m);

        } else {
            holder.messageCard_m.setVisibility(View.GONE);
            holder.messageCard.setVisibility(View.VISIBLE);
            holder.messageTextView.setText(message.message);
            holder.userNameTextView.setText(message.userName);
            Glide.with(holder.profileImageView.getContext()).load(message.userPhoto).into(holder.profileImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(Message m) {
        Map<String, Object> message = new HashMap<>();
        message.put("userId", m.userId);
        message.put("userPhoto", m.userPhoto);
        message.put("userName", m.userName);
        message.put("message",m.message);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chat").document(String.valueOf(System.currentTimeMillis()))
                .set(message);
    }
}
