package com.islavdroid.chat2;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;




public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private View btnSend;
    private EditText editText;
    boolean isMine = true;
    private List<ChatMessage> chatMessages;
    private ArrayAdapter<ChatMessage> adapter;
    private ImageButton btn_sticker;
    private View stickerLayout;
    private View additionalLayout;
    RelativeLayout stickersLayout;
    RelativeLayout layoutForStickers;
    RelativeLayout layoutForAdding;
    RelativeLayout.LayoutParams layoutParamsForStickers;
    RelativeLayout.LayoutParams layoutParamsForAdding;
    private ImageButton btn_add;

    private List<Stickers> stickerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecStickerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//-----------------------стикеры---------------------

        recyclerView = (RecyclerView)findViewById(R.id.sticker_recycler_view);
        mAdapter = new RecStickerAdapter(this,stickerList,R.layout.sticker_row);
       // RecyclerView.LayoutManager mLayoutManager =   new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
       RecyclerView.LayoutManager mLayoutManager =  new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        prepareUserData();




        stickerLayout =findViewById(R.id.sticker_layout);
        stickerLayout.setVisibility(View.GONE);


layoutForStickers = new RelativeLayout(this);

stickersLayout=(RelativeLayout)findViewById(R.id.relSendMessage) ;

        layoutParamsForStickers = (RelativeLayout.LayoutParams) stickersLayout
                .getLayoutParams();




        chatMessages = new ArrayList<>();
        btn_sticker =(ImageButton)findViewById(R.id.btn_sticker);


        btn_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stickerLayout.getVisibility() == View.GONE) {
                    additionalLayout .setVisibility(View.GONE);
               layoutParamsForStickers.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,0);
                    layoutParamsForStickers.addRule(RelativeLayout.ABOVE,R.id.sticker_layout);
                    stickerLayout.setVisibility(View.VISIBLE);
                }
                else {
                    layoutParamsForStickers.addRule(RelativeLayout.ABOVE,0);
                    layoutParamsForStickers.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    stickerLayout.setVisibility(View.GONE);
                }
            }
        });
//-----------------------стикеры---------------------

//-----------------дополнения---------------
   btn_add = (ImageButton) findViewById(R.id.btn_add);
        additionalLayout=(RelativeLayout)findViewById(R.id.add_layout);
        additionalLayout.setVisibility(View.GONE);


       btn_add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (additionalLayout.getVisibility() == View.GONE) {
                   stickerLayout.setVisibility(View.GONE);
                   layoutParamsForStickers.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,0);
                   layoutParamsForStickers.addRule(RelativeLayout.ABOVE,R.id.add_layout);
                   additionalLayout.setVisibility(View.VISIBLE);
               }
               else {
                   layoutParamsForStickers.addRule(RelativeLayout.ABOVE,0);
                   layoutParamsForStickers.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                   additionalLayout.setVisibility(View.GONE);
               }
           }
       });

//-----------------дополнения---------------

        listView = (ListView) findViewById(R.id.list_msg);
        btnSend = findViewById(R.id.btn_chat_send);
        editText = (EditText) findViewById(R.id.msg_type);

        //set ListView adapter first
        adapter = new MessageAdapter(this, R.layout.chat_left, chatMessages);
        listView.setAdapter(adapter);

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Введите текст...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    ChatMessage chatMessage = new ChatMessage(editText.getText().toString(), isMine);
                    chatMessages.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                    if (isMine) {
                        isMine = false;
                    } else {
                        isMine = true;
                    }
                }
            }
        });
    }

    private void prepareUserData() {
        Stickers sticker = new Stickers(R.drawable.cat);
        stickerList.add(sticker);
        sticker = new Stickers(R.drawable.cat2);
        stickerList.add(sticker);
        sticker = new Stickers(R.drawable.cat3);
        stickerList.add(sticker);
        sticker = new Stickers(R.drawable.google);
        stickerList.add(sticker);
        sticker = new Stickers(R.drawable.gun);
        stickerList.add(sticker);
        sticker = new Stickers(R.drawable.pizza);
        stickerList.add(sticker);
        sticker = new Stickers(R.drawable.skelet);
        stickerList.add(sticker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}