package StaffScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;

import Common.navigationHelper;

public class table_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_table_main);

        navigationHelper.setUpNavigation(this);

        ViewGroup tableContainer = findViewById(R.id.tableContainer);

        for (int i = 0; i < tableContainer.getChildCount(); i++) {
            View child = tableContainer.getChildAt(i);
            if ("table".equals(child.getTag())) {
                child.setOnClickListener(v -> {
                    Intent intent = new Intent(table_MainActivity.this, Menu_CallAPI_MainActivity.class);
                    startActivity(intent);
                });
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}