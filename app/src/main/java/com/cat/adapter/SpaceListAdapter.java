package com.cat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.cat.R;
import com.cat.entity.SpaceItem;

import java.util.List;

public class SpaceListAdapter extends BaseSwipListAdapter {


    private Context context;
    private List<SpaceItem> data;

    public SpaceListAdapter(Context context, List<SpaceItem> data) {
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
       ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.space_list, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bindView(position);

        return convertView;
    }
    class ViewHolder {
        TextView spaceNum;
        TextView ownerName;
        TextView contactNum;
        TextView inTime;
        TextView spaceName;

        ViewHolder(View view) {
            spaceName= (TextView) view.findViewById(R.id.address);
            spaceNum= (TextView) view.findViewById(R.id.space_num);
            ownerName= (TextView) view.findViewById(R.id.owner_name);
            contactNum= (TextView) view.findViewById(R.id.contact_num);
            inTime= (TextView) view.findViewById(R.id.in_time);
        }
        @SuppressLint("SetTextI18n")
        public void bindView(int position) {
            if (position >= data.size())
                return;

            SpaceItem spaceItem = data.get(position);

            //textTitle.setText(poiItem.getTitle());
            spaceName.setText(spaceItem.getSpaceName());
            spaceNum.setText(spaceItem.getSpaceNum().toString());
            ownerName.setText(spaceItem.getOwnerName());
            contactNum.setText(spaceItem.getContactNum());
            inTime.setText(spaceItem.getInnerTime());

        }
    }
            @Override
        public boolean getSwipEnableByPosition(int position) {
            return true;
        }
}
