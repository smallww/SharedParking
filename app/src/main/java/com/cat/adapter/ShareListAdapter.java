package com.cat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.cat.R;
import com.cat.entity.TaskShareItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShareListAdapter extends BaseSwipListAdapter {

    private Context context;
    private List<TaskShareItem> data;

    public ShareListAdapter(Context context, List<TaskShareItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShareListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.share_list, parent, false);
            viewHolder = new ShareListAdapter.ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ShareListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.bindView(position);

        return convertView;
    }

    public class ViewHolder {
        TextView address;
        TextView spaceNum;
        TextView ownerName;
        TextView contactNum;
        TextView time;
        TextView inTime;
        TextView nameTV;
        TextView phoneTV;
        TextView typeTV;

        ViewHolder(View view) {
            address= (TextView) view.findViewById(R.id.address);
            spaceNum= (TextView) view.findViewById(R.id.space_num);
            ownerName= (TextView) view.findViewById(R.id.owner_name);
            contactNum= (TextView) view.findViewById(R.id.contact_num);
            time= (TextView) view.findViewById(R.id.time);
            inTime= (TextView) view.findViewById(R.id.in_time);
            nameTV= (TextView) view.findViewById(R.id.nameTV);
            phoneTV= (TextView) view.findViewById(R.id.phoneTV);
            typeTV= (TextView) view.findViewById(R.id.typeTV);
        }
        @SuppressLint("SetTextI18n")
        public void bindView(int position) {
            if (position >= data.size())
                return;

            TaskShareItem taskShareItem = data.get(position);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String star=taskShareItem.getStartTime().substring(0,taskShareItem.getStartTime().length()-3);
            String end=taskShareItem.getEndTime().substring(0,taskShareItem.getEndTime().length()-3);

            //textTitle.setText(poiItem.getTitle());
            address.setText(taskShareItem.getAddress());
            spaceNum.setText(taskShareItem.getSpaceNum().toString());
            ownerName.setText(taskShareItem.getOwnerName());
            contactNum.setText(taskShareItem.getContactNum());
            time.setText(star+" - "+end);
            inTime.setText(sdf.format(new Date(Long.valueOf(taskShareItem.getCreateTime()))));
            typeTV.setText(taskShareItem.getTaskType());
            Log.i("667", String.valueOf(taskShareItem));

        }
    }
    @Override
    public boolean getSwipEnableByPosition(int position) {
        return true;
    }
}

