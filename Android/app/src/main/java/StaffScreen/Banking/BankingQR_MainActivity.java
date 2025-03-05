package StaffScreen.Banking;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.project.R;

import java.io.IOException;
import java.sql.SQLOutput;

import Common.navigationHelper;
import StaffScreen.Table.table_MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BankingQR_MainActivity extends AppCompatActivity {

    ImageView imgBankingQR;
    Button btnBankingConfirm;
    private OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_banking_qr_main);
        imgBankingQR = findViewById(R.id.imgBankingQR);
        btnBankingConfirm = findViewById(R.id.btnBankingConfirm);
        navigationHelper.setUpNavigation(this);
        int billId = getIntent().getIntExtra("BillId", -1);
        double totalPrice = getIntent().getDoubleExtra("totalPrice", -1);
        String accountName = "TA DUY HAI";
        String encodedAccountName = Uri.encode(accountName);

        String qrUrl = "https://img.vietqr.io/image/tpb-00396925536-compact2.png?amount=" + totalPrice + "&accountName=" + encodedAccountName;
        Glide.with(this)
                .load(qrUrl)
                .into(imgBankingQR);

        btnBankingConfirm.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Confirm that you have received this banking?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        callAPIPut(billId);
                        Intent intent = new Intent(this, table_MainActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
    private void callAPIPut(int billId) {
        String url = "http://10.0.2.2:5009/api/Orders/"+ billId + "/close";


        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create("", null))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API_ERROR", "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("API_SUCCESS", "Response Close Bill: " + responseData);

                    runOnUiThread(() -> {
                        finish();
                    });
                } else {
                    Log.e("API_ERROR", "Response error Close Bill: " + response.code());
                }
            }
        });
    }
}