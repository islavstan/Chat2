package com.islavdroid.chat2;


import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

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
    TextView user_status;
    private List<Stickers> stickerList = new ArrayList<>();
    private RecyclerView recyclerView;
 boolean typingStarted=false;
    private RecStickerAdapter mAdapter;
    TextView textPrint;
    Dialog translateDialog;
    ImageView favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_status=(TextView) findViewById(R.id.user_status);
        user_status.setSelected(true);

           favorite =(ImageView)findViewById(R.id.favorite);
        favorite.setVisibility(View.GONE);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorite.getVisibility()==View.VISIBLE){
                    favorite.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Доверительный чат отключен",Toast.LENGTH_SHORT).show();
                }
            }
        });









//----------------------------drawer---------------------------------




        navigationDrawerRight = new DrawerBuilder().withActivity(this).withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false).withToolbar(toolbar).
                        withActionBarDrawerToggleAnimated(true).withDrawerGravity(Gravity.RIGHT).withSavedInstance(savedInstanceState)
                .withSelectedItem(-1).build();










       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Сменить фон").withIcon(R.drawable.ic_wallpaper_black_24dp).withSelectable(false).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
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
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Информация о контакте").withIcon(R.drawable.ic_account_circle_black_24dp).withSelectable(false));

       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Переименовать контакт").withIcon(R.drawable.ic_create_black_24dp).withSelectable(false).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
           @Override
           public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
               renameContact();
               return true;
           }
       }));
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Галерея").withIcon(R.drawable.ic_insert_photo_black_24dp).withSelectable(false));
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Язык сообщений").withIcon(R.drawable.ic_translate_black_24dp).withSelectable(false).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
           @Override
           public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
               navigationDrawerRight.closeDrawer();
               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               View dialogTitle = (LinearLayout) getLayoutInflater()
                       .inflate(R.layout.title_for_translate_dialog, null);




               final String[] dialogFunctions ={"албанский","амхарский", "английский","арабский","армянский","белорусский",
               "болгарский","мальтийский","албанский","албанский","албанский","албанский","албанский","албанский"};
               builder.setItems(dialogFunctions, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });

               builder.setCustomTitle(dialogTitle);
               translateDialog = builder.create();
               translateDialog.show();
               return true;

           }
       }));



        navigationDrawerRight.addItem( new DividerDrawerItem());
        navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Доверительный чат").withIcon(R.drawable.ic_favorite_black_24dp).withSelectable(false).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                favorite.setVisibility(View.VISIBLE);
                navigationDrawerRight.closeDrawer();
                Toast.makeText(MainActivity.this,"Доверительный чат включен",Toast.LENGTH_SHORT).show();
                return true;

            }
        }));
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Защитить контакт паролем").withIcon(R.drawable.ic_lock_black_24dp).withSelectable(false).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
           @Override
           public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
               showPasswordDialog();
               return true;
           }
       }));
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Скрыть контакт").withIcon(R.drawable.ic_visibility_off_black_24dp).withSelectable(false));
       navigationDrawerRight.addItem(new PrimaryDrawerItem().withName("Удалить чат").withIcon(R.drawable.ic_delete_black_24dp).withSelectable(false).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
           @Override
           public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
               deleteChat();



               return true;
           }
       }));



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
                   btn_add.setImageResource(R.drawable.ic_clear_white_24dp);

                   additionalLayout.setVisibility(View.VISIBLE);
               }
               else {
                   layoutParamsForStickers.addRule(RelativeLayout.ABOVE,0);
                   layoutParamsForStickers.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                   additionalLayout.setVisibility(View.GONE);
                   btn_add.setImageResource(R.drawable.ic_add_black_24dp);
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


//-----------------------уведомление о печати-----------------------------
         textPrint = (TextView)findViewById(R.id.text_print) ;
        textPrint.setVisibility(View.GONE);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1) {
                    //Log.i(TAG, “typing started event…”);
                    typingStarted = true;
                    textPrint.setVisibility(View.VISIBLE);
                    //send typing started status
                } else if (s.toString().trim().length() == 0 && typingStarted) {
                    //Log.i(TAG, “typing stopped event…”);
                    typingStarted = false;
                    textPrint.setVisibility(View.GONE);
                    //send typing stopped status
                }}


            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//----------------------------------уведомление о печати-----------------------------






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




   //показать диалог для ввода пароля для контакта
    protected void showPasswordDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_for_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.password_edittext);
        // setup a dialog window
        alertDialogBuilder
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      //  resultText.setText("Hello, " + editText.getText());
                    }
                })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();

// Customize the button
        navigationDrawerRight.closeDrawer();
        alert.show();
        Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setTextColor(Color.BLACK);
        //  button.setTypeface(Typeface.DEFAULT_BOLD);
        Button button2 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        button2.setTextColor(Color.BLACK);

    }

    //показать диалог для ввода пароля для контакта
    protected void renameContact() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_for_rename, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.name_edittext);
        // setup a dialog window
        alertDialogBuilder
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  resultText.setText("Hello, " + editText.getText());
                    }
                })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();

// Customize the button
        navigationDrawerRight.closeDrawer();
        alert.show();
        Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setTextColor(Color.BLACK);
        //  button.setTypeface(Typeface.DEFAULT_BOLD);
        Button button2 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        button2.setTextColor(Color.BLACK);

    }





    //показать диалог для очистки чата
    protected void deleteChat() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.title_for_delete_chat, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setCustomTitle(promptView);
        alertDialogBuilder.setMessage("Вы уверены, что хотите удалить все сообщения?");



        // setup a dialog window
        //alertDialogBuilder.setCancelable(false)
        alertDialogBuilder  .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  resultText.setText("Hello, " + editText.getText());
                    }
                })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();

// Customize the button
        navigationDrawerRight.closeDrawer();
        alert.show();
        Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setTextColor(Color.BLACK);
        //  button.setTypeface(Typeface.DEFAULT_BOLD);
        Button button2 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        button2.setTextColor(Color.BLACK);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.more:
                navigationDrawerRight.openDrawer();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}