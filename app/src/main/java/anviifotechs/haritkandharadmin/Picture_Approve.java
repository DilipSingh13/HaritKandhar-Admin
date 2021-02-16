package anviifotechs.haritkandharadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import anviifotechs.haritkandharadmin.helper.Functions;

import static anviifotechs.haritkandharadmin.HomeActivity.g_p_s1;

public class Picture_Approve extends AppCompatActivity {

    private ListView DetailsListView;
    private TextView Result_Lable;
    //private static final String TAG = Picture_Approve.class.getSimpleName();
    private ArrayList<HashMap<String, String>> JsonList;
    private ProgressDialog pDialog;
    private String name, email, mobile, plant_picture, month, code, tree_name, date, time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_approve);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DetailsListView = findViewById(R.id.listView_search_result);
        Result_Lable = findViewById(R.id.res_lable);

        JsonList = new ArrayList<>();
        // Progress dialog
        pDialog = new ProgressDialog(Picture_Approve.this, R.style.MyDialogTheme);
        pDialog.setCancelable(false);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (!isDeviceOnline()) {
            activate_online_device();
        } else {
            ViewPicture();
        }
    }

    private void activate_online_device() {
        android.app.AlertDialog.Builder alert112 = new android.app.AlertDialog.Builder(this);
        alert112.setTitle("Network Error");
        alert112.setIcon(R.drawable.ic_baseline_network_check_24);
        alert112.setMessage(g_p_s1);
        alert112.setPositiveButton("Activate Internet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface alert, int which) {
                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivityForResult(settingsIntent, 9003);
                alert.dismiss();
            }
        });
        alert112.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        alert112.show();
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void ViewPicture() {
        pDialog.setMessage("Searching please wait...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.VIEW_PLANT_PICTURE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.d(TAG, "View plantation request Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONArray jsonObject = new JSONArray(response);

                    if (response != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject c = jsonArray.getJSONObject(i);

                            name = c.getString("name");
                            code = c.getString("Unique_code");
                            email = c.getString("email");
                            mobile = c.getString("mobile");
                            month = c.getString("month");
                            tree_name = c.getString("tree_name");
                            date = c.getString("date");
                            time = c.getString("time");
                            plant_picture = c.getString("plant_picture");

                            HashMap<String, String> fetchOrders = new HashMap<>();

                            // adding each child node to HashMap key => value

                            fetchOrders.put("Unique_code", code);
                            fetchOrders.put("name", name);
                            fetchOrders.put("email", email);
                            fetchOrders.put("mobile", mobile);
                            fetchOrders.put("month", month);
                            fetchOrders.put("tree_name", tree_name);
                            fetchOrders.put("date", date);
                            fetchOrders.put("time", time);
                            fetchOrders.put("plant_picture", plant_picture);

                            JsonList.add(fetchOrders);
                        }
                        pDialog.hide();
                        Result_Lable.setVisibility(View.GONE);
                    }

                } catch (final Exception e) {
                    JsonList.clear();
                    pDialog.hide();
                    Result_Lable.setVisibility(View.VISIBLE);
                    Result_Lable.setText("No pending request found try again !");
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), JsonList,
                        R.layout.search_plantation_picture_list, new String[]{"Unique_code", "name", "email", "mobile", "month", "tree_name", "date", "time", "plant_picture"},
                        new int[]{R.id.unique_code, R.id.name, R.id.email, R.id.mobile, R.id.month, R.id.tree_name, R.id.date, R.id.time, R.id.plant_picture});

                DetailsListView.setAdapter(adapter);

                DetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final TextView Email = (TextView) view.findViewById(R.id.email);
                        final TextView Code = (TextView) view.findViewById(R.id.unique_code);
                        final TextView Name = (TextView) view.findViewById(R.id.name);
                        final TextView Mobile = (TextView) view.findViewById(R.id.mobile);
                        final TextView Plant_Picture = (TextView) view.findViewById(R.id.plant_picture);
                        name = Name.getText().toString();
                        email = Email.getText().toString();
                        mobile = Mobile.getText().toString();
                        code = Code.getText().toString();
                        plant_picture = Plant_Picture.getText().toString();
                        //Intent
                        Intent i = new Intent(getApplicationContext(), Complete_Picture_Approval.class);
                        i.putExtra("code", code);
                        i.putExtra("name", name);
                        i.putExtra("email", email);
                        i.putExtra("mobile", mobile);
                        i.putExtra("plant_picture", plant_picture);
                        startActivity(i);
                    }
                });
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
          //      Log.e(TAG, "View plantation request Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}