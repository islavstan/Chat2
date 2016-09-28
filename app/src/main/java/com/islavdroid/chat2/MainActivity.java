package com.islavdroid.chat2;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    private Drawer navigationDrawerRight;

    private List<Stickers> stickerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecStickerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//----------------------------drawer---------------------------------
        navigationDrawerRight = new DrawerBuilder().withActivity(this).withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false).withToolbar(toolbar).
                withActionBarDrawerToggleAnimated(true).withDrawerGravity(Gravity.RIGHT).withSavedInstance(savedInstanceState)
        .withSelectedItem(-1).build();




        navigationDrawerRight.isDrawerOpen();




       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Сменить фон").withIcon(R.drawable.ic_wallpaper_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
           @Override
           public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
          // Toast.makeText(MainActivity.this,"iririr",Toast.LENGTH_SHORT).show();
              /* Intent intent = new Intent();
               intent.setType("image*//*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent, "Select Picture"),0);*/

               Intent galleryIntent = new  Intent(Intent.ACTION_PICK,
                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(galleryIntent, 002);
               return false;
           }
       }));
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Информация о контакте").withIcon(R.drawable.ic_account_circle_black_24dp));

       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Переименовать контакт").withIcon(R.drawable.ic_create_black_24dp));
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Галерея").withIcon(R.drawable.ic_insert_photo_black_24dp));
        navigationDrawerRight.addItem( new DividerDrawerItem());
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Защитить контакт паролем").withIcon(R.drawable.ic_lock_black_24dp));
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Скрыть чат").withIcon(R.drawable.ic_visibility_off_black_24dp));
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Удалить чат").withIcon(R.drawable.ic_delete_black_24dp));



        //navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Настройки").withIcon(R.drawable.ic_settings_black_24dp));

//----------------------------drawer---------------------------------


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


//------------------------смена фона-----------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 002 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            //Get the picked image uri
            String picturePath = getRealPathFromURI(selectedImage);
            RelativeLayout layout=(RelativeLayout)findViewById(R.id.all_display) ;

           BitmapDrawable bmp= new BitmapDrawable(picturePath);
          //  bmp.setGravity(Gravity.TOP);

            layout.setBackground(bmp);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    //------------------------смена фона-----------------------------




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