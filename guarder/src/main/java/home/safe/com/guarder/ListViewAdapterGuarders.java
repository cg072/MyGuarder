package home.safe.com.guarder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by hotki on 2017-11-14.
 */
/* ArrayAdapter 또한 BaseAdapter를 상속하여 만들어졌다
 * 아이템이 TextView로 이루어진 경우를 위해 만들어 놓은 것이다
 * 그러므로 Custom Adapter를 만들 때는 ArrayAdapter 대신에 BaseAdapter를 사용하는 것이 좋다
 */

public class ListViewAdapterGuarders extends ArrayAdapter implements View.OnClickListener {

    ToggleButton btnGuardResist;

    // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의
    public interface GuardersListBtnClickListener {
        void onGuardersListBtnClick(int position, int count) ;
    }

    // 생성자로부터 전달된 resource id 값을 저장
    int resourceID;

    // 생성자로부터 전달된 ListBtnClickListener 저장
    private GuardersListBtnClickListener listBtnClickListener;

    // ListViewBtnAdapter 생성자, 마지막에 ListBtnClickListener 추가
    ListViewAdapterGuarders(Context context, int resource, ArrayList<ListViewItemGuarders> list, GuardersListBtnClickListener clickListener) {
        super(context, resource, list);

        // resource id 값 복사, (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceID = resource;

        // 생성자에 리스너 추가
        this.listBtnClickListener = clickListener;
    }

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItemGuarders> lvitemGuarders = new ArrayList<ListViewItemGuarders>();

    // position에 위치한 데이터를 화면에 출력하는데 사용될 view를 리턴 : 필수구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // 생성자로부터 저장된 resourceID(listview_item_guarders)에 해당하는 Layout을 inflate하여 convertView 참조 획득
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceID/*R.Layout.listview_item_guarders*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된) 로부터 위젯에 대한 참조 획득
        final TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        final TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);

        // Data Set(ListViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListViewItemGuarders lvItemGuarders = (ListViewItemGuarders) getItem(position);

        tvName.setText(lvItemGuarders.getTvName());
        tvPhone.setText(hyphenAdd(lvItemGuarders.getTvPhone()));

        // btnGuardersAdd 클릭 시 작업내용
        btnGuardResist = (ToggleButton) convertView.findViewById(R.id.btnGuardResist);
        btnGuardResist.setTag(position);
        if(lvItemGuarders.getUse() == true) {
            btnGuardResist.setChecked(true);
        } else {
            btnGuardResist.setChecked(false);
        }
        btnGuardResist.setOnClickListener(this);

       return convertView;
    }

    @Override
    public void onClick(View view) {
        // ActivityGuarder 의 GuardListBtnClickListener의 onGuardBtnClick() 함수 호출
        if(this.listBtnClickListener != null) {
            this.listBtnClickListener.onGuardersListBtnClick((int) view.getTag(), getCount());
        }
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    private String hyphenAdd(String phone) {

        String resultString = phone;

        switch(resultString.length()) {
            case 10 :
                resultString =  resultString.substring(0,3) + "-" +
                        resultString.substring(3,6) + "-" +
                        resultString.substring(6,10);
                break;

            case 11 :
                resultString =  resultString.substring(0,3) + "-" +
                        resultString.substring(3,7) + "-" +
                        resultString.substring(7,11);
                break;
            default :
                resultString = "Error";
        }
        return resultString;
    }
}
