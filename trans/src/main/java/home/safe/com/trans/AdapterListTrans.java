package home.safe.com.trans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by plupin724 on 2017-12-01.
 * 이동수단내역보기 프래그먼트에 리스트 뷰를 전달할 어댑터 클래스 작성
 */

public class AdapterListTrans extends BaseExpandableListAdapter{
    ArrayList<TestListViewDTO> parentItems = new ArrayList<TestListViewDTO>();
    HashMap<TestListViewDTO, String> childItems = new HashMap<>();

    public void addItem(TestListViewDTO test){

        parentItems.add(test);
        childItems.put(test, test.getText());
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return parentItems.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return parentItems.get(i).getText();
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Context parentContext = viewGroup.getContext();

        ListViewItemBasicTrans itemBasic = new ListViewItemBasicTrans(parentContext.getApplicationContext());

        TestListViewDTO parDto = parentItems.get(i);

        itemBasic.setText(parDto.getNum(), parDto.getTranName(), parDto.getTime(), parDto.getAuthor());

        return itemBasic;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Context childContext = viewGroup.getContext();

        ListViewItemTextTrans itemText = new ListViewItemTextTrans(childContext.getApplicationContext());

        TestListViewDTO childDto = parentItems.get(i);
        itemText.setText(childDto.getText());

        return itemText;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}

/*public class AdapterListTrans extends BaseAdapter{

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
}*/
