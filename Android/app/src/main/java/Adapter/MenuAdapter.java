package Adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

import Models.Menu;

public class MenuAdapter extends ArrayAdapter<Menu> {

    private Context context;
    private List<Menu> menuList;

    public MenuAdapter(Context context, List<Menu> menuList) {
        super(context, 0, menuList);
        this.context = context;
        this.menuList = menuList;
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
            itemImage.setImageResource(R.drawable.backgrd); // Ảnh mặc định nếu không tìm thấy
        }


        addButton.setOnClickListener(v ->
                Toast.makeText(context, "Đã thêm: " + menu.getName(), Toast.LENGTH_SHORT).show()
        );

        return convertView;
    }
}
