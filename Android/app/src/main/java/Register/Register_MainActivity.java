package Register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import Login.login_MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register_MainActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    TextView tvLogin;
    EditText edUsername, edPassword, edRepeatPassword;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_main);
        tvLogin = findViewById(R.id.tvLogin);
        edUsername = findViewById(R.id.ed_username_register);
        edPassword = findViewById(R.id.ed_password_register);
        edRepeatPassword = findViewById(R.id.ed_repeatPass_register);
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, login_MainActivity.class);
            startActivity(intent);
        });
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            callAPIPost(edUsername.getText().toString(), edPassword.getText().toString(), edRepeatPassword.getText().toString());
        });
    }
    private void callAPIPost(String email, String password, String repeatPass) {
        String url = "http://10.0.2.2:5009/api/Account";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", email);
            jsonObject.put("password", password);
            jsonObject.put("repeatPassword", password);
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
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Register Success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register_MainActivity.this, login_MainActivity.class);
                        startActivity(intent);
                    });
                } else {
                    runOnUiThread(() -> {
                        String errorBody = null;
                        try {
                            errorBody = response.body() != null ? response.body().string() : "Unknown error";
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("API_Failed", "Response: " + errorBody);
                        String finalErrorBody = errorBody;
                        runOnUiThread(() ->
                                Toast.makeText(getApplicationContext(), "Error: " + finalErrorBody, Toast.LENGTH_SHORT).show()
                        );
                    });
                }
            }
        });
    }
}