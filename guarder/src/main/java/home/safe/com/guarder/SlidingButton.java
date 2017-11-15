package home.safe.com.guarder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by hotki on 2017-11-14.
 */

public class SlidingButton extends Activity{
    SharedPreferences pref;
    ToggleButton btn_choice;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_button);

        btn_choice = (ToggleButton) findViewById(R.id.btn_toggle1);
        text = (TextView) findViewById(R.id.textView1);

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref.getString("choice", "").equals("1")){
            text.setText("ON");
        } else {
            text.setText("OFF");
        }

        btn_choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if(isChecked == true){
                    text.setText("ON");
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("choice", "1");
                    editor.commit();
                } else {
                    text.setText("OFF");
                    System.out.println("토글버튼 해제");
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("choice", "0");
                    editor.commit();
                }
            }
        });

    }
}

