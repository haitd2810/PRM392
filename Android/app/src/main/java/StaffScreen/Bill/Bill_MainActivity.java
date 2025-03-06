package StaffScreen.Bill;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sourceforge.jtds.jdbc.DateTime;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Common.navigationHelper;
import Models.Bill;
import Models.BillInfor;
import Models.Menu;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Bill_MainActivity extends AppCompatActivity {
    CalendarView calendarView;
    TextView tvTotalBill;
    List<Bill> billList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    BillAdapter billAdapter;
    ListView billView;
    double total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_main);

        calendarView = findViewById(R.id.cld_bill);
        tvTotalBill = findViewById(R.id.tvTotalBill);
        billView = findViewById(R.id.lsBills_bill);
        billAdapter = new BillAdapter(this,billList);
        billView.setAdapter(billAdapter);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateNow = sdf.format(new Date());
        callAPI(dateNow.toString());
        tvTotalBill.setText(String.valueOf(total));

        navigationHelper.setUpNavigation(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                callAPI(formattedDate);

            }
        });
    }
    private void callAPI(String date) {
        Log.e("Calendar Changed", "Changed to " + date);
        String url = "http://10.0.2.2:5009/api/Bills/"+date.replace("/","%2F");

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
                        billAdapter.notifyDataSetChanged();
                        total = 0;
                        for (Bill bill : billList) {
                            for (BillInfor billInfor : bill.getBillInfors()) {
                                total += billInfor.getPrice();
                            }
                        }
                        tvTotalBill.setText(String.format("%,.0f VND", total));
                    });
                }else{
                    Log.e("API Failed", "Data: " + response.body().string());
                }
            }
        });
    }
}