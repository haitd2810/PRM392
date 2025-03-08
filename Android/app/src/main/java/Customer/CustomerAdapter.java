package Customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project.R;

import java.util.List;

import Models.Bill;
import Models.BillInfor;
import Models.Booking;
import StaffScreen.Bill.BillDetail_MainActivity;
import StaffScreen.TableDetail.detail_MainActivity;

public class CustomerAdapter extends ArrayAdapter<Booking> {
    private Context context;
    private List<Booking> bookList;

    public CustomerAdapter(@NonNull Context context, List<Booking> bookList) {
        super(context, 0, bookList);
        this.context = context;
        this.bookList = bookList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_booking_information, parent, false);
        }

        Booking booking = bookList.get(position);

        TextView itemFullname = convertView.findViewById(R.id.txtFullName_Booking);
        TextView itemPhone = convertView.findViewById(R.id.txtPhone_Booking);
        TextView itemStartDate = convertView.findViewById(R.id.txtStartDate_Booking);
        ImageView imgConfirm = convertView.findViewById(R.id.confirm_booking);

        itemFullname.setText("Full name: " + String.valueOf(booking.getFullName()));
        itemPhone.setText("Phone: " + String.valueOf(booking.getPhone()));
        itemStartDate.setText("Start Date: " + String.valueOf(booking.getStartDate()));
        if(booking.getStatus().equals("confirm")){
            int imgResId = context.getResources().getIdentifier("confirmed", "drawable", context.getPackageName());
            imgConfirm.setImageResource(imgResId);
        }else{
            imgConfirm.setImageDrawable(null);
        }

        LinearLayout item = convertView.findViewById(R.id.bookingitems);
        item.setOnClickListener(v -> {
            Intent intent = new Intent(context, detailBooking_MainActivity.class);
            intent.putExtra("bookId", booking.getId());
            context.startActivity(intent);
        });


//        ImageView btnView = convertView.findViewById(R.id.btnViewDetail_Bill);
//        btnView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, BillDetail_MainActivity.class);
//            intent.putExtra("BillId", bill.getId());
//            context.startActivity(intent);
//        });

        return convertView;
    }
}
