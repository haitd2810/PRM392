package Customer;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class Payment_MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_main);

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();

        // Cấu hình WebView
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        // Thiết lập WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains("vnpay_return")) {
                    finish();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });

        // Kiểm tra và load Payment URL
        String paymentUrl = getIntent().getStringExtra("payment_url");
        if (paymentUrl != null) {
            System.out.println("Loading Payment URL: " + paymentUrl);
            webView.loadUrl(paymentUrl);
        } else {
            System.out.println("Payment URL is null!");
        }
    }
}
