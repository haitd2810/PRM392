package StaffScreen.Bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project.R;

import java.util.List;

import Models.Bill;
import Models.BillInfor;

public class BillDetailAdapter extends ArrayAdapter<BillInfor> {
    private Context context;
    private List<BillInfor> billDetailList;

    public BillDetailAdapter(@NonNull Context context, List<BillInfor> billList) {
        super(context, 0, billList);
        this.context = context;
        this.billDetailList = billList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bill_infor, parent, false);
        }

        BillInfor bill = billDetailList.get(position);

        ImageView itemImage = convertView.findViewById(R.id.img_BillDetail);
        TextView itemMenuName = convertView.findViewById(R.id.tvName_BillDetail);
        TextView itemQuantity = convertView.findViewById(R.id.tvQuantity_BillDetail);
        TextView itemPrice = convertView.findViewById(R.id.tvPrice_BillDetail);
        TextView itemTotalPrice = convertView.findViewById(R.id.tvTotalPrice_BillDetail);

        String imgName = bill.getMenu().getImg();
        int imgResId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());

        if (imgResId != 0) {
            itemImage.setImageResource(imgResId);
        } else {
            itemImage.setImageResource(R.drawable.backgrd);
        }
        itemMenuName.setText(String.valueOf(bill.getMenu().getName()));
        itemQuantity.setText("x" + String.valueOf(bill.getQuantity()));
        itemTotalPrice.setText(String.format("%,.0f VND", bill.getPrice()));
        itemPrice.setText(String.format("%,.0f VND", bill.getMenu().getPrice()));


        return convertView;
    }
}
