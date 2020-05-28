package com.example.test_cont;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageButton mandar_mensaje;
    private EditText input_group_message;
    private ScrollView my_scroll_view;
    private TextView group_chat_text;
    private TextView custom_profile_group;
    private Button back2;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, GroupNameRef, GroupMessageKey;

    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        currentGroupName=getIntent().getExtras().get("groupName").toString();

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef=FirebaseDatabase.getInstance().getReference().child("Grupos").child(currentGroupName);

        InitializeFields();

        custom_profile_group.setText(currentGroupName);

        GetUserInfo();


        mandar_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMessageInfoToDatabase();

                input_group_message.setText("");

                my_scroll_view.fullScroll(my_scroll_view.FOCUS_DOWN);
            }
        });
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected  void onStart() {

        super.onStart();

        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void InitializeFields() {

        mandar_mensaje=(ImageButton) findViewById(R.id.mandar_mensaje);
        input_group_message=(EditText) findViewById(R.id.input_group_message);
        my_scroll_view=(ScrollView) findViewById(R.id.my_scroll_view);
        group_chat_text=(TextView) findViewById(R.id.group_chat_text);
        back2 = (Button) findViewById(R.id.back2);
        custom_profile_group=(TextView)findViewById(R.id.custom_profile_group);
    }

    private void GetUserInfo() {
        usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //comprobar si el usuario existe
                if(dataSnapshot.exists()){
                    currentUserName=dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SaveMessageInfoToDatabase() {
        String message=input_group_message.getText().toString();
        String messageKey=GroupNameRef.push().getKey();
        if(TextUtils.isEmpty(message)){
            Toast.makeText(this,"Escribe un mensaje primero...",Toast.LENGTH_SHORT).show();

        }else{
            java.util.Calendar calForDate= java.util.Calendar.getInstance();
            SimpleDateFormat currentDateFormat=new SimpleDateFormat("dd/MM/YYYY");
            currentDate=currentDateFormat.format(calForDate.getTime());

            java.util.Calendar calForTime= Calendar.getInstance();
            SimpleDateFormat currentTimeFormat=new SimpleDateFormat("HH:mm");
            currentTime=currentTimeFormat.format(calForTime.getTime());

            HashMap<String, Object> groupMessageKey=new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);

            GroupMessageKey=GroupNameRef.child(messageKey);
            HashMap<String, Object> messageInfoMap=new HashMap<>();
            messageInfoMap.put("name",currentUserName);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date",currentDate);
            messageInfoMap.put("time",currentTime);
            GroupMessageKey.updateChildren(messageInfoMap);

        }
    }

    private void DisplayMessages(DataSnapshot dataSnapshot){
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while(iterator.hasNext()){
            String chatDate=(String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage=(String)((DataSnapshot)iterator.next()).getValue();
            String chatName=(String)((DataSnapshot)iterator.next()).getValue();
            String chatTime=(String)((DataSnapshot)iterator.next()).getValue();

            group_chat_text.append(chatName+" :\n"+ chatMessage+"\n"+chatDate+"       "+chatTime+"\n\n");

            my_scroll_view.fullScroll(my_scroll_view.FOCUS_DOWN);

        }
    }

}
