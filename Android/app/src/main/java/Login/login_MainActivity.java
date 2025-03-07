package Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import Common.AccountManager;
import Customer.customer_MainActivity;
import Models.Account;
import Models.Menu;
import StaffScreen.Table.table_MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class login_MainActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    EditText edEmail,edPassword;
    Button btnLogin;
    public Account account = new Account();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_main);
        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v -> {
            callAPIPost(edEmail.getText().toString(), edPassword.getText().toString());
        });

    }
    private void callAPIPost(String email, String password) {
        String url = "http://10.0.2.2:5009/api/Authen";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
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
                    Log.d("API_SUCCESS", "Response: " + responseData);
                    Gson gson = new Gson();
                    Type Type = new TypeToken<Account>() {}.getType();
                    final Account fetchedAccount = gson.fromJson(responseData, Type);
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Login thành công!", Toast.LENGTH_SHORT).show();
                        account = fetchedAccount;
                        AccountManager.getInstance().setAccount(account);
                        Log.d("Account Role: ", "RoleID: "+ account.getRoleId());
                        if(account.getRoleId() == 3){
                            Intent intent = new Intent(login_MainActivity.this, customer_MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(login_MainActivity.this, table_MainActivity.class);
                            startActivity(intent);

                            finish();
                        }
                    });
                } else {
                    Log.e("API_ERROR", "Response error: " + response.code());
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Login thất bại!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

}