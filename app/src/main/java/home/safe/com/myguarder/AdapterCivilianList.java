package home.safe.com.myguarder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import home.safe.com.guarder.GuarderVO;

/**
 * Created by JINNY_ on 2017-12-13.
 */

public class AdapterCivilianList extends BaseAdapter{

    ArrayList<GuarderVO> alData;

    public AdapterCivilianList(ArrayList<GuarderVO> data)
    {
        this.alData = data;
    }

    @Override
    public int getCount() {
        return alData.size();
    }

    @Override
    public Object getItem(int position) {
        return alData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_civilian_list,viewGroup,false);

        TextView tvCivilianName = (TextView)view.findViewById(R.id.tvCivilianName);
        TextView tvCivilianState = (TextView)view.findViewById(R.id.tvCivilianState);

        tvCivilianName.setText(alData.get(position).getGmcid());
        tvCivilianState.setText(""+ alData.get(position).getGstate());

        return view;
    }
}
