package com.example.dennis.testapp.LocalApps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dennis.testapp.R;
import com.example.dennis.testapp.Singletons.RusSingleton;
import com.squareup.picasso.Picasso;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.List;

public class LocalAppListAdapter extends RecyclerView.Adapter<LocalAppListAdapter.MyViewHolder> {

    private List<LocalAppInfo> localAppList;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView smallAppImage;
        public TextView appTitleText;
        public ImageView circleImg;
        public ImageView uninstallImg;
        public TextView ratingValText;
        public View holderView;

        public MyViewHolder(View view) {
            super(view);
            smallAppImage = (ImageView) view.findViewById(R.id.small_app_image);
            appTitleText = (TextView) view.findViewById(R.id.app_title);
            circleImg = (ImageView) view.findViewById(R.id.rating_circle);
            uninstallImg = (ImageView) view.findViewById(R.id.uninstall);
            ratingValText = (TextView) view.findViewById(R.id.rating_val);
            holderView = view;



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RusSingleton.getInstance().getRusConsoleServer().sendr(("app start " + localAppList.get(getAdapterPosition()).id).getBytes(Charset.forName("US-ASCII")));

                    if (!RusSingleton.getInstance().getActivityString().equals("ControllerScreen")) {

                        RusSingleton.getInstance().getAppsActivity().finish();

                    }
                }
            });





        }
    }


    public LocalAppListAdapter(List<LocalAppInfo> localAppList) {
        this.localAppList = localAppList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_apps_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LocalAppInfo app = localAppList.get(position);

        holder.appTitleText.setText(app.getAppTitle().replace('_', ' '));

        if(!app.isPayedFor()){
           holder.holderView.setAlpha(0.3f);
        }

        DecimalFormat ratingFormat = new DecimalFormat("#.0");

        if(app.isInstalled()){
            holder.ratingValText.setText("");
            holder.circleImg.setVisibility(View.INVISIBLE);
            holder.ratingValText.setVisibility(View.INVISIBLE);
        }else{
            holder.ratingValText.setText(ratingFormat.format(app.getRating()));
            holder.uninstallImg.setVisibility(View.INVISIBLE);
        }

        Picasso.with(holder.smallAppImage.getContext()).load(app.getSmallAppIconUrl()).into(holder.smallAppImage);
    }

    @Override
    public int getItemCount() {
        return localAppList.size();
    }
}
