package home.safe.com.notice;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by plupin724 on 2017-11-16.
 * 공지사항 리스트뷰의 어댑터 객체 생성
 */

public class AdapterNotice extends BaseExpandableListAdapter {

    public Context context;

    ArrayList<TestDtoNotice> items = new ArrayList<TestDtoNotice>();
    HashMap<TestDtoNotice, String> hash = new HashMap<>();

    //메인액티비티에서 보낸 정보를 어레이리스트에 추가
    public void addItem(TestDtoNotice test){

        Log.v("ㅇㅇ?", "ㅇㅇ?");

        items.add(test);

        hash.put(test, test.getTestcontents());

    }



    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return items.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        return items.get(i).getTestcontents();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        /*String titleName = title.get(i);
        Log.v("타이틀네임", titleName);
        String authorName = author.get(i);
        Log.v("저자네임", authorName);
        String index = String.valueOf(i);
        Log.v("인덱스", index);
        View v = view;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = (LinearLayout) inflater.inflate(R.layout.elv_item_title, null);
        }

        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        TextView tvAuthor = (TextView)view.findViewById(R.id.tvAuthor);
        tvTitle.setText(titleName);
        tvAuthor.setText(authorName);

        return v;*/

        Log.v("어딥니꽈?", "어디죠?");


        /*ListViewItemTitle itemTitle = new ListViewItemTitle(context);

        Log.v("됩니까?", "되요?");

        TestDtoNotice dto = items.get(i);

        itemTitle.setTitle(dto.getTesttitle(), dto.getTestauthor());*/

        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        /*String contentName = hash.get(title.get(i)).get(i1);

        View v = view;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (LinearLayout) inflater.inflate(R.layout.elv_item_contents, null);

        }

        TextView tvContents = view.findViewById(R.id.tvContents);
        tvContents.setText(contentName);

        return v;*/

        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
