package home.safe.com.myguarder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivitySetting extends AppCompatActivity implements View.OnClickListener{

    Button btnCycleSetting;
    Button btnGuarderSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnCycleSetting = (Button)findViewById(R.id.btnCycleSetting);
        btnGuarderSetting = (Button)findViewById(R.id.btnGuarderSetting);

        btnCycleSetting.setOnClickListener(this);
        btnGuarderSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnCycleSetting:
                Toast.makeText(this,"Cycle",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnGuarderSetting:
                Toast.makeText(this,"Guarder",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnTransSetting:
                break;
            case R.id.btnMyInfoSetting:
                break;
            case R.id.btnNoticeSetting:
                break;
            case R.id.btnModeSetting:
                break;
            case R.id.btnSignOutSetting:
                break;
        }
    }
}
