package StaffScreen.Table;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
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
import Models.Menu;
import Models.Table;
import StaffScreen.Menu.Menu_CallAPI_MainActivity;
import StaffScreen.TableDetail.detail_MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class table_MainActivity extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient();
    private List<Table> tableList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_table_main);

        navigationHelper.setUpNavigation(this);


        callAPI();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void callAPI() {
        String url = "http://10.0.2.2:5009/api/Tables";

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
                    Log.e("Response", "Response: " + responseData);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Table>>() {}.getType();
                    final List<Table> fetchedTableList = gson.fromJson(responseData, listType);

                    runOnUiThread(() -> {
                        tableList.clear();
                        tableList.addAll(fetchedTableList);
                        generateTableLayout(fetchedTableList);
                        //menuAdapter.notifyDataSetChanged();
                    });
                }
            }
        });
    }
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
    private void generateTableLayout(List<Table> tableList) {
        GridLayout tableContainer = findViewById(R.id.tableContainer);
        tableContainer.removeAllViews();

        int columnCount = 2;
        tableContainer.setColumnCount(columnCount);

        for (Table table : tableList) {
            Log.e("Table Data", "tableId:  " + table.isOrder());
            FrameLayout tableFrame = new FrameLayout(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = dpToPx(140);
            params.height = dpToPx(100);
            params.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
            tableFrame.setLayoutParams(params);
            tableFrame.setBackground(getResources().getDrawable(R.drawable.rounded_table));
            tableFrame.setTag("table");

            // Icon bàn
            ImageView tableIcon = new ImageView(this);
            tableIcon.setLayoutParams(new FrameLayout.LayoutParams(dpToPx(60), dpToPx(60), Gravity.CENTER));
            tableIcon.setImageResource(R.drawable.table_icon);

            // Số bàn
            TextView tableNumber = new TextView(this);
            FrameLayout.LayoutParams numberParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
            );
            numberParams.bottomMargin = dpToPx(8);
            tableNumber.setLayoutParams(numberParams);
            tableNumber.setText(String.valueOf(table.getId()));
            tableNumber.setTextSize(16);
            tableNumber.setTextColor(Color.WHITE);

            tableFrame.addView(tableIcon);
            tableFrame.addView(tableNumber);

            // Badge thông báo nếu có
            if (table.isOrder()) {
                ImageView notificationBadge = new ImageView(this);
                FrameLayout.LayoutParams badgeParams = new FrameLayout.LayoutParams(dpToPx(20), dpToPx(20), Gravity.TOP | Gravity.END);
                badgeParams.setMargins(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6));
                notificationBadge.setLayoutParams(badgeParams);
                notificationBadge.setImageResource(R.drawable.bell_icon);
                tableFrame.addView(notificationBadge);
            }

            // Thêm vào GridLayout
            tableContainer.addView(tableFrame);
        }
        tableContainer = findViewById(R.id.tableContainer);

        for (int i = 0; i < tableContainer.getChildCount(); i++) {
            View child = tableContainer.getChildAt(i);
            if ("table".equals(child.getTag())) {
                int index = i;
                child.setOnClickListener(v -> {
                    Table clickedTable = tableList.get(index);
                    Intent intent;
                    if (clickedTable.isOrder()) {
                        intent = new Intent(table_MainActivity.this, detail_MainActivity.class);
                        intent.putExtra("tableId", clickedTable.getId());
                        Log.e("Data", "Table ID: " + clickedTable.getId());
                    } else {
                        intent = new Intent(table_MainActivity.this, Menu_CallAPI_MainActivity.class);
                        intent.putExtra("tableId", clickedTable.getId());
                    }
                    startActivity(intent);
                });
            }
        }

    }

}