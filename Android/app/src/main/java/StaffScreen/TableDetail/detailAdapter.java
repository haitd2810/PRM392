package StaffScreen.TableDetail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Models.Bill;
import Models.BillInfor;
import Models.Menu;
import StaffScreen.Menu.MenuAdapter;
import StaffScreen.Order.Request.CreateOrderRequest;
import StaffScreen.Order.Request.OrderItems;
import StaffScreen.TableDetail.Request.UpdateDetailRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class detailAdapter extends ArrayAdapter<BillInfor> {
    private OkHttpClient client = new OkHttpClient();
    private Context context;
    private List<BillInfor> detail = new ArrayList<>();
    private int tableId;
    private TotalPriceUpdateListener totalPriceUpdateListener;
    public interface TotalPriceUpdateListener {
        void onTotalPriceUpdated(double total);
    }
    public detailAdapter(Context context, List<BillInfor> detail, int tableId, TotalPriceUpdateListener listener) {
        super(context, 0, detail);
        this.context = context;
        this.detail = detail;
        this.tableId = tableId;
        this.totalPriceUpdateListener = listener;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_deltail, parent, false);
        }
        BillInfor billInfor = detail.get(position);
        ImageView itemImage = convertView.findViewById(R.id.imgFoodDetail);
        TextView itemName = convertView.findViewById(R.id.tvFoodNameDetail);
        TextView itemPrice = convertView.findViewById(R.id.tvPrice);
        TextView itemQuantity = convertView.findViewById(R.id.tvQuantityDetail);
        Button btnDecrease = convertView.findViewById(R.id.btnDecreaseDetail);
        Button btnIncrease = convertView.findViewById(R.id.btnIncreaseDetail);

        itemName.setText(billInfor.getMenu().getName());
        itemPrice.setText(String.format("%,.0f VND", (billInfor.getMenu().getPrice() * billInfor.getQuantity())));
        itemQuantity.setText(String.valueOf(billInfor.getQuantity()));
        String imgName = billInfor.getMenu().getImg();
        int imgResId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());

        if (imgResId != 0) {
            itemImage.setImageResource(imgResId);
        } else {
            itemImage.setImageResource(R.drawable.backgrd);
        }
        btnDecrease.setOnClickListener(v -> {
            if (billInfor.getQuantity() > 0) {
                billInfor.setQuantity(billInfor.getQuantity() - 1);
                UpdateDetailRequest updateDetailRequest = new UpdateDetailRequest();
                updateDetailRequest.setBillInforId(billInfor.getId());
                updateDetailRequest.setQuantity(billInfor.getQuantity());
                updateDetailRequest.setPrice(billInfor.getMenu().getPrice() * billInfor.getQuantity());
                updateDetailRequest.setTableId(tableId);
                callAPIPost(updateDetailRequest);

                if (billInfor.getQuantity() == 0) {
                    detail.remove(position);
                }
                notifyDataSetChanged();
                updateTotal();

            }
        });

        btnIncrease.setOnClickListener(v -> {
            billInfor.setQuantity(billInfor.getQuantity() + 1);
            UpdateDetailRequest updateDetailRequest = new UpdateDetailRequest();
            updateDetailRequest.setBillInforId(billInfor.getId());
            updateDetailRequest.setQuantity(billInfor.getQuantity());
            updateDetailRequest.setPrice(billInfor.getMenu().getPrice() * billInfor.getQuantity());
            callAPIPost(updateDetailRequest);
            updateTotal();
            notifyDataSetChanged();
        });

        return convertView;
    }

    private void callAPIPost(UpdateDetailRequest requestOrder) {
        String url = "http://10.0.2.2:5009/api/Orders/"+requestOrder.getBillInforId();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("billInforId", requestOrder.getBillInforId());
            jsonObject.put("quantity",  requestOrder.getQuantity());
            jsonObject.put("price",  requestOrder.getPrice());
            jsonObject.put("tableId",  requestOrder.getTableId());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
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
                    Log.d("API_SUCCESS", "ResponseUpdateOrder: " + responseData);
                } else {
                    Log.e("API_ERROR", "Response error Update Order: " + response.code());
                }
            }
        });
    }

    private void updateTotal() {
        double total = 0.0;
        for (BillInfor b : detail) {
            total += b.getMenu().getPrice() * b.getQuantity();
        }
        if (totalPriceUpdateListener != null) {
            totalPriceUpdateListener.onTotalPriceUpdated(total);
        }
    }


}
