package anviifotechs.haritkandharadmin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import anviifotechs.haritkandharadmin.Navigation.DrawerAdapter;
import anviifotechs.haritkandharadmin.Navigation.DrawerItem;
import anviifotechs.haritkandharadmin.Navigation.SimpleItem;
import anviifotechs.haritkandharadmin.Navigation.SpaceItem;
import anviifotechs.haritkandharadmin.helper.DatabaseHandler;
import anviifotechs.haritkandharadmin.helper.SessionManager;
import anviifotechs.haritkandharadmin.imageSliderFragment.CustomViewPagerAdapter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, DrawerAdapter.OnItemSelectedListener, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    private SlidingRootNav slidingRootNav;
    private static final int Home = 0;
    private static final int My_Account = 1;
    private static final int Approve_Plantation = 2;
    private static final int Approve_Picture = 3;
    private static final int Block_Unblock = 4;
    private static final int About = 6;
    private static final int Contact_Dev = 7;
    private static final int Exit = 9;
   // private static final String TAG = "HomeActivity";
    private static final long SLIDER_TIMER = 2000;
    private int currentPage = 0;
    private boolean isCountDownTimerActive = false;
    private Handler handler;
    private ViewPager viewPager;
    static String g_p_s1 = "No network is connection is available.";
    private SessionManager session;
    private DatabaseHandler db;
    private HashMap<String, String> user = new HashMap<>();
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private TextView Logout_btn;
    private ProgressDialog pDialog;
    private Toast toast;
    private long lastBackPressTime = 0;
    TextView Lable;
    String Name, Email;
    CardView Block_Unblock_User, approve_Plantation, approve_Picture, DownloadReport;

    private LinearLayout dotsLayout;
    private TextView[] dots;
    private static final String[] LOCATION_AND_READ_WRITE_STORAGE =
            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int RC_LOCATION_AND_READ_WRITE_STORAGE_PERM = 124;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (!isCountDownTimerActive) {
                automateSlider();
            }
            handler.postDelayed(runnable, 2500);

        }
    };

    private void automateSlider() {
        isCountDownTimerActive = true;
        new CountDownTimer(SLIDER_TIMER, 2500) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                int nextSlider = currentPage + 1;
                if (nextSlider == 5) {
                    nextSlider = 0;
                }

                viewPager.setCurrentItem(nextSlider);
                isCountDownTimerActive = false;
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Logout_btn = findViewById(R.id.logout_icon);
        Block_Unblock_User = findViewById(R.id.card_block);
        approve_Plantation = findViewById(R.id.approve_plantation);
        approve_Picture = findViewById(R.id.approve_picture);
        viewPager = findViewById(R.id.view_pager_slider);
        dotsLayout = findViewById(R.id.layoutDots);
        dotsLayout = findViewById(R.id.layoutDots);
        Lable = findViewById(R.id.lable);
        DownloadReport = findViewById(R.id.card_download);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        }

        if (!isDeviceOnline()) {
            activate_online_device();

        } else {
            // Progress dialog
            pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
            pDialog.setCancelable(false);

            db = new DatabaseHandler(getApplicationContext());
            user = db.getUserDetails();

            session = new SessionManager(getApplicationContext());

            Name = user.get("name");
            Email = user.get("email");

            Name = Name.toUpperCase();
            Lable.setText("WELCOME  " + Name);

            if (Email.equals("superadmin@gmail.com")) {
                DownloadReport.setVisibility(View.VISIBLE);
            } else {
                DownloadReport.setVisibility(View.GONE);
            }

            Logout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout_Conformation();
                }
            });

            handler = new Handler();

            handler.postDelayed(runnable, 2000);
            runnable.run();

            CustomViewPagerAdapter viewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(position);
                    if (position == 0) {
                        currentPage = 0;
                    } else if (position == 1) {
                        currentPage = 1;
                    } else if (position == 2) {
                        currentPage = 2;
                    } else if (position == 3) {
                        currentPage = 3;
                    } else {
                        currentPage = 4;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            slidingRootNav = new SlidingRootNavBuilder(this)
                    .withToolbarMenuToggle(toolbar)
                    .withMenuOpened(false)
                    .withContentClickableWhenMenuOpened(true)
                    .withSavedState(savedInstanceState)
                    .withMenuLayout(R.layout.menu_left_drawer)
                    .inject();

            screenIcons = loadScreenIcons();
            screenTitles = loadScreenTitles();

            DrawerAdapter drawadapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(Home).setChecked(true),
                    createItemFor(My_Account),
                    createItemFor(Approve_Plantation),
                    createItemFor(Approve_Picture),
                    createItemFor(Block_Unblock),
                    new SpaceItem(18),
                    createItemFor(About),
                    createItemFor(Contact_Dev),
                    new SpaceItem(18),
                    createItemFor(Exit)));

            drawadapter.setListener(this);
            RecyclerView list = findViewById(R.id.list);
            list.setNestedScrollingEnabled(false);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(drawadapter);

            drawadapter.setSelected(Home);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            Block_Unblock_User.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), BlockUnblockUserActivity.class));
                }
            });

            approve_Plantation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), Plantation_Approve.class));
                }
            });
            approve_Picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), Picture_Approve.class));
                }
            });
            DownloadReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), View_Download.class));
                }
            });
        }
    }

    @AfterPermissionGranted(RC_LOCATION_AND_READ_WRITE_STORAGE_PERM)
    private void allpermission_activate_haritkandhar_app() {
        if (hasLocationAndContactsPermissions()) {
            // Have permissions, do the thing!
            // Toast.makeText(this, "TODO: Permissions", Toast.LENGTH_LONG).show();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_location_contacts),
                    RC_LOCATION_AND_READ_WRITE_STORAGE_PERM,
                    LOCATION_AND_READ_WRITE_STORAGE);
        }
    }

    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_READ_WRITE_STORAGE);
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

    private void logoutUser() {
        db.resetTables();
        session.setLogin(false);
        // Launching the login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.scale, R.anim.scale);
        finish();
    }

    @Override
    public void onItemSelected(int position) {
        if (position == My_Account) {
            allpermission_activate_haritkandhar_app();
            startActivity(new Intent(this, MyAccount.class));
        }
        if (position == Block_Unblock) {
            startActivity(new Intent(this, BlockUnblockUserActivity.class));
        }
        if (position == Approve_Plantation) {
            startActivity(new Intent(this, Plantation_Approve.class));
        }
        if (position == Approve_Picture) {
            startActivity(new Intent(this, Picture_Approve.class));
        }
        if (position == About) {
            startActivity(new Intent(this, AboutUs.class));
        }
        if (position == Contact_Dev) {
            Intent mailIntent = new Intent();
            mailIntent.setAction(Intent.ACTION_SEND);
            mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@anviinfotechs.com"});
            mailIntent.setData(Uri.parse("support@anviinfotechs.com"));
            mailIntent.setPackage("com.google.android.gm");
            mailIntent.setType("text/plain");
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            startActivity(mailIntent);
        }
        if (position == Exit) {
            exit_Conformation();
        }
        slidingRootNav.closeMenu();
    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.navfunt))
                .withTextTint(color(R.color.navfunw))
                .withSelectedIconTint(color(R.color.navfun))
                .withSelectedTextTint(color(R.color.navfuntt));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.Titles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.Icons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
                finish();
            }
            super.onBackPressed();
        }
    }

    private void logout_Conformation() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);

        dialogBuilder.setIcon(R.drawable.logo);
        dialogBuilder.setTitle("Logout");
        dialogBuilder.setMessage("Do you want to logout?");
        dialogBuilder.setCancelable(false);


        dialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // empty
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Conform Delete Action Method
                        logoutUser();
                        dialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void exit_Conformation() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);

        dialogBuilder.setIcon(R.drawable.logo);
        dialogBuilder.setTitle("Exit");
        dialogBuilder.setMessage("Do you want to exit application?");

        dialogBuilder.setCancelable(false);


        dialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // empty
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Conform Delete Action Method
                        System.exit(0);
                        dialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[5];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) ;
        }
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
       // Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
      //  Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
      //  Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
       // Log.d(TAG, "onRationaleDenied:" + requestCode);
    }


}


