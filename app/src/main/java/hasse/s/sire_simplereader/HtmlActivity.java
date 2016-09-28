package hasse.s.sire_simplereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class HtmlActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        WebView webView = (WebView) findViewById(R.id.webView2);
        String location = getIntent().getStringExtra("FileLocation");
        webView.loadUrl("file:///" +location);
    }
}
