package com.example.test_cont;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverID,MessageReceiverName, messageSenderID, messageReceiverImage;

    private TextView custom_profile_name;
    private CircleImageView userImage;

    private Toolbar ChatToolBar;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private Button back;

    private ImageButton send_message;
    private EditText input_message;

    private final List<Mensajes> mensajesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter mensajeAdapter;
    private RecyclerView userMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth=FirebaseAuth.getInstance();
        messageSenderID=mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        messageReceiverID=getIntent().getExtras().get("visit_user_id").toString();
        MessageReceiverName=getIntent().getExtras().get("visit_user_name").toString();
        messageReceiverImage=getIntent().getExtras().get("visit_image").toString();


        IntializeControllers();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, ChatsFragment.class));
            }
        });

        custom_profile_name.setText(MessageReceiverName);
        Picasso.get().load(messageReceiverImage).placeholder(R.drawable.ic_account).into(userImage);



        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });
    }

    private void IntializeControllers(){


        ChatToolBar=(Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(ChatToolBar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView =layoutInflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionBarView);

        custom_profile_name=(TextView) findViewById(R.id.custom_profile_name);
        userImage = (CircleImageView) findViewById(R.id.profile_image);
        back = (Button) findViewById(R.id.back);
        send_message=(ImageButton) findViewById(R.id.send_message);
        input_message=(EditText) findViewById(R.id.input_message);


        mensajeAdapter = new MessageAdapter(mensajesList);
        userMessageList=(RecyclerView) findViewById(R.id.private_message_list);
        linearLayoutManager= new LinearLayoutManager(this);
        userMessageList.setLayoutManager(linearLayoutManager);
        userMessageList.setAdapter(mensajeAdapter);


    }



    @Override
    protected void onStart() {
        super.onStart();

        rootRef.child("Mensajes").child(messageSenderID).child(messageReceiverID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mensajes mensajes=dataSnapshot.getValue(Mensajes.class);

                mensajesList.add(mensajes);

                mensajeAdapter.notifyDataSetChanged();
                userMessageList.smoothScrollToPosition(userMessageList.getAdapter().getItemCount());

                userMessageList.smoothScrollToPosition(userMessageList.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendMessage(){
        String messageText=input_message.getText().toString();

        if(TextUtils.isEmpty(messageText)){
            Toast.makeText(this, "Primero escribe tu mensaje", Toast.LENGTH_SHORT).show();
        }
        else{
            String messageSenderRef = "Mensajes/" + messageSenderID + "/" + messageReceiverID;
            String messageReceiverRef = "Mensajes/" + messageReceiverID + "/" + messageSenderID;

            DatabaseReference userMessageKeyRef = rootRef.child("Mensajes")
                    .child(messageSenderID).child(messageReceiverID).push();

            String messagePushID = userMessageKeyRef.getKey();

            Map messageTextBody=new HashMap<>();
            messageTextBody.put("mensaje", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderID);
            messageTextBody.put("to", messageReceiverID);
            messageTextBody.put("messageID", messagePushID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ChatActivity.this, "Mensaje enviado correctamente", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    input_message.setText("");
                }
            });
        }
    }

}
