package home.safe.com.trans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by plupin724 on 2017-12-01.
 * 이동수단내역보기 프래그먼트에 리스트 뷰를 전달할 어댑터 클래스 작성
 */

public class AdapterListTrans extends BaseAdapter{

    //메인액티비티에서 받을 정보를 저장할 배열리스트
    ArrayList<TestListViewDTO> items = new ArrayList<TestListViewDTO>();

    public void addItem(TestListViewDTO dto){
        items.add(dto);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int position = i;
        Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.items_trans_list, viewGroup, false);
        }

        TextView listseq = (TextView) view.findViewById(R.id.listseq);
        TextView listtype = (TextView) view.findViewById(R.id.listtype);
        TextView listname = (TextView) view.findViewById(R.id.listname);

        TestListViewDTO dto = items.get(i);

        listseq.setText(dto.getNum());
        listtype.setText(dto.getType());
        listname.setText(dto.getName());


        return view;
    }
}
