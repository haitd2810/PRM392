package StaffScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;
import com.google.gson.Gson;

import Common.AccountManager;
import Common.navigationHelper;
import Login.login_MainActivity;
import Models.Account;

public class profile_MainActivity extends AppCompatActivity {

    TextView tvName, tvPassword, tvCode, tvRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_main);
        tvName = findViewById(R.id.tvname_profile);
        tvPassword = findViewById(R.id.tvpassword_profile);
        tvCode = findViewById(R.id.tvCode_profile);
        tvRole = findViewById(R.id.tvrole_profile);
        Account account = AccountManager.getInstance().getAccount();
        if (account != null) {
            tvName.setText(account.getUsername());
            tvPassword.setText(account.getPassword());
            tvCode.setText(account.getCode());
            tvRole.setText(account.getRole().getName());
        }

        navigationHelper.setUpNavigation(this);

        findViewById(R.id.btnLogout_Profile).setOnClickListener(v -> {
            AccountManager.getInstance().setAccount(null);
            Intent intent = new Intent(this, login_MainActivity.class);
            startActivity(intent);
        });
    }
}