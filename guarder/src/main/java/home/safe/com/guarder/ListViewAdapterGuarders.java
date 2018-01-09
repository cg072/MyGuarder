package home.safe.com.guarder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hotki on 2017-11-14.
 */
/* ArrayAdapter 또한 BaseAdapter를 상속하여 만들어졌다
 * 아이템이 TextView로 이루어진 경우를 위해 만들어 놓은 것이다
 * 그러므로 Custom Adapter를 만들 때는 ArrayAdapter 대신에 BaseAdapter를 사용하는 것이 좋다
 */

public class ListViewAdapterGuarders extends ArrayAdapter implements View.OnClickListener {

    TextView btnGuardReg;

    public interface GuardersListBtnClickListener {
        void onGuardersListBtnClick(int position, int count) ;
    }

    int resourceID;
    private GuardersListBtnClickListener listBtnClickListener;

    ListViewAdapterGuarders(Context context, int resource, ArrayList<GuarderVO> list, GuardersListBtnClickListener clickListener) {
        super(context, resource, list);

        this.resourceID = resource;
        this.listBtnClickListener = clickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_guarders, parent, false);
        }

        final TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        final TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);

        final GuarderVO guarderVO = (GuarderVO) getItem(position);

        tvName.setText(guarderVO.getGmcname());
        tvPhone.setText(addHyphen(guarderVO.getGmcphone()));

        btnGuardReg = (TextView) convertView.findViewById(R.id.btnGuardReg);
        btnGuardReg.setTag(position);
        setRegBtn(guarderVO.getGstate());
        btnGuardReg.setOnClickListener(this);

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


    /*
     *  date     : 2017.11.22
     *  author   : Kim Jong-ha
     *  title    : addHyphen() 메소드 생성
     *  comment  : 전화 번호 사이의 '-' 를 추가한다
     *  return   : String 형태
     * */
    private String addHyphen(String phone) {

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

    public void setRegBtn(int sig) {
        switch (sig) {
            case 0 :
                btnGuardReg.setBackground(getContext().getResources().getDrawable(R.drawable.toggle_off));
                break;
            case 1 :
                btnGuardReg.setBackground(getContext().getResources().getDrawable(R.drawable.toggle_on));
                break;
        }
    }
}
