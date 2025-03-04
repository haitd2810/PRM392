package StaffScreen.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;

import java.util.List;

import Models.Order;
import StaffScreen.Menu.MenuAdapter;

public class OrderAdapter extends ArrayAdapter<Order> {
    private Context context;
    private List<Order> orderList;
    public OrderAdapter(Context context, List<Order> orderList) {
        super(context, 0, orderList);
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        }

        Order order = orderList.get(position);

        ImageView itemImage = convertView.findViewById(R.id.imgFoodOrder);
        TextView itemName = convertView.findViewById(R.id.tvFoodNameOrder);
        TextView itemPrice = convertView.findViewById(R.id.tvPriceOrder);
        TextView itemQuantity = convertView.findViewById(R.id.tvQuantityOrder);
        Button btnDecrease = convertView.findViewById(R.id.btnDecreaseOrder);
        Button btnIncrease = convertView.findViewById(R.id.btnIncreaseOrder);

        itemName.setText(order.getMenu().getName());
        itemPrice.setText(String.format("%,.0f VND", (order.getMenu().getPrice() * order.getQuantity())));
        itemQuantity.setText(String.valueOf(order.getQuantity()));

        String imgName = order.getMenu().getImg();
        int imgResId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());

        if (imgResId != 0) {
            itemImage.setImageResource(imgResId);
        } else {
            itemImage.setImageResource(R.drawable.backgrd);
        }

        btnDecrease.setOnClickListener(v -> {
            if (order.getQuantity() > 1) {
                order.setQuantity(order.getQuantity() - 1);
            } else {
                MenuAdapter.menuOrder.removeIf(x -> x.getTableId() == order.getTableId()
                        && x.getMenu().getId() == order.getMenu().getId());
                orderList.remove(position);
            }
            notifyDataSetChanged();
        });

        btnIncrease.setOnClickListener(v -> {
            order.setQuantity(order.getQuantity() + 1);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
