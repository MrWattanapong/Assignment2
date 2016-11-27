package com.egco428.a23281;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lostl2ose on 11/27/2016.
 */
public class CustomAdapter extends ArrayAdapter<Person> {
    Context context;
    List<Person> user;
    public CustomAdapter(Context context, List<Person> user){
        super(context, 0 , user);
        this.context = context;
        this.user = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = user.get(position);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item, null);

        TextView user = (TextView)view.findViewById(R.id.textView);
        user.setText(person.getUsername());

        return view;
    }
}
