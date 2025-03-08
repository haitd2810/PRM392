package StaffScreen.Menu;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Models.Menu;
import Models.Order;

public class MenuAdapter extends ArrayAdapter<Menu> {

    private Context context;
    private List<Menu> menuList;
    public static List<Order> menuOrder = new ArrayList<>();
    private int tableId;

    public MenuAdapter(Context context, List<Menu> menuList, int tableId) {
        super(context, 0, menuList);
        this.context = context;
        this.menuList = menuList;
        this.tableId = tableId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        }

        Menu menu = menuList.get(position);

        ImageView itemImage = convertView.findViewById(R.id.itemImage);
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemPrice = convertView.findViewById(R.id.itemPrice);
        ImageView addButton = convertView.findViewById(R.id.addButton);

        itemName.setText(menu.getName());
        itemPrice.setText(String.format("%,.0f VND", menu.getPrice()));
        String imgName = menu.getImg();
        int imgResId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());

        if (imgResId != 0) {
            itemImage.setImageResource(imgResId);
        } else {
            itemImage.setImageResource(R.drawable.backgrd);
        }


        addButton.setOnClickListener(v ->{
                Toast.makeText(context, "Added : " + menu.getName(), Toast.LENGTH_SHORT).show();
                if(search(menuOrder, menu,tableId)){
                    int index = getIndex(menuOrder, menu,tableId);
                    List<Order> od = menuOrder.stream().filter(o -> o.getTableId() == tableId
                                    && o.getMenu().getId() == menu.getId())
                            .collect(Collectors.toList());
                    Log.e("Items added", "Data: " + od);
                    menuOrder.get(index).setQuantity( menuOrder.get(index).getQuantity() + 1);
                }else{
                    Log.e("New Items", "New: ");
                    Order order = new Order();
                    order.setQuantity(1);
                    order.setMenu(menu);
                    order.setTableId(tableId);
                    menuOrder.add(order);
                }
            }


        );

        return convertView;
    }
    private boolean search(List<Order> itemOrders, Menu target, int tableId){
        List<Order> od = itemOrders.stream().filter(o -> o.getTableId() == tableId
                                                    && o.getMenu().getId() == target.getId())
                                            .collect(Collectors.toList());

        if(od.size() > 0) return true;

        return false;
    }

    private int getIndex(List<Order> itemOrders, Menu target, int tableId){
        List<Order> od = itemOrders.stream().filter(o -> o.getTableId() == tableId
                        && o.getMenu().getId() == target.getId())
                .collect(Collectors.toList());

        if(od.size() > 0) return IntStream.range(0, itemOrders.size())
                .filter(i -> itemOrders.get(i).getTableId() == tableId
                        && itemOrders.get(i).getMenu().getId() == target.getId())
                .findFirst()
                .orElse(-1);

        return -1;
    }
}
