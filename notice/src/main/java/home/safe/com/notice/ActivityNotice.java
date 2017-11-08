package home.safe.com.notice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*웹 사용을 위한 임포트*/
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class ActivityNotice extends AppCompatActivity {
    //웹뷰 선언
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //액티비티에 리니어 레이아웃 추가
        //고정된 것들은, 미리 세팅하여 로딩 시키기!!!!
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //웹뷰를 사용하기 위한 초기 세팅
        web = new WebView(this);
        WebSettings webSet = web.getSettings();
        webSet.setJavaScriptEnabled(true);
        webSet.setUseWideViewPort(true );
        webSet.setBuiltInZoomControls(false);
        webSet.setAllowUniversalAccessFromFileURLs(true);
        webSet.setJavaScriptCanOpenWindowsAutomatically(true);
        webSet.setSupportMultipleWindows(true);

        //drecated 대체 할 것을 찾아보기!!!
        webSet.setSaveFormData(false);
        webSet.setSavePassword(false);
        webSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("file:///android_asset/index.html");
        linearLayout.addView(web);
        setContentView(linearLayout);
       // setContentView(R.layout.activity_notice);
    }

    public void onBackPressed(){
        if(web.canGoBack()) web.goBack();
        else finish();
    }
}
