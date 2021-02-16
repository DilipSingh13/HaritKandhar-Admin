package anviifotechs.haritkandharadmin.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import anviifotechs.haritkandharadmin.MyApplication;
import anviifotechs.haritkandharadmin.R;
import anviifotechs.haritkandharadmin.helper.Functions;

public class UnblockFragment extends Fragment {

    private ListView DetailsListView;
    private TextView Result_Lable;
    private static final String TAG = UnblockFragment.class.getSimpleName();
    private ArrayList<HashMap<String, String>> JsonList;
    private ProgressDialog pDialog;
    private String name,email,mobile;

    public UnblockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unblock_users, container, false);

        DetailsListView = view.findViewById(R.id.listView_search_result);
        Result_Lable= view.findViewById(R.id.res_lable);

        JsonList = new ArrayList<>();

        // Progress dialog
        pDialog  =new ProgressDialog(getActivity(), R.style.MyDialogTheme);
        pDialog.setCancelable(false);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        ViewUsers();
        return view;
    }
    private void ViewUsers() {

        pDialog.setMessage("Searching please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.VIEW_BLOCK_USERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Search user Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONArray jsonObject = new JSONArray(response);

                    if(response !=null) {
                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject c = jsonArray.getJSONObject(i);

                            name = c.getString("name");
                            email = c.getString("email");
                            mobile = c.getString("mobile");

                            HashMap<String, String> fetchOrders = new HashMap<>();

                            // adding each child node to HashMap key => value
                            fetchOrders.put("name", name);
                            fetchOrders.put("email", email);
                            fetchOrders.put("mobile", mobile);

                            JsonList.add(fetchOrders);
                        }
                        hideDialog();
                        Result_Lable.setVisibility(View.GONE);
                    }

                } catch (final Exception e) {
                    JsonList.clear();
                    hideDialog();
                    Result_Lable.setVisibility(View.VISIBLE);
                    Result_Lable.setText("No blocked user found try again !");
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), JsonList,
                        R.layout.search_users_unblock_list, new String[]{"name", "email", "mobile"}, new int[]{R.id.name,R.id.email,R.id.mobile});

                DetailsListView.setAdapter(adapter);

                DetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final TextView Email = (TextView) view.findViewById(R.id.email);
                        final TextView Name = (TextView) view.findViewById(R.id.name);
                        final TextView Mobile = (TextView) view.findViewById(R.id.mobile);
                        final TextView Unblock = (TextView) view.findViewById(R.id.unblock);
                        name=Name.getText().toString();
                        email = Email.getText().toString();
                        mobile = Mobile.getText().toString();
                        Unblock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UnblockUser(email);
                            }
                        });
                    }
                });
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void UnblockUser(final String email) {
        String tag_string_req = "req_register";

        pDialog.setMessage("Unblocking user...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.UNBLOCK_USERS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Unblock user response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getActivity(),"User unblocked successfully !", Toast.LENGTH_LONG).show();
                        JsonList.clear();
                        ViewUsers();
                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Unblock user Error: " + error.getMessage(), error);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
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