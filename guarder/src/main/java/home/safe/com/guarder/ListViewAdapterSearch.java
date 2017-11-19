package home.safe.com.guarder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by hotki on 2017-11-14.
 */
/* ArrayAdapter 또한 BaseAdapter를 상속하여 만들어졌다
 * 아이템이 TextView로 이루어진 경우를 위해 만들어 놓은 것이다
 * 그러므로 Custom Adapter를 만들 때는 ArrayAdapter 대신에 BaseAdapter를 사용하는 것이 좋다
 */

public class ListViewAdapterSearch extends ArrayAdapter implements View.OnClickListener{

    ListViewItemSearch saveItem = new ListViewItemSearch();
    Button btnSearchAdd ;
    String name;
    String phone;

    // 버튼 클릭 이벤트를 위한 Listrner 인터페이스 정의
    public interface SearchListBtnClickListener {
        void onSearchListBtnClick(int position, ListViewItemSearch listViewItemSearch) ;
    }

    // 생성자로부터 전달된 resource id 값을 저장
    int resourceID;

    // 생성자로부터 전달된 ListBtnClickListener 저장
    private SearchListBtnClickListener listBtnClickListener;

    // ListViewBtnAdapter 생성자, 마지막에 ListBtnClickListener 추가
    ListViewAdapterSearch(Context context, int resource, ArrayList<ListViewItemSearch> list, SearchListBtnClickListener clickListener) {
        super(context, resource, list);

        // resource id 값 복사, (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceID = resource;

        // 생성자에 리스너 추가
        this.listBtnClickListener = clickListener;
    }

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItemSearch> alitemSearch = new ArrayList<ListViewItemSearch>();

    // position에 위치한 데이터를 화면에 출력하는데 사용될 view를 리턴 : 필수구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // 생성자로부터 저장된 resourceID(listview_item_search)에 해당하는 Layout을 inflate하여 convertView 참조 획득
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceID/*R.Layout.listview_item_search*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된) 로부터 위젯에 대한 참조 획득
        final TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        final TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);

        // Data Set(ListViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItemSearch lvItemSearch = (ListViewItemSearch) getItem(position);

        tvName.setText(lvItemSearch.getTvName());
        tvPhone.setText(lvItemSearch.getTvPhone());

        alitemSearch.add(lvItemSearch);

        name = tvName.getText().toString();
        phone = tvPhone.getText().toString();

        // btnSearchAdd 클릭 시 작업내용
        btnSearchAdd = (Button) convertView.findViewById(R.id.btnSearchAdd);
/*        btnSearchAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원 검색 후 추가를 눌렀을 시 동작하는 내용을 코딩한다.
                Toast.makeText(context, tvName.getText() + "(을)를 추가하였습니다", Toast.LENGTH_SHORT).show();
                setSaveItem(tvName.getText().toString(), tvPhone.getText().toString());
            }
        });*/
        btnSearchAdd.setTag(position);
        btnSearchAdd.setOnClickListener(this);

       return convertView;
    }

    @Override
    public void onClick(View view) {
        // ActivityGuarder 의 GuardListBtnClickListener의 onGuardBtnClick() 함수 호출
        if(this.listBtnClickListener != null) {
            this.listBtnClickListener.onSearchListBtnClick((int) view.getTag(), alitemSearch.get((int) view.getTag()));
        }
    }

    private void setSaveItem(String name, String phone) {
        saveItem.setTvName(name);
        saveItem.setTvPhone(phone);
    }

    public ListViewItemSearch getSaveItem() {
        return saveItem;
    }

    public String getTvName(){

        return null;
    };
}
