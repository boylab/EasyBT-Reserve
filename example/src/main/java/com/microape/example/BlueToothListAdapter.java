package com.microape.example;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pengle on 2018-06-29.
 * email:pengle609@163.com
 */

public class BlueToothListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<BluetoothDevice> deviceList;

    public BlueToothListAdapter(Context context, ArrayList<BluetoothDevice> deviceList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_bluetooth_list, parent, false);
            viewHolder.tv_Name = convertView.findViewById(R.id.tv_Name);
            viewHolder.tv_Bond = convertView.findViewById(R.id.tv_Bond);
            viewHolder.tv_Mac = convertView.findViewById(R.id.tv_Mac);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            String bond = "";
            switch (getItem(position).getBondState()) {
                case BluetoothDevice.BOND_NONE:
                default:
                    bond = "未配对";
                    break;
                case BluetoothDevice.BOND_BONDING:
                    bond = "正在配对";
                    break;
                case BluetoothDevice.BOND_BONDED:
                    bond = "已配对";
                    break;
            }
            viewHolder.tv_Name.setText(getItem(position).getName());
            viewHolder.tv_Bond.setText(bond);
            viewHolder.tv_Mac.setText(getItem(position).getAddress());
            getItem(position).getBondState();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_Name, tv_Bond, tv_Mac;
    }
}