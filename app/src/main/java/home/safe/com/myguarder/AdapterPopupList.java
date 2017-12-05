package home.safe.com.myguarder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JINNY_ on 2017-12-04.
 */

/**
*
* @author 경창현
* @version 1.0.0
* @text PopupList -  Adapter
* @since 2017-12-05 오후 2:40
**/
public class AdapterPopupList extends BaseAdapter{

    ArrayList<LocationVO> alData;

    public AdapterPopupList(ArrayList<LocationVO> alData)
    {
        this.alData = alData;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_popup_list, parent, false);

        TextView tvListNumber = (TextView)convertView.findViewById(R.id.tvListNumber);
        TextView tvListDate = (TextView)convertView.findViewById(R.id.tvListDate);

        tvListNumber.setText(""+alData.get(position).getLseq());
        tvListDate.setText(alData.get(position).getLday());

        return convertView;
    }
}
