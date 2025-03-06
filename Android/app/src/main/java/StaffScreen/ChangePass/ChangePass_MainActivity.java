package StaffScreen.ChangePass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

import Common.AccountManager;
import Login.login_MainActivity;
import Models.Account;
import StaffScreen.ChangePass.Request.ChangePasswordRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePass_MainActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    EditText editOldPass, editNewPass, editRepeatPass;
    Button btnSave_changepass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_pass_main);
        editOldPass = findViewById(R.id.edit_oldPass_changepass);
        editNewPass = findViewById(R.id.edit_newPassword_changepass);
        editRepeatPass = findViewById(R.id.edit_repeatPass_changepass);
        btnSave_changepass = findViewById(R.id.btnSave_changepass);
        Account account = AccountManager.getInstance().getAccount();
        btnSave_changepass.setOnClickListener(v -> {
            ChangePasswordRequest request = new ChangePasswordRequest(account.getId(), editOldPass.getText().toString(),
                    editNewPass.getText().toString(), editRepeatPass.getText().toString());
            callAPIPut(account.getId(),request);


        });
    }

    private void callAPIPut(int accountId, ChangePasswordRequest requestChange) {
        String url = "http://10.0.2.2:5009/api/ChangePass/"+ accountId;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountId", requestChange.getAccountId());
            jsonObject.put("oldPassword", requestChange.getOldPass());
            jsonObject.put("newPassword", requestChange.getNewPass());
            jsonObject.put("repeatPassword", requestChange.getRepeatPass());
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
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
                    Log.d("API_SUCCESS", "Response Close Bill: " + responseData);

                    runOnUiThread(() -> {
                        AccountManager.getInstance().setAccount(null);
                        Intent intent = new Intent(ChangePass_MainActivity.this, login_MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    Log.e("API_ERROR", "Response error Close Bill: " + response.code() + " - " + errorBody);

                    runOnUiThread(() ->
                            Toast.makeText(getApplicationContext(), errorBody, Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}