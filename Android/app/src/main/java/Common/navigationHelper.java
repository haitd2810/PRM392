package Common;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.project.R;

import StaffScreen.Bill.Bill_MainActivity;
import StaffScreen.profile_MainActivity;
import StaffScreen.Table.table_MainActivity;

public class navigationHelper {
    public static void setUpNavigation(Activity activity){
        activity.findViewById(R.id.nav_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, table_MainActivity.class);
                activity.startActivity(intent);
            }
        });
        activity.findViewById(R.id.nav_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, profile_MainActivity.class);
                activity.startActivity(intent);
            }
        });
        activity.findViewById(R.id.nav_Bill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Bill_MainActivity.class);
                activity.startActivity(intent);
            }
        });
    }
}
