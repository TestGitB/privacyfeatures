package com.android.phoneagent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.phoneagent.R;
import com.android.phoneagent.entities.DeviceContact;
import com.android.phoneagent.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends BaseAdapter
{
    private final Context mContext;
    private static LayoutInflater mInflater;
    private List<DeviceContact> contacts;

    public ContactsAdapter(Context context)
    {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        contacts = new ArrayList<DeviceContact>();
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    public int getCount()
    {
        return contacts.size();
    }

    public DeviceContact getItem(int position)
    {
        return contacts.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.contact_list_item, null);
            holder = new ViewHolder();
            holder.contactName = (TextView) convertView.findViewById(R.id.contactName);
            holder.contactPhone = (TextView) convertView.findViewById(R.id.contactPhone);
            holder.contactState = convertView.findViewById(R.id.contactState);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        DeviceContact entry = contacts.get(position);

        holder.contactName.setText(entry.getContactName());
        holder.contactPhone.setText(entry.getContactNumber());
        holder.contactState.setBackgroundResource(Utils.getColorForContactState(entry.getContactState()));
        //holder.contactState.setBackgroundColor(Utils.getColorForContactState(entry.getContactState()));
        return convertView;
    }

    public void setContacts(List<DeviceContact> dataset)
    {
        contacts = dataset;
    }

    public static class ViewHolder
    {
        TextView contactName;
        TextView contactPhone;
        View contactState;
    }


}

