package com.bupt.affectionfornurse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bupt.affectionfornurse.R;

import java.util.List;

/**
 * Created by zhuyikun on 8/6/16.
 */
public class MessageAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> list;

    public MessageAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_listview, parent, false);
            holder = new ViewHolder();
            holder.tvMessageContent = (TextView) convertView.findViewById(R.id.tv_message_content);
            convertView.setTag(holder);
        }
        holder.tvMessageContent.setText(list.get(position).toString());
        return convertView;
    }
    static class ViewHolder {

        TextView tvMessageContent;

    }
}
