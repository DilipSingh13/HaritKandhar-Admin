package anviifotechs.haritkandharadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import anviifotechs.haritkandharadmin.helper.Functions;

import static anviifotechs.haritkandharadmin.HomeActivity.g_p_s1;

public class Complete_Picture_Approval extends AppCompatActivity {

    ImageView Picture;
    Button btn_Approve, btn_Deny;
    String Name, Email, Mobile, Pic, image_Url, Code;
    private ProgressDialog pDialog;
    ProgressBar progressBar;
    TextView Res_lable;
   // private static final String TAG = Complete_Picture_Approval.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_picture_approval);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_Approve = findViewById(R.id.approve);
        btn_Deny = findViewById(R.id.deny);
        Picture = findViewById(R.id.plant_img);
        Res_lable = findViewById(R.id.res_lable);
        progressBar = findViewById(R.id.ProgressBar);

        btn_Approve.setVisibility(View.INVISIBLE);
        btn_Deny.setVisibility(View.INVISIBLE);
        pDialog = new ProgressDialog(Complete_Picture_Approval.this, R.style.MyDialogTheme);
        pDialog.setCancelable(false);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        if (!isDeviceOnline()) {
            activate_online_device();
        } else {
            if (b != null) {
                Code = (String) b.get("code");
                Name = (String) b.get("name");
                Email = (String) b.get("email");
                Mobile = (String) b.get("mobile");
                Pic = (String) b.get("plant_picture");
            }

            image_Url = "*****ENTER YOUR SERVER URL HERE*****/Plant_Pictures/" + Pic;
            new DownloadTask(Picture)
                    .execute(image_Url);

            btn_Approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approvePlant(Code, Email, Pic);
                }
            });
            btn_Deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    denyPicture(Code, Email, Pic);
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

    public class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
             //   Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            Res_lable.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            btn_Approve.setVisibility(View.VISIBLE);
            btn_Deny.setVisibility(View.VISIBLE);

        }
    }

    private void approvePlant(final String code, final String email, final String pic) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Approving picture user...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.PICTURE_APPROVAL_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            //    Log.d(TAG, "Approve plantation request Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String Msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
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
              //  Log.e(TAG, "Approve plantation request Error: " + error.getMessage(), error);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("code", code);
                params.put("email", email);
                params.put("pic", pic);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void denyPicture(final String code, final String email, final String Picture) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Approving picture user...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.DENY_PICTURE_APPROVAL_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Approve plantation request Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String Msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
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
                //Log.e(TAG, "Approve plantation request Error: " + error.getMessage(), error);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("code", code);
                params.put("email", email);
                params.put("Picture", Picture);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
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