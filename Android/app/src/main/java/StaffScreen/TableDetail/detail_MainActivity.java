package StaffScreen.TableDetail;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Common.navigationHelper;
import Models.Bill;
import Models.BillInfor;
import Models.Table;
import StaffScreen.Banking.BankingQR_MainActivity;
import StaffScreen.Menu.Menu_CallAPI_MainActivity;
import StaffScreen.Order.Request.CreateOrderRequest;
import StaffScreen.Order.Request.OrderItems;
import StaffScreen.Table.table_MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class detail_MainActivity extends AppCompatActivity implements detailAdapter.TotalPriceUpdateListener {
    private OkHttpClient client = new OkHttpClient();
    private List<Bill> bill = new ArrayList<>();
    private List<BillInfor> billInfors = new ArrayList<>();
    private List<BillInfor> listBill = new ArrayList<>();
    private detailAdapter detailAdapter;
    private ListView lsView;
    private TextView tvTotal;
    private double total = 0.0;
    private ImageView addNewMeal;
    int tableId = 0;
    private Button btnCashDetail, btnBankingDetail;
    @Override
    public void onTotalPriceUpdated(double total) {
        runOnUiThread(() -> tvTotal.setText(String.format("Total: %,.0f VND", total)));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_main);
        tableId = getIntent().getIntExtra("tableId", -1);
        lsView = findViewById(R.id.lsOrderViewDetail);
        tvTotal = findViewById(R.id.tvTotalDetail);
        addNewMeal = findViewById(R.id.addOrderButtonDetail);
        btnCashDetail = findViewById(R.id.btnCashDetail);
        btnBankingDetail = findViewById(R.id.btnBankingDetail);
        detailAdapter = new detailAdapter(this, billInfors, tableId, this);
        lsView.setAdapter(detailAdapter);
        callAPI();

        navigationHelper.setUpNavigation(this);
        addNewMeal.setOnClickListener(v -> {
            Intent intent = new Intent(this, Menu_CallAPI_MainActivity.class);
            intent.putExtra("tableId", tableId);
            startActivity(intent);
        });
        btnCashDetail.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Confirm that you have received the cash?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        callAPIPut(bill.get(0).getId());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        btnBankingDetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, BankingQR_MainActivity.class);
            intent.putExtra("BillId", bill.get(0).getId());
            intent.putExtra("totalPrice", Double.parseDouble(tvTotal.getText().toString().split(" ")[1].replace(",", "")));
            startActivity(intent);
        });

    }
    private void callAPI() {
        String url = "http://10.0.2.2:5009/api/TableDetails/"+tableId;

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
                    Log.e("ResponseBill", "Response: " + responseData);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Bill>>() {}.getType();
                    List<Bill> fetchedTableDetailList = gson.fromJson(responseData, listType);

                    runOnUiThread(() -> {
                        if (fetchedTableDetailList != null && !fetchedTableDetailList.isEmpty()) {
                            bill.clear();
                            bill.addAll(fetchedTableDetailList);
                            billInfors.addAll(bill.get(0).getBillInfors());
                            for(BillInfor b : billInfors){
                                total += b.getPrice();
                            }
                            tvTotal.setText(String.format("Total: %,.0f", total));
                            Log.e("API_DONE", "API Fetched: " + billInfors.size());
                            if (detailAdapter != null) {
                                detailAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("API_ERROR", "API trả về danh sách rỗng");
                        }
                    });

                }
            }
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
                        Intent intent = new Intent(detail_MainActivity.this, table_MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    Log.e("API_ERROR", "Response error Close Bill: " + response.code());
                }
            }
        });
    }
}