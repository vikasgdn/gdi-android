package com.gdi.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.api.LogoutRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.fragment.ScoreCardFragment;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.overallFrame)
    FrameLayout contentFrame;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView nav_view;
    public static ActionBarDrawerToggle mDraweToggle;
    Context context;
    View holderView;
    private CustomDialog customDialog;
    private static int nightMode = 0;
    public static final String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //setSupportActionBar(toolbar);
        // setActionBar();
        context = this;
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        holderView = findViewById(R.id.main_content);
        setUpToolBar();

        toolbar.setNavigationIcon(R.drawable.menu_icon);

        getDisplayDimensions();
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        toolbar.setNavigationIcon(R.drawable.menu_icon);
        mDraweToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDraweToggle);

        mDraweToggle.setDrawerIndicatorEnabled(false);
        mDraweToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mDraweToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        mDraweToggle.syncState();
        //toolbar.setNavigationIcon(R.drawable.menu_icon);

        nav_view.setNavigationItemSelectedListener(this);

        if (AppPrefs.getUserRole(context) == 400 || AppPrefs.getUserRole(context) == 300
                || AppPrefs.getUserRole(context) == 280){
            nav_view.getMenu().findItem(R.id.homeNavigate).setVisible(true);
            nav_view.getMenu().findItem(R.id.changePasswordNavigate).setVisible(true);
            nav_view.getMenu().findItem(R.id.standardReportNavigate).setVisible(false);
            nav_view.getMenu().findItem(R.id.profileNavigate).setVisible(true);
            nav_view.getMenu().findItem(R.id.logoutNavigate).setVisible(true);
        }else {
            nav_view.getMenu().findItem(R.id.homeNavigate).setVisible(true);
            nav_view.getMenu().findItem(R.id.changePasswordNavigate).setVisible(true);
            nav_view.getMenu().findItem(R.id.standardReportNavigate).setVisible(true);
            nav_view.getMenu().findItem(R.id.profileNavigate).setVisible(true);
            nav_view.getMenu().findItem(R.id.logoutNavigate).setVisible(true);
        }

        setHomeScreen();
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.homeNavigate) {
            setHomeScreen();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.changePasswordNavigate) {
            startActivity(new Intent(context, ChangePasswordScreen.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.standardReportNavigate) {
            //openStandardReportDialog();
            startActivity(new Intent(context, OpenDialogActivity.class));
        } /*else if (id == R.id.actionPlanNavigate) {
            //startActivity(new Intent(context, ActionPlanActivity.class));
            openCompetetionDialog();
            drawer.closeDrawer(GravityCompat.START);
        }*/else if (id == R.id.profileNavigate) {
            startActivity(new Intent(context, UserProfileActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.logoutNavigate) {
            confirmationLogoutDialog();
            drawer.closeDrawer(GravityCompat.START);
        }


        return true;
    }

    private void setUpToolBar() {
        initToolbar(toolbar);
        //setTitle("Welcome User");
    }

    private void getDisplayDimensions() {
        Display display = getWindowManager().getDefaultDisplay();
        String displayName = display.getName();  // minSdkVersion=17+
        AppLogger.e(TAG, "displayName  = " + displayName);

        // display size in pixels
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        AppLogger.e(TAG, "width        = " + width);
        AppLogger.e(TAG, "height       = " + height);

        AppConstant.boxSize = (int) (width * 0.35);
    }

    private void setHomeScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, new ScoreCardFragment());
        fragmentTransaction.commit();
    }


    private void confirmationLogoutDialog() {
        AlertDialog.Builder dialog = buildAlertDialog(getString(R.string.alert_dialog_logout_title),
                getString(R.string.alert_dialog_logout_msg));

        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // logout();
                AppPrefs.setLoggedIn(context, false);
                AppPrefs.setAccessToken(context, "");
                AppUtils.toast(MainActivity.this, "Logout Successfully");
                AppPrefs.clear(context);
                finish();
                startActivity(new Intent(context, SignInActivity.class));

                FirebaseAuth.getInstance().signOut();
            }
        });

        dialog.show();
    }

    private void logout() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Logout Response: " + response);

                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString(ApiResponseKeys.RES_KEY_MESSAGE);
                    if (object.getString(ApiResponseKeys.RES_KEY_ERROR)
                            .equals(AppConstant.ATTRIBUTE_FALSE)) {
                        AppPrefs.setLoggedIn(context, false);
                        AppPrefs.setAccessToken(context, "");
                        AppUtils.toast(MainActivity.this, message);
                        AppPrefs.clear(context);
                        finish();
                        startActivity(new Intent(context, SignInActivity.class));

                    } else {
                        AppUtils.toast(MainActivity.this, message);
                        if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR) {
                            finish();
                            AppPrefs.setLoggedIn(context, false);
                            AppPrefs.setAccessToken(context, "");
                            AppPrefs.clear(context);
                            startActivity(new Intent(context, SignInActivity.class));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                error.printStackTrace();
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                //serverNotRespondingAlert();
            }
        };


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                LogoutRequest logoutRequest = new LogoutRequest(AppPrefs.getAccessToken(context),task.getResult().getToken(),
                                        stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(MainActivity.this).addToRequestQueue(logoutRequest);

                            }
                        }
                    });
        }

    }
}
