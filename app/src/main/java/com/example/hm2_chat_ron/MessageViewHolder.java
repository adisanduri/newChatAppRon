package com.example.hm2_chat_ron;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public CardView messageCard;
    ImageView profileImageView;
    TextView userNameTextView;
    TextView messageTextView;
    ImageView profileImageView_m;
    TextView userNameTextView_m;
    TextView messageTextView_m;
    public CardView messageCard_m;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        messageCard = itemView.findViewById(R.id.messageCard);
        profileImageView = itemView.findViewById(R.id.profileImageView_message);
        userNameTextView = itemView.findViewById(R.id.userNameTextView_message);
        messageTextView = itemView.findViewById(R.id.messageTextView);

        messageCard_m =  itemView.findViewById(R.id.messageCard_m);
        profileImageView_m = itemView.findViewById(R.id.profileImageView_message_m);
        userNameTextView_m = itemView.findViewById(R.id.userNameTextView_message_m);
        messageTextView_m = itemView.findViewById(R.id.messageTextView_m);

    }
}
