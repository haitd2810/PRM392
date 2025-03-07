package StaffScreen.Bill;

import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Common.navigationHelper;
import Models.Bill;
import Models.BillInfor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BillDetail_MainActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    private List<Bill> billList = new ArrayList<>();
    private List<BillInfor> billInfors = new ArrayList<>();
    private BillDetailAdapter detailAdapter;
    private
    TextView txtId;
    ListView lsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_detail_main);
        txtId = findViewById(R.id.txtBillDetailId_billInfo);
        lsView = findViewById(R.id.listViewBillInfor_billInfo);

        detailAdapter = new BillDetailAdapter(this, billInfors);
        lsView.setAdapter(detailAdapter);
        int billId = getIntent().getIntExtra("BillId",-1);
        txtId.setText("Bill Detail Number: " + String.valueOf(billId));

        callAPI(billId);
        navigationHelper.setUpNavigation(this);
    }
    private void callAPI(int billId) {
        String url = "http://10.0.2.2:5009/api/Bills/"+ billId + "/Detail";

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
                    Type listType = new TypeToken<List<Bill>>() {}.getType();
                    final List<Bill> fetchedBillList = gson.fromJson(responseData, listType);

                    runOnUiThread(() -> {
                        billList.clear();
                        billList.addAll(fetchedBillList);
                        if(billList.size() > 0){
                            billInfors.clear();
                            billInfors.addAll(billList.get(0).getBillInfors());
                            Log.e("Data Bill Infor", "Data Size: " + billInfors.size());
                        }
                        detailAdapter.notifyDataSetChanged();
                    });
                }else{
                    Log.e("API Failed", "Data: " + response.body().string());
                }
            }
        });
    }
}