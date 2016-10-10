package com.islavdroid.chat2;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<ChatMessage> {

    private Activity activity;
    private List<ChatMessage> messages;
    AlertDialog dialog;
  //  TextView textTitle;
    String titleText;

    public MessageAdapter(Activity context, int resource, List<ChatMessage> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        ChatMessage chatMessage = getItem(position);
        int viewType = getItemViewType(position);

        if (chatMessage.isMine()) {
            layoutResource = R.layout.chat_left;
        } else {
            layoutResource = R.layout.chat_right;
        }

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        //set message content
        holder.msg.setText(chatMessage.getContent());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private class ViewHolder {
        private TextView msg;
        private RelativeLayout layout;

        public ViewHolder(View v) {

            msg = (TextView) v.findViewById(R.id.txt_msg);
            LinearLayout layout=(LinearLayout)v.findViewById(R.id.bubble);


            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                   View dialogTitle = LayoutInflater.from(activity).inflate(R.layout.title_for_chat_dialog,null);



                    final String[] dialogFunctions ={"Копировать","Редактировать", "Удалить"};
                    builder.setItems(dialogFunctions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                   // builder.setCancelable(false);
                     dialog = builder.create();
                  //  TextView textView=(TextView)dialog.findViewById(R.id.text_chat_dialog) ;
                     titleText = msg.getText().toString();
                    //textTitle.setText(titleText);

                   // dialog.setTitle(msg.getText().toString());

                   dialog.setCustomTitle(dialogTitle);

                    dialog.show();
                    return true;





                }
            });
        }
    }
}