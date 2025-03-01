package StaffScreen.TableDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

import Models.Bill;
import Models.BillInfor;
import Models.Menu;

public class detailAdapter extends ArrayAdapter<BillInfor> {
    private Context context;
    private List<BillInfor> detail = new ArrayList<>();
    public detailAdapter(Context context, List<BillInfor> detail) {
        super(context, 0, detail);
        this.context = context;
        this.detail = detail;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_deltail, parent, false);
        }
        BillInfor billInfor = detail.get(position);
        ImageView itemImage = convertView.findViewById(R.id.imgFood);
        TextView itemName = convertView.findViewById(R.id.tvFoodName);
        TextView itemPrice = convertView.findViewById(R.id.tvPrice);
        TextView itemQuantity = convertView.findViewById(R.id.tvQuantity);

        itemName.setText(billInfor.getMenu().getName());
        itemPrice.setText(String.format("%,.0f VND", billInfor.getPrice()));
        itemQuantity.setText(String.valueOf(billInfor.getQuantity()));
        String imgName = billInfor.getMenu().getImg();
        int imgResId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());

        if (imgResId != 0) {
            itemImage.setImageResource(imgResId);
        } else {
            itemImage.setImageResource(R.drawable.backgrd);
        }

        return convertView;
    }

}
