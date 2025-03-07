package Customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.List;

import Common.AccountManager;
import Models.Account;
import Models.Booking;

public class customer_MainActivity extends AppCompatActivity {
    private Calendar selectedDateTime = Calendar.getInstance();
    TextView txtAccount;
    Button btnMakeReser, btnChangePass, btnLogout;
    CustomerAdapter customerAdapter;
    ListView lsBooking;
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
        List<Booking> bookingList = account.getBookings();
        customerAdapter = new CustomerAdapter(this, bookingList);
        lsBooking.setAdapter(customerAdapter);
        btnMakeReser.setOnClickListener(view -> showReservationDialog());

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
            } else {
                Toast.makeText(customer_MainActivity.this, "Reservation made for " + fullname + " at " + startTime + " on " + startDate, Toast.LENGTH_SHORT).show();
            }
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
                String selectedDateStr = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
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
}