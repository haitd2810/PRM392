package Customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import Models.Bill;
import Models.Booking;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class detailBooking_MainActivity extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient();
    private Booking bookDetail = new Booking();
    TextView tvFullname, tvPhone, tvDate, tvNumber, tvStatus;
    Button btnCancel, btnPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_booking_main);
        tvFullname = findViewById(R.id.tvFullname_BookingDetail);
        tvPhone = findViewById(R.id.tvPhone_BookingDetail);
        tvDate = findViewById(R.id.tvStartDate_BookingDetail);
        tvNumber = findViewById(R.id.tvNumberBook_BookingDetail);
        tvStatus = findViewById(R.id.tvStatus_BookingDetail);
        btnCancel = findViewById(R.id.btnCancelBook_Customer);
        btnPayment = findViewById(R.id.btnPayment_Customer);
        callAPI(getIntent().getIntExtra("bookId",-1));

        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure to cancel this?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        callAPIDelete(bookDetail.getId());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("Start Generated Payment URL: ");
                    String paymentUrl = VNPayConfig.getPaymentUrl(200000, String.valueOf(bookDetail.getId()));
                    System.out.println("Generated Payment URL: " + paymentUrl);
                    Intent intent = new Intent(detailBooking_MainActivity.this, Payment_MainActivity.class);
                    intent.putExtra("payment_url", paymentUrl);
                    Log.d("VNPAY_URL", paymentUrl);
                    startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void callAPI(int bookId) {
        String url = "http://10.0.2.2:5009/api/Booking/"+ bookId + "/Detail";

        Request request = new Request.Builder()
                .url(url)
                .get()
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
                    Log.e("API Success", "Data: " + responseData);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<Booking>() {}.getType();
                    final Booking fetchedBook = gson.fromJson(responseData, listType);

                    runOnUiThread(() -> {
                        bookDetail = fetchedBook;
                        tvFullname.setText("Fullname: " + bookDetail.getFullName());
                        tvPhone.setText("Phone: " + bookDetail.getPhone());
                        tvDate.setText("Booking Time: " + bookDetail.getStartDate().split("T")[0]+ " " + bookDetail.getStartDate().split("T")[1]);
                        tvNumber.setText("Number of booking: " + bookDetail.getNumberOfBooking());
                        tvStatus.setText("Status: " + bookDetail.getStatus());
                        if(bookDetail.getStatus().equals("confirm")){
                            tvStatus.setTextColor(Color.parseColor("#0065FF"));
                            TextView tvThanks = findViewById(R.id.thanks);
                            tvThanks.setText("Thank you for using our restaurant service. We look forward to seeing you more in the future!");
                            btnCancel.setVisibility(View.GONE);
                        }
                    });
                }else{
                    Log.e("API Failed", "Data: " + response.body().string());
                }
            }
        });
    }

    private void callAPIDelete(int bookId) {
        String url = "http://10.0.2.2:5009/api/Booking/"+ bookId;

        Request request = new Request.Builder()
                .url(url)
                .delete()
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
                    Log.e("API Success", "Data: " + responseData);

                    runOnUiThread(() -> {
                        Intent intent = new Intent(detailBooking_MainActivity.this, customer_MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }else{
                    Log.e("API Failed", "Data: " + response.body().string());
                }
            }
        });
    }


}