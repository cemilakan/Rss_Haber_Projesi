package net.kisacasi.bes_mid_rss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    public LayoutInflater layoutInflater;
    private Context context;
    private List<Model> modelList;
    public CustomAdapter(Context context, List<Model> models) {
        this.context=context;
        this.modelList=models;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LinearLayout rootView= (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.custom_list,null);
        ImageView resim = (ImageView) rootView.findViewById(R.id.resim);
        TextView date_and_creator = (TextView) rootView.findViewById(R.id.date_and_creator);
        TextView title = (TextView) rootView.findViewById(R.id.title);

        final Model model=modelList.get(i);
        Date date=new Date(model.getDate());
        DateFormat df=new SimpleDateFormat("dd.MM.yyyy");

        resim.setImageBitmap(model.getResim());
        title.setText(model.getTitle());
        date_and_creator.setText(String.format("%02d:%02d", date.getHours(), date.getMinutes()) + " | " + df.format(date)+"   |  "+ model.getCreator());
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkiAc(model.getLink());
            }
        });


        return rootView;


    }

    private void linkiAc(String link) {
        Intent intent = new Intent(context, Icerik.class);
        intent.putExtra("link", link);
        context.startActivity(intent);
    }
}
