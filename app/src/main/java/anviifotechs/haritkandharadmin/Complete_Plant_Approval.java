package anviifotechs.haritkandharadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anviifotechs.haritkandharadmin.helper.Functions;

import static anviifotechs.haritkandharadmin.HomeActivity.g_p_s1;

public class Complete_Plant_Approval extends AppCompatActivity {

   // private static final String TAG = Complete_Plant_Approval.class.getSimpleName();
    private ProgressDialog pDialog;
    private String Name, Code, Email, Mobile, count, tree1, tree2, Address;
    private Spinner Count, Option1, Option2;
    TextView txtName, txtEmail, txtMobile, txtOption1, txtOption1Colon, txtOption2, txtOption2Colon;
    Button Sumbit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_plant_approval);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.email);
        txtMobile = findViewById(R.id.mobile);
        Count = findViewById(R.id.count);
        Sumbit = findViewById(R.id.sumbit);
        Option1 = findViewById(R.id.edit_option1);
        Option2 = findViewById(R.id.edit_option2);
        txtOption1 = findViewById(R.id.option1);
        txtOption2 = findViewById(R.id.option2);
        txtOption1Colon = findViewById(R.id.option1colon);
        txtOption2Colon = findViewById(R.id.option2colon);

        // Progress dialog
        pDialog = new ProgressDialog(Complete_Plant_Approval.this, R.style.MyDialogTheme);
        pDialog.setCancelable(false);
        if (!isDeviceOnline()) {
            activate_online_device();
        } else {
            Intent in = getIntent();
            Bundle b = in.getExtras();

            if (b != null) {
                Name = (String) b.get("name");
                Email = (String) b.get("email");
                Mobile = (String) b.get("mobile");
                Address = (String) b.get("address");
            }
            txtName.setText(Name);
            txtEmail.setText(Email);
            txtMobile.setText(Mobile);
            txtEmail.setText(Email);
            List<String> Cnt = new ArrayList<String>();
            Cnt.add("Select Count");
            Cnt.add("1");
            Cnt.add("2");

            Count.setSelection(0);
            ArrayAdapter<String> CountAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Cnt);
            CountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Count.setAdapter(CountAdapter);

            List<String> Tree1 = new ArrayList<String>();
            Tree1.add("Select the tree");
            Tree1.add("आंबा");
            Tree1.add("चिकू");
            Tree1.add("आवळा");
            Tree1.add("जांभूळ");
            Tree1.add("जांब");
            Tree1.add("लिंबू");
            Tree1.add("मोसंबी");
            Tree1.add("संत्रा");
            final List<String> Generictree = new ArrayList<String>();
            Generictree.add("Select the tree");
            Generictree.add("Mango");
            Generictree.add("Chiku");
            Generictree.add("Aawla");
            Generictree.add("Jamun");
            Generictree.add("Jamb");
            Generictree.add("Lemon");
            Generictree.add("Citrus");
            Generictree.add("Orange");
            Option1.setSelection(0);
            ArrayAdapter<String> TreeAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Tree1);
            TreeAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Option1.setAdapter(TreeAdapter1);

            Option1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  //  tree1 = parent.getItemAtPosition(position).toString();
                  tree1=Generictree.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            List<String> Tree2 = new ArrayList<String>();
            Tree2.add("Select the tree");
            Tree2.add("आंबा");
            Tree2.add("चिकू");
            Tree2.add("आवळा");
            Tree2.add("जांभूळ");
            Tree2.add("जांब");
            Tree2.add("लिंबू");
            Tree2.add("मोसंबी");
            Tree2.add("संत्रा");
            Option2.setSelection(0);
            ArrayAdapter<String> TreeAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Tree2);
            TreeAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Option2.setAdapter(TreeAdapter2);

            Option2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 tree2=Generictree.get(position);
                 //   tree2 = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    count = parent.getItemAtPosition(position).toString();
                    if (count.equals("Select Count")) {
                        txtOption1.setVisibility(View.GONE);
                        txtOption1Colon.setVisibility(View.GONE);
                        Option1.setVisibility(View.GONE);
                        txtOption2.setVisibility(View.GONE);
                        txtOption2Colon.setVisibility(View.GONE);
                        Option2.setVisibility(View.GONE);
                    } else if (count.equals("1")) {
                        txtOption1.setVisibility(View.VISIBLE);
                        txtOption1Colon.setVisibility(View.VISIBLE);
                        Option1.setVisibility(View.VISIBLE);
                        txtOption2.setVisibility(View.GONE);
                        txtOption2Colon.setVisibility(View.GONE);
                        Option2.setVisibility(View.GONE);
                    } else {
                        txtOption1.setVisibility(View.VISIBLE);
                        txtOption1Colon.setVisibility(View.VISIBLE);
                        Option1.setVisibility(View.VISIBLE);
                        txtOption2.setVisibility(View.VISIBLE);
                        txtOption2Colon.setVisibility(View.VISIBLE);
                        Option2.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            Sumbit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count.equals("Select Count")) {
                        Toast.makeText(getApplicationContext(), "Select the count !", Toast.LENGTH_SHORT).show();
                    }
                    if (count.equals("1")) {
                        if (tree1.equals("Select the tree")) {
                            Toast.makeText(getApplication(), "Select tree 1 name", Toast.LENGTH_SHORT).show();
                        } else {
                            tree2 = "Not Available";
                            approvePlant(Name, Email, Mobile, count, tree1, tree2);
                        }
                    } else if (count.equals("2")) {
                        if (tree1.contains("Select the tree")) {
                            Toast.makeText(getApplication(), "Select tree 1 name", Toast.LENGTH_SHORT).show();
                        } else if (tree2.equals("Select the tree")) {
                            Toast.makeText(getApplication(), "Select tree 2 name", Toast.LENGTH_SHORT).show();
                        } else {
                            approvePlant(Name, Email, Mobile, count, tree1, tree2);
                        }
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

    private void approvePlant(final String name, final String email, final String mobile, final String count, final String tree1, final String tree2) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Please wait...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.PLANTATION_APPROVAL_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Approve plantation request Response: " + response);
                pDialog.hide();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String Msg = jObj.getString("message");
                        genericdialog("Plant Allocated Status"," Status : "+Msg);
                       // Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_LONG).show();
                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.e(TAG, "Approve plantation request Error: " + error.getMessage(), error);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("count", count);
                params.put("tree1", tree1);
                params.put("tree2", tree2);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void genericdialog(String titel, String msg1) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(titel);
        builder.setMessage(msg1);
        builder.setIcon(R.drawable.logo);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));

            }
        });
        builder.show();
    }
}