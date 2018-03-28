package com.example.dennis.testapp.SearchConsole;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dennis.testapp.R;

import java.util.ArrayList;


public class ConsoleListAdapter extends BaseAdapter {


    Context context;
    ArrayList<String[]> data;
    private LayoutInflater inflater = null;

    public ConsoleListAdapter(Context context, ArrayList<String[]> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.console_row_item, null);

        TextView appTitle = (TextView) vi.findViewById(R.id.app_title);
        TextView pcName = (TextView) vi.findViewById(R.id.pc_name);
        TextView playerCount = (TextView) vi.findViewById(R.id.player_count);

        String gameNameStr = data.get(position)[4];
        String pcNameStr = data.get(position)[2];
        String playerCountStr = data.get(position)[3];

        Log.d("davidcal", gameNameStr);

        if (gameNameStr.equals("NO_APP"))
        {
            appTitle.setText("Choose an App");
            playerCount.setText("");
        }
        else if (gameNameStr.equals("SELECTING"))
        {
            appTitle.setText("Join server");

            if (playerCountStr == "1")
                playerCount.setText(playerCountStr + " Player Waiting");
            else
                playerCount.setText(playerCountStr + " Players Waiting");
        }
        else
        {
            appTitle.setText(gameNameStr.substring(1).replace("_", " "));

            if (playerCountStr == "1")
                playerCount.setText(playerCountStr + " Player");
            else
                playerCount.setText(playerCountStr + " Players");
        }

        pcName.setText(pcNameStr);

        return vi;
    }

}
