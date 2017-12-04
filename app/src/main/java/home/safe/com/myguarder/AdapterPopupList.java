package home.safe.com.myguarder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by JINNY_ on 2017-12-04.
 */

public class AdapterPopupList extends BaseAdapter{

    ArrayList<LocationVO> data;

    public AdapterPopupList(ArrayList<LocationVO> data)
    {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_popup_list, parent, false);

        //내용 코딩

        return convertView;
    }
}
