package com.memo.dayandnight.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.memo.dayandnight.R;
import com.memo.dayandnight.ui.RVItemActivity;

/**
 * Created by lbl on 2017/1/13.
 */
public class SimpleChanAdapter extends RecyclerView.Adapter<SimpleChanAdapter.SimpleChanViewHolder> {

    private View.OnClickListener mSimpleClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, RVItemActivity.class);
            context.startActivity(intent);
        }
    };

    @Override
    public SimpleChanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.item_rv, parent, false);
        return new SimpleChanViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SimpleChanViewHolder holder, int position) {
        holder.itemView.setOnClickListener(mSimpleClickListener);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    static class SimpleChanViewHolder extends RecyclerView.ViewHolder{
        public SimpleChanViewHolder(View itemView) {
            super(itemView);
        }
    }
}
