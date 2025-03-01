package StaffScreen.Order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

import Common.navigationHelper;
import Models.Order;
import StaffScreen.Menu.MenuAdapter;
import StaffScreen.Menu.Menu_CallAPI_MainActivity;
import StaffScreen.TableDetail.detail_MainActivity;
import StaffScreen.menu_MainActivity;

public class order_MainActivity extends AppCompatActivity {

    private List<Order> itemsOrder = new ArrayList<>();
    private int tableId;
    private ListView lsOrderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_main);
        lsOrderView = findViewById(R.id.lsOrderView);

        tableId = getIntent().getIntExtra("tableId", -1);
        for(Order o : MenuAdapter.menuOrder){
            if(o.getTableId() == tableId){
                itemsOrder.add(o);
            }
        }
        OrderAdapter orderAdapter = new OrderAdapter(this,itemsOrder);
        lsOrderView.setAdapter(orderAdapter);

        navigationHelper.setUpNavigation(this);

        findViewById(R.id.addOrderButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Menu_CallAPI_MainActivity.class);
            intent.putExtra("tableId", tableId);
            startActivity(intent);
        });

        findViewById(R.id.btnOrder).setOnClickListener( v -> {
            Intent intent = new Intent(this, detail_MainActivity.class);
            startActivity(intent);
        });


    }
}