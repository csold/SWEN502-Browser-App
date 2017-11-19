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
 * Created by oldchri on 18/10/2017.
 */

public class LinkAdapter extends ArrayAdapter<Link> {

    int resource;
    String response;
    Context context;

    public LinkAdapter(Context context, int resource, List<Link> items) {
        super(context, resource, items);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linkView;
        Link link = getItem(position);
        if(convertView==null) {
            linkView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, linkView, true);
        } else {
            linkView = (LinearLayout) convertView;
        }
        ImageView favicon = (ImageView)linkView.findViewById(R.id.linkFavicon);
        TextView title = (TextView)linkView.findViewById(R.id.linkTitle);
        TextView url = (TextView)linkView.findViewById(R.id.linkUrl);
        favicon.setImageBitmap(link.getFavicon());
        title.setText(link.getTitle());
        url.setText(link.getUrl());
        return linkView;
    }
}
