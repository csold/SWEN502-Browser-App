package com.example.oldchri.bitbrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by oldchri on 19/10/2017.
 */

public class TabAdapter extends ArrayAdapter<Tab> {

    int resource;
    String response;
    Context context;

    public TabAdapter(Context context, int resource, List<Tab> items) {
        super(context, resource, items);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linkView;
        Tab link = getItem(position);
        if(convertView==null) {
            linkView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, linkView, true);
        } else {
            linkView = (LinearLayout) convertView;
        }
        TextView url = (TextView)linkView.findViewById(R.id.tabUrl);
        TextView pos = (TextView)linkView.findViewById(R.id.tabPos);
        url.setText(link.getUrl());
        pos.setText(String.valueOf(link.getPos()));
        return linkView;
    }
}
