package Customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Common.AccountManager;
import Customer.Request.MakeReservationRequest;
import Login.login_MainActivity;
import Models.Account;
import Models.Booking;
import Models.Menu;
import StaffScreen.ChangePass.ChangePass_MainActivity;
import StaffScreen.ChangePass.Request.ChangePasswordRequest;
import StaffScreen.Order.Request.CreateOrderRequest;
import StaffScreen.Order.Request.OrderItems;
import StaffScreen.Order.order_MainActivity;
import StaffScreen.TableDetail.detail_MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class customer_MainActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    private Calendar selectedDateTime = Calendar.getInstance();
    TextView txtAccount;
    Button btnMakeReser, btnChangePass, btnLogout;
    CustomerAdapter customerAdapter;
    ListView lsBooking;
    List<Booking> bookingList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_main);
        txtAccount = findViewById(R.id.txtAccount_Information);
        btnMakeReser = findViewById(R.id.btnMakeReservation);
        btnChangePass = findViewById(R.id.btnChangePass_Customer);
        btnLogout = findViewById(R.id.btnLogout_Customer);
        lsBooking = findViewById(R.id.listViewBillInfor_billInfo);
        Account account = AccountManager.getInstance().getAccount();
        txtAccount.setText("Hello " + account.getUsername());
        customerAdapter = new CustomerAdapter(this, bookingList);
        lsBooking.setAdapter(customerAdapter);

        callAPI(account.getId());

        btnMakeReser.setOnClickListener(view -> showReservationDialog());

        btnChangePass.setOnClickListener((view -> showChangePassDialog()));

        btnLogout.setOnClickListener(v -> {
            AccountManager.getInstance().setAccount(null);
            Intent intent = new Intent(this, login_MainActivity.class);
            startActivity(intent);
        });

    }
    private void showChangePassDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_changepassword, null);
        builder.setView(dialogView);
        EditText txtOldPass = dialogView.findViewById(R.id.edit_oldPass_changepass_customer);
        EditText txtNewPass = dialogView.findViewById(R.id.edit_newPassword_changepass_customer);
        EditText txtRepeatPass = dialogView.findViewById(R.id.edit_repeatPass_changepass_customer);

        Button btnChange = dialogView.findViewById(R.id.btnSave_changepass_customer);
        btnChange.setOnClickListener(v -> {
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.setAccountId(AccountManager.getInstance().getAccount().getId());
            request.setOldPass(txtOldPass.getText().toString());
            request.setNewPass(txtNewPass.getText().toString());
            request.setRepeatPass(txtRepeatPass.getText().toString());
            callAPIPut(request.getAccountId(),request);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showReservationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reservation, null);
        builder.setView(dialogView);

        EditText edtFullname = dialogView.findViewById(R.id.edtFullname);
        EditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        TextView txtStartDate = dialogView.findViewById(R.id.txtStartDate);
        TextView txtStartTime = dialogView.findViewById(R.id.txtStartTime);

        // Chọn ngày
        txtStartDate.setOnClickListener(v -> showDatePicker(txtStartDate, txtStartTime));

        // Chọn giờ
        txtStartTime.setOnClickListener(v -> showTimePicker(txtStartTime));

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String fullname = edtFullname.getText().toString();
            String phone = edtPhone.getText().toString();
            String startDate = txtStartDate.getText().toString();
            String startTime = txtStartTime.getText().toString();

            // Lấy thời gian hiện tại
            Calendar now = Calendar.getInstance();

            // Kiểm tra ngày giờ đã chọn
            if (fullname.isEmpty() || phone.isEmpty() || startDate.equals("Select Start Date") || startTime.equals("Select Start Time")) {
                Toast.makeText(customer_MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (selectedDateTime.before(now)) {
                Toast.makeText(customer_MainActivity.this, "Date and time must be in the future!", Toast.LENGTH_SHORT).show();
            }

            MakeReservationRequest request = new MakeReservationRequest();
            request.setFullName(fullname);
            request.setPhone(phone);
            request.setStartDate(startDate + " " + startTime);
            request.setStatus("booked");
            callAPIPost(request);

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDatePicker(TextView txtStartDate, TextView txtStartTime) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year1, month1, dayOfMonth, 0, 0);

            if (selectedDate.before(Calendar.getInstance()) && selectedDate.equals(Calendar.getInstance())) {
                Toast.makeText(customer_MainActivity.this, "Please select a future date!", Toast.LENGTH_SHORT).show();
            } else {
                selectedDateTime.set(year1, month1, dayOfMonth); // Cập nhật ngày đã chọn

                String selectedDateStr = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);

                txtStartDate.setText(selectedDateStr);

                // Nếu ngày hợp lệ nhưng chưa chọn giờ thì reset giờ
                if (txtStartTime.getText().toString().equals("Select Start Time")) {
                    txtStartTime.setText("Select Start Time");
                }
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Ngăn chọn ngày trong quá khứ
        datePickerDialog.show();
    }

    private void showTimePicker(TextView txtStartTime) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH), hourOfDay, minute1);

            if (selectedTime.before(Calendar.getInstance())) {
                Toast.makeText(customer_MainActivity.this, "Please select a future time!", Toast.LENGTH_SHORT).show();
            } else {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute1);
                String selectedTimeStr = String.format("%02d:%02d", hourOfDay, minute1);
                txtStartTime.setText(selectedTimeStr);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
    private void callAPI(int accountId) {
        String url = "http://10.0.2.2:5009/api/Booking/"+accountId;

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
                    Type listType = new TypeToken<List<Booking>>() {}.getType();
                    final List<Booking> fetchedBookingList = gson.fromJson(responseData, listType);

                    runOnUiThread(() -> {
                        bookingList.clear();
                        bookingList.addAll(fetchedBookingList);
                        customerAdapter.notifyDataSetChanged();
                    });
                }
            }
        });
    }
    private void callAPIPost(MakeReservationRequest requestMakeReser) {
        String url = "http://10.0.2.2:5009/api/Booking";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("startDate", requestMakeReser.getStartDate());
            jsonObject.put("status", requestMakeReser.getStatus());
            jsonObject.put("phone", requestMakeReser.getPhone());
            jsonObject.put("fullName", requestMakeReser.getFullName());
            jsonObject.put("accountId", AccountManager.getInstance().getAccount().getId());
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
                if (response.isSuccessful() && response.code() == 200) {
                    String responseData = response.body().string();
                    Log.d("API_SUCCESS", "Response Make Reservation: " + responseData);

                    runOnUiThread(() -> {
                        Intent intent = new Intent(customer_MainActivity.this, customer_MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    runOnUiThread(() ->
                            Toast.makeText(getApplicationContext(), "Error: " + errorBody, Toast.LENGTH_SHORT).show()
                    );
                }
            }
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
                    Log.d("API_SUCCESS", "Response Change pass: " + responseData);

                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Change pass Success! Please login again", Toast.LENGTH_SHORT).show();
                        AccountManager.getInstance().setAccount(null);
                        Intent intent = new Intent(customer_MainActivity.this, login_MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    Log.e("API_ERROR", "Response error Change pass: " + response.code() + " - " + errorBody);

                    runOnUiThread(() ->
                            Toast.makeText(getApplicationContext(), errorBody, Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}