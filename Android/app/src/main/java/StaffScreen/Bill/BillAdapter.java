package StaffScreen.Bill;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;

import java.util.List;

import Models.Bill;
import Models.BillInfor;
import Models.Order;
import StaffScreen.Menu.MenuAdapter;

public class BillAdapter extends ArrayAdapter<Bill> {
    private Context context;
    private List<Bill> billList;
    public BillAdapter(Context context, List<Bill> billList) {
        super(context, 0, billList);
        this.context = context;
        this.billList = billList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        }

        Bill bill = billList.get(position);

        TextView itemtable = convertView.findViewById(R.id.txtTableId_BillItem);
        TextView itemDate = convertView.findViewById(R.id.txtDate_BillItem);
        TextView itemPrice = convertView.findViewById(R.id.txtPrice_BillItem);

        itemtable.setText("Table Number:" + String.valueOf(bill.getTableId()));
        itemDate.setText("Date:" + String.valueOf(bill.getCreateAt()));
        double total = 0;
        for(BillInfor b : bill.getBillInfors()){
            total += b.getPrice();
        }
        itemPrice.setText("Total Price: " + String.format("%,.0f VND", total));

        ImageView btnView = convertView.findViewById(R.id.btnViewDetail_Bill);
        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BillDetail_MainActivity.class);
            intent.putExtra("BillId", bill.getId());
            context.startActivity(intent);
        });

        return convertView;
    }
}
