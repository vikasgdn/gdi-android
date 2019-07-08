package com.gdi.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.StandardReport.ReportLocationCampaignActivity;
import com.gdi.activity.StandardReport.ReportSectionGroupActivity;
import com.gdi.activity.StandardReport.ReportTrendLocationActivity;
import com.gdi.api.LogoutRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.fragment.ScoreCardFragment;
import com.gdi.fragment.AuditFragment;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.CustomDialog;
import com.gdi.utils.CustomTypefaceTextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        holderView = findViewById(R.id.main_content);
        setUpToolBar();

        toolbar.setNavigationIcon(R.drawable.menu_icon);

        getDisplayDimensions();
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        toolbar.setNavigationIcon(R.drawable.menu_icon);
        mDraweToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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


       /* MenuItem reportView = nav_view.getMenu().findItem(R.id.reportNavigate);
        View view = reportView.getActionView();
        TextView report = (TextView)view.findViewById(R.id.menu_text) ;*/


        /*drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawer, float slideOffset) {

                                         holderView.setX(nav_view.getWidth() * slideOffset);
                                         DrawerLayout.LayoutParams lp =
                                                 (DrawerLayout.LayoutParams) holderView.getLayoutParams();
                                         lp.height = drawer.getHeight() -
                                                 (int) (drawer.getHeight() * slideOffset * 0.3f);
                                         lp.topMargin = (drawer.getHeight() - lp.height) / 2;
                                         holderView.setLayoutParams(lp);
                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                     }
                                 }
        );*/

        setHomeScreen();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    private void setStandardReport() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, new AuditFragment());
        fragmentTransaction.addToBackStack(ScoreCardFragment.TAG);
        fragmentTransaction.commit();
    }

    private void openCompetetionDialog() {
        customDialog = new CustomDialog(context, R.layout.competetion_benchmarking_dailog);
        customDialog.setCancelable(false);
        CustomTypefaceTextView tvCityCompset = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_city_compset);
        CustomTypefaceTextView tvGlobal = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_global);
        CustomTypefaceTextView tvCancel = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_cancel);

        tvCityCompset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CompCityCompsetActivity.class));
                customDialog.dismiss();
            }
        });
        tvGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CompGlobalActivity.class));
                customDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();

    }

    private void openStandardReportDialog() {
        customDialog = new CustomDialog(context, R.layout.standard_report_dailog);
        customDialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        customDialog.setCancelable(false);
        CustomTypefaceTextView tvSectionReport = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_section_report);
        CustomTypefaceTextView tvTrendLocation = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_trend_location);
        CustomTypefaceTextView tvLocationCampaign = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_location_campaign);
        CustomTypefaceTextView tvCancel = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_cancel);

        tvSectionReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportSectionGroupActivity.class));
                customDialog.dismiss();
            }
        });
        tvTrendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportTrendLocationActivity.class));
                customDialog.dismiss();
            }
        });
        tvLocationCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportLocationCampaignActivity.class));
                customDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                drawer.openDrawer(GravityCompat.START);
            }
        });
        customDialog.show();

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
                logout();
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
        LogoutRequest logoutRequest = new LogoutRequest(AppPrefs.getAccessToken(context),
                stringListener, errorListener);
        VolleyNetworkRequest.getInstance(MainActivity.this).addToRequestQueue(logoutRequest);
    }
}
