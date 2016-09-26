package com.islavdroid.chat2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class RecStickerAdapter extends RecyclerView.Adapter<RecStickerAdapter.MyViewHolder> {
    private List<Stickers> eList;
    int layout ;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListener recyclerViewOnClickListener;
    public RecStickerAdapter(Context c, List<Stickers>l, int layout){
        eList=l;
        layoutInflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout=layout;
    }





    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v =layoutInflater.inflate(layout,viewGroup,false);
        MyViewHolder viewHolder=new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.image.setImageResource(eList.get(position).getSticker());
    }

    @Override
    public int getItemCount() {
        return eList.size();
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener r){
        recyclerViewOnClickListener=r;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image=(ImageView) itemView.findViewById(R.id.sticker_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListener !=null){
                recyclerViewOnClickListener.OnclickListener(v,getPosition());
            }
        }
    }
}
