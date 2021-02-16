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
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Map;

import anviifotechs.haritkandharadmin.helper.Functions;

import static anviifotechs.haritkandharadmin.HomeActivity.g_p_s1;

public class Plantation_Approve extends AppCompatActivity {

    private ListView DetailsListView;
    private TextView Result_Lable;
    private ArrayList<HashMap<String, String>> JsonList;
    private static final String TAG = Plantation_Approve.class.getSimpleName();
    private ProgressDialog pDialog;
    private String name, email, mobile,address;
    private EditText SearchText;
    private ImageView Search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantation_approve);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DetailsListView = findViewById(R.id.listView_search_result);
        Result_Lable = findViewById(R.id.res_lable);
        SearchText = findViewById(R.id.edtKeyword);
        Search = findViewById(R.id.searchicon);

        JsonList = new ArrayList<>();
        // Progress dialog
        pDialog = new ProgressDialog(Plantation_Approve.this, R.style.MyDialogTheme);
        pDialog.setCancelable(false);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (!isDeviceOnline()) {
            activate_online_device();
        } else {
        ViewUsers();

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_text = SearchText.getText().toString();
                if (search_text.isEmpty()) {
                    Toast.makeText(Plantation_Approve.this, "Type email or mobile number to search", Toast.LENGTH_SHORT).show();
                } else {
                    JsonList.clear();
                    SearchUsers(search_text);
                }
            }
        });
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

    private void SearchUsers(final String search_text) {
        pDialog.setMessage("Searching please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.SEARCH_USERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Log.d(TAG, "Search users Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONArray jsonObject = new JSONArray(response);

                    if (response != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject c = jsonArray.getJSONObject(i);

                            name = c.getString("name");
                            email = c.getString("email");
                            mobile = c.getString("mobile");
                            address = c.getString("address");

                            HashMap<String, String> fetchOrders = new HashMap<>();

                            // adding each child node to HashMap key => value
                            fetchOrders.put("name", name);
                            fetchOrders.put("email", email);
                            fetchOrders.put("mobile", mobile);
                            fetchOrders.put("address", address);

                            JsonList.add(fetchOrders);
                        }
                        hideDialog();
                        SearchText.setText("");
                        Result_Lable.setVisibility(View.GONE);
                    }

                } catch (final Exception e) {
                    JsonList.clear();
                    hideDialog();
                    Result_Lable.setVisibility(View.VISIBLE);
                    ViewUsers();
                    SearchText.setText("");
                    Result_Lable.setText("No users found search again !");
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), JsonList,
                        R.layout.search_plantation_list, new String[]{"name", "email", "mobile", "address"},
                        new int[]{R.id.name, R.id.email, R.id.mobile, R.id.address});

                DetailsListView.setAdapter(adapter);

                DetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final TextView Email = (TextView) view.findViewById(R.id.email);
                        final TextView Name = (TextView) view.findViewById(R.id.name);
                        final TextView Mobile = (TextView) view.findViewById(R.id.mobile);
                        final TextView Address = (TextView) view.findViewById(R.id.address);
                        final TextView Approve = (TextView) view.findViewById(R.id.approve);
                        name = Name.getText().toString();
                        email = Email.getText().toString();
                        mobile = Mobile.getText().toString();
                        address = Address.getText().toString();
                        Approve.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getApplicationContext(), Complete_Plant_Approval.class);
                                i.putExtra("name", name);
                                i.putExtra("email", email);
                                i.putExtra("mobile", mobile);
                                i.putExtra("address", address);
                                startActivity(i);
                            }
                        });
                    }
                });
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
           //     Log.e(TAG, "Reset Password Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", search_text);

                return params;
            }
        };
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void ViewUsers() {

        pDialog.setMessage("Please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.VIEW_PLANTATION_APPROVAL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Log.d(TAG, "View plantation request Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONArray jsonObject = new JSONArray(response);

                    if(response !=null) {
                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject c = jsonArray.getJSONObject(i);

                            name = c.getString("name");
                            email = c.getString("email");
                            mobile = c.getString("mobile");
                            address = c.getString("address");

                            HashMap<String, String> fetchOrders = new HashMap<>();

                            // adding each child node to HashMap key => value
                            fetchOrders.put("name", name);
                            fetchOrders.put("email", email);
                            fetchOrders.put("mobile", mobile);
                            fetchOrders.put("address", address);

                            JsonList.add(fetchOrders);
                        }
                        hideDialog();
                        Result_Lable.setVisibility(View.GONE);
                    }

                } catch (final Exception e) {
                    JsonList.clear();
                    hideDialog();
                    Result_Lable.setVisibility(View.VISIBLE);
                    Result_Lable.setText("No pending approval found try again !");
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), JsonList,
                        R.layout.search_plantation_list, new String[]{"name", "email", "mobile","address"},
                        new int[]{R.id.name,R.id.email,R.id.mobile, R.id.address});

                DetailsListView.setAdapter(adapter);

                DetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final TextView Email = (TextView) view.findViewById(R.id.email);
                        final TextView Name = (TextView) view.findViewById(R.id.name);
                        final TextView Mobile = (TextView) view.findViewById(R.id.mobile);
                        final TextView Address = (TextView) view.findViewById(R.id.address);
                        final TextView Approve = (TextView) view.findViewById(R.id.approve);
                        name = Name.getText().toString();
                        email = Email.getText().toString();
                        mobile = Mobile.getText().toString();
                        address = Address.getText().toString();
                        Approve.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i =new Intent(getApplicationContext(), Complete_Plant_Approval.class);
                                i.putExtra("name",name);
                                i.putExtra("email",email);
                                i.putExtra("mobile",mobile);
                                i.putExtra("address",address);
                                startActivity(i);
                            }
                        });
                    }
                });
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e(TAG, "View plantation request Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}