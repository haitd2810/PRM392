package DBContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.DriverManager;
public class DBContext {
    private static final String LOG = "DEBUG";
    private static String ip = "192.168.1.17"; //get by window + R => cmd => ipconfig (get IPV4)
    private static String port = "1433";
    private static String classs = "net.sourceforge.jtds.jdbc.Driver";
    private static String db = "Restaurant";
    private static String un = "sa";
    private static String password = "123456";
    public static Connection connect() {
        Connection conn = null;
        String ConnURL = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip +":"+port+";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
            Log.d("msgSuccess","Connect thanh cong");
        } catch (SQLException e) {
            Log.d(LOG, e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.d(LOG, e.getMessage());
        }
        return conn;
    }

    public void queryData() {
        Connection conn = connect();
        if (conn != null) {
            try {
                Statement statement = conn.createStatement();
                String query = "SELECT * FROM Menu";
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String data = resultSet.getString("name");
                    Log.d("Database", "Data: " + data);
                }

                conn.close();
            } catch (Exception e) {
                Log.e("Database", "Lỗi khi truy vấn: " + e.getMessage());
            }
        }
    }
}
