package StaffScreen.Order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Common.navigationHelper;
import Models.Order;
import StaffScreen.Menu.MenuAdapter;
import StaffScreen.Menu.Menu_CallAPI_MainActivity;
import StaffScreen.Order.Request.CreateOrderRequest;
import StaffScreen.Order.Request.OrderItems;
import StaffScreen.TableDetail.detail_MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class order_MainActivity extends AppCompatActivity {

    private List<Order> itemsOrder = new ArrayList<>();
    private List<OrderItems> orderItems = new ArrayList<>();
    private CreateOrderRequest requestOrder = new CreateOrderRequest();
    private final OkHttpClient client = new OkHttpClient();
    private int tableId;
    private ListView lsOrderView;
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_main);
        lsOrderView = findViewById(R.id.lsOrderView);
        btnOrder = findViewById(R.id.btnOrderMenu);

        tableId = getIntent().getIntExtra("tableId", -1);
        for(Order o : MenuAdapter.menuOrder){
            if(o.getTableId() == tableId){
                itemsOrder.add(o);
            }
        }

        OrderAdapter orderAdapter = new OrderAdapter(this, itemsOrder);
        lsOrderView.setAdapter(orderAdapter);


        navigationHelper.setUpNavigation(this);

        btnOrder.setOnClickListener(v -> {
            Log.d("Data To Call", "API Data Size: " + requestOrder);
            for(Order o : itemsOrder){
                int menuId = o.getMenu().getId();
                int quantity = o.getQuantity();
                double price = o.getMenu().getPrice() * o.getQuantity();
                OrderItems odi = new OrderItems(menuId,quantity,price);
                orderItems.add(odi);
            }
            requestOrder = new CreateOrderRequest(tableId, orderItems);

            Iterator<Order> iterator = MenuAdapter.menuOrder.iterator();
            while (iterator.hasNext()) {
                Order o = iterator.next();
                if (o.getTableId() == tableId) {
                    iterator.remove();
                }
            }
            callAPIPost(requestOrder);

        });

        findViewById(R.id.addOrderButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Menu_CallAPI_MainActivity.class);
            intent.putExtra("tableId", tableId);
            startActivity(intent);
        });
    }

    private void callAPIPost(CreateOrderRequest requestOrder) {
        String url = "http://10.0.2.2:5009/api/Orders";

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("tableId", requestOrder.getTableId());
            for (OrderItems item : requestOrder.getItems()) {  // Lặp qua danh sách items
                JSONObject itemObject = new JSONObject();
                itemObject.put("menuId", item.getMenuId());
                itemObject.put("quantity", item.getQuantity());
                itemObject.put("price", item.getPrice());

                jsonArray.put(itemObject);
            }
            jsonObject.put("items", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .post(body)
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
                    Log.d("API_SUCCESS", "ResponseOrder: " + responseData);

                    runOnUiThread(() -> {
                        Intent intent = new Intent(order_MainActivity.this, detail_MainActivity.class);
                        intent.putExtra("tableId", tableId);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    Log.e("API_ERROR", "Response error Order: " + response.code());
                }
            }
        });
    }
}