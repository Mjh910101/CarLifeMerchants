package com.carlife.merchants.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlife.merchants.R;
import com.carlife.merchants.activitys.RepairActivity;
import com.carlife.merchants.activitys.RepairDataActivity;
import com.carlife.merchants.box.RepairObj;
import com.carlife.merchants.handlers.ColorHandler;
import com.carlife.merchants.tool.Passageway;

import java.util.List;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/1/25.
 */
public class RepairDataAdapter extends BaseAdapter {
    private Context context;
    private List<RepairObj> itemList;
    private LayoutInflater inflater;

    public RepairDataAdapter(Context context, List<RepairObj> list) {
        initAdapter(context);
        this.itemList = list;
    }

    private void initAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.layout_repair_data_item, null);
            holder = new HolderView();

            holder.time = (TextView) convertView.findViewById(R.id.repairItem_time);
            holder.maney = (TextView) convertView.findViewById(R.id.repairItem_maney);
            holder.address = (TextView) convertView.findViewById(R.id.repairItem_address);
            holder.id = (TextView) convertView.findViewById(R.id.repairItem_id);
            holder.status = (TextView) convertView.findViewById(R.id.repairItem_status);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        RepairObj obj = itemList.get(position);
        setView(holder, obj);
        setOnClick(convertView, obj);
        return convertView;
    }

    private void setOnClick(View view, final RepairObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RepairObjHandler.saveRepairObj(obj);
                Bundle b = new Bundle();
                b.putString("id", obj.getObjectId());
                b.putString("fee", obj.getFee());
                Passageway.jumpActivity(context, RepairDataActivity.class, b);
            }
        });
    }

    private void setView(HolderView holder, RepairObj obj) {

        holder.time.setText(obj.getCreatedAt());
        holder.address.setText(obj.getAddress());
        holder.maney.setText("￥" + obj.getFee());
        holder.id.setText(obj.getObjectId());

        holder.status.setVisibility(View.VISIBLE);
        switch (obj.getOrder_status()) {
            case "2":
                holder.status.setTextColor(ColorHandler.getColorForID(context, R.color.text_red_02));
                holder.status.setText("未付款");
                holder.status.setBackgroundResource(R.drawable.red02_box);
                break;
            case "3":
                holder.status.setTextColor(ColorHandler.getColorForID(context, R.color.text_gary_02));
                holder.status.setText("已付款");
                holder.status.setBackgroundResource(R.drawable.gray02_box);
                break;
            default:
                holder.status.setVisibility(View.GONE);
                break;
        }
    }

    public void addItems(List<RepairObj> list) {
        for (RepairObj obj : list) {
            itemList.add(obj);
        }
        notifyDataSetChanged();
    }

    class HolderView {
        TextView time;
        TextView maney;
        TextView address;
        TextView id;
        TextView status;
    }
}
