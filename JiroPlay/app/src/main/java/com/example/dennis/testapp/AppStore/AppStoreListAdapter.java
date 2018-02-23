package com.example.dennis.testapp.AppStore;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dennis.testapp.LocalApps.LocalAppInfo;
import com.example.dennis.testapp.R;
import com.example.dennis.testapp.Singletons.RusSingleton;
import com.squareup.picasso.Picasso;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.List;


public class AppStoreListAdapter extends RecyclerView.Adapter<AppStoreListAdapter.MyViewHolder> {

    private List<StoreAppInfo> appStoreArr;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView mainAppImage;
        public TextView appTitleText;
        public TextView ratingText;
        public TextView appDescriptionText;
        public TextView playerCountText;
        public ImageView gyroImage;
        public ImageView accelImage;
        public ImageView bgImage;
        View holderView;

        public MyViewHolder(final View view) {
            super(view);
            mainAppImage = (ImageView) view.findViewById(R.id.main_image);
            appTitleText = (TextView) view.findViewById(R.id.app_title);
            ratingText = (TextView) view.findViewById(R.id.app_rating);
            gyroImage = (ImageView)view.findViewById(R.id.gyroscope);
            accelImage = (ImageView)view.findViewById(R.id.accelerometer);
            playerCountText = (TextView)view.findViewById(R.id.num_players);
            appDescriptionText = (TextView)view.findViewById(R.id.app_desc);
            bgImage = (ImageView)view.findViewById(R.id.bgimage);
            holderView = view;


        }
    }


    public AppStoreListAdapter(List<StoreAppInfo> appStoreArr) {
        this.appStoreArr = appStoreArr;
    }

    @Override
    public AppStoreListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_store_row_item, parent, false);

        return new AppStoreListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AppStoreListAdapter.MyViewHolder holder, int position) {
        StoreAppInfo app = appStoreArr.get(position);



        // set Game title
        holder.appTitleText.setText(app.getAppTitle());

        // set game rating
        DecimalFormat ratingFormat = new DecimalFormat("#.0");
        holder.ratingText.setText(ratingFormat.format(app.getRating()));

        // set game image
        Picasso.with(holder.mainAppImage.getContext()).load(app.getMainAppIconUrl()).fit().centerInside().into(holder.mainAppImage);

        // set game description
        holder.appDescriptionText.setText(app.getDescription());

        // set game player count
        holder.playerCountText.setText(app.getMinPlayers() + " - " + app.getMaxPlayers());

        if(!app.usesAccelerometer()){
            holder.accelImage.setAlpha(0.4f);
        }
        if(!app.usesGyroscope()){
            holder.gyroImage.setAlpha(0.4f);
        }



        if(!app.getAppBackgroundUrl().equals("")){
            Log.d("bgimage", "setting");
            Picasso.with(holder.bgImage.getContext()).load(app.getAppBackgroundUrl()).fit().into(holder.bgImage);
        }else{
            Log.d("bgimage", "not setting");
        }


    }

    @Override
    public int getItemCount() {
        return appStoreArr.size();
    }
}
