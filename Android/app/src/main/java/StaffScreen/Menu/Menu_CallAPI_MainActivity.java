package StaffScreen.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Common.navigationHelper;
import Models.Menu;
import StaffScreen.Order.order_MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Menu_CallAPI_MainActivity extends AppCompatActivity {
    private ListView listView;
    private MenuAdapter menuAdapter;
    private List<Menu> menuList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    private int tableId;
    private ImageView cartImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_call_api_main);
        listView = findViewById(R.id.menu_list);
        cartImg = findViewById(R.id.cartIcon);
        tableId = getIntent().getIntExtra("tableId", -1);
        menuAdapter = new MenuAdapter(this, menuList, tableId);
        listView.setAdapter(menuAdapter);
        callAPI();
        navigationHelper.setUpNavigation(this);
        cartImg.setOnClickListener(v -> {
            if(menuAdapter.menuOrder.size() != 0){
                Intent intent = new Intent(this, order_MainActivity.class);
                intent.putExtra("tableId", tableId);
                startActivity(intent);
            }
        });
    }
    private void callAPI() {
        String url = "http://10.0.2.2:5009/api/Menu";

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

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Menu>>() {}.getType();
                    final List<Menu> fetchedMenuList = gson.fromJson(responseData, listType);

                    runOnUiThread(() -> {
                        menuList.clear();
                        menuList.addAll(fetchedMenuList);
                        menuAdapter.notifyDataSetChanged();
                    });
                }
            }
        });
    }
}