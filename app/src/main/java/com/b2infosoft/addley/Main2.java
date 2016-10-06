package com.b2infosoft.addley;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.addley.fragment.DownLineList;
import com.b2infosoft.addley.global.Global;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.global.Visit;
import com.b2infosoft.addley.fragment.Dashboard;
import com.b2infosoft.addley.fragment.GenerateCommissionT;
import com.b2infosoft.addley.fragment.HowToWork_1;
import com.b2infosoft.addley.fragment.MissingSale;
import com.b2infosoft.addley.fragment.ReferEarn;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class Main2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout isLogin, isLogout, isHeader;
    DrawerLayout drawer;
    NavigationView navigationView;
    View headerView;
    Button login;
    TextView user_name, toolbar_wallet_balance, nav_available_amount;
    ImageView user_image;
    ImageButton toolbar_home, toolbar_wallet, toolbar_search;
    Fragment fragment = null;
    LinearLayout linearLayout_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_home = (ImageButton) toolbar.findViewById(R.id.toolbar_home);
        toolbar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //defaultFragment(new Dashboard());
                defaultFragment(0);
            }
        });
        linearLayout_wallet = (LinearLayout) toolbar.findViewById(R.id.linearLayout_wallet);
        linearLayout_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (com.b2infosoft.addley.global.Login.isLogin(getApplicationContext())) {
                    //defaultFragment(new GenerateCommissionT());
                    defaultFragment(3);
                } else {
                    actionSignIn();
                }
            }
        });
        toolbar_wallet = (ImageButton) toolbar.findViewById(R.id.toolbar_wallet);
        toolbar_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (com.b2infosoft.addley.global.Login.isLogin(getApplicationContext())) {
                    //defaultFragment(new GenerateCommissionT());
                    defaultFragment(3);
                } else {
                    actionSignIn();
                }
            }
        });
        toolbar_wallet_balance = (TextView) toolbar.findViewById(R.id.toolbar_wallet_balance);
        toolbar_wallet_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (com.b2infosoft.addley.global.Login.isLogin(getApplicationContext())) {
                    //defaultFragment(new GenerateCommissionT());
                    defaultFragment(3);
                } else {
                    actionSignIn();
                }
            }
        });

        /*
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_home_white_48dp);
        */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main2, navigationView, false);

        login = (Button) headerView.findViewById(R.id.user_login);
        isHeader = (LinearLayout) headerView.findViewById(R.id.header);
        isHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2.this, Profile.class));
            }
        });
        user_name = (TextView) headerView.findViewById(R.id.user_name);
        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2.this, Profile.class));
            }
        });
        nav_available_amount = (TextView) headerView.findViewById(R.id.nav_available_amount);
        user_image = (ImageView) headerView.findViewById(R.id.user_image);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2.this, Profile.class));
            }
        });
        user_image.setImageResource(com.b2infosoft.addley.global.Login.getUserProfilePic(getBaseContext()));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSignIn();
            }
        });
        if (com.b2infosoft.addley.global.Login.isLogin(this)) {
            new InitialTask().execute();
        }
        setLogin();
        refreshWallet();
        //defaultFragment(new Dashboard());
        defaultFragment(getActive());
    }

    private void refreshWallet() {
        getSupportActionBar().setTitle(null);
        toolbar_wallet_balance.setText(Global.getRupee(this).concat(String.valueOf(com.b2infosoft.addley.global.Login.getWalletAmount(this))));
        nav_available_amount.setText(Global.getRupee(this).concat(String.valueOf(com.b2infosoft.addley.global.Login.getWalletAmount(this))));
    }

    private void closeDrawerMenu() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(Gravity.LEFT);
        }
    }

    private void setLogin() {
        navigationView.removeHeaderView(headerView);
        isLogin = (LinearLayout) headerView.findViewById(R.id.header_is_login);
        isLogout = (LinearLayout) headerView.findViewById(R.id.header_is_logout);
        if (com.b2infosoft.addley.global.Login.isLogin(this)) {
            isLogin.setVisibility(View.VISIBLE);
            isLogout.setVisibility(View.GONE);
            user_name.setText(com.b2infosoft.addley.global.Login.getValue(this, Tag.USER_NAME));
            navigationView.getMenu().setGroupVisible(R.id.menu_3, true);
        } else {
            isLogin.setVisibility(View.GONE);
            isLogout.setVisibility(View.VISIBLE);
            navigationView.getMenu().setGroupVisible(R.id.menu_3, false);
        }
        navigationView.addHeaderView(headerView);
    }

    private void actionSignIn() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setLogin();
        refreshWallet();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setLogin();
        refreshWallet();
    }

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment == null) {
                fragment = new Dashboard();
                defaultFragment(fragment);
            } else {
                defaultFragment(fragment);
            }
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                //super.onBackPressed();

                Log.d(Tag.RATING, "" + com.b2infosoft.addley.global.Login.getRatingStatus(this));

                if (com.b2infosoft.addley.global.Login.getRatingStatus(this) == Tag.RATING_DEFAULT) {
                    promptRatingUs();
                    return;
                } else if (com.b2infosoft.addley.global.Login.getRatingStatus(this) == Tag.RATING_REMIND) {
                    Random random = new Random(10);
                    if (random.nextInt() == Tag.RATING_DEFAULT) {
                        promptRatingUs();
                        return;
                    }
                }
                finish();
                return;
            } else {
                Toast.makeText(getBaseContext(), "Double tap to exit", Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    public void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        //myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        myAppLinkToMarket.addFlags(flags);
        try {
            startActivity(myAppLinkToMarket);
            com.b2infosoft.addley.global.Login.setRatingStatus(getApplicationContext(),Tag.RATING_RATE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " Sorry, Not able to open!", Toast.LENGTH_SHORT).show();
        }
    }

    private void promptRatingUs() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Rating us");
        alert.setMessage("If you love our app, please take a movement to rate it");
        alert.setPositiveButton("RATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchMarket();
                dialog.dismiss();
            }
        });
        alert.setNeutralButton("REMIND LATER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                com.b2infosoft.addley.global.Login.setRatingStatus(getBaseContext(), Tag.RATING_REMIND);
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                com.b2infosoft.addley.global.Login.setRatingStatus(getBaseContext(), Tag.RATING_DEFAULT);
                dialog.dismiss();
            }
        });
        alert.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(this, Search.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //Fragment fragment =null;
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            //fragment = new User();
            fragment = new Dashboard();
            setActive(0);
        } else if (id == R.id.nav_profile) {
            if (com.b2infosoft.addley.global.Login.isLogin(getApplicationContext())) {
                startActivity(new Intent(Main2.this, Profile.class));
            } else {
                actionSignIn();
            }
        } else if (id == R.id.nav_how_it_works) {
//            fragment = new HowToWork();
            fragment = new HowToWork_1();
            setActive(1);
        } else if (id == R.id.nav_downline_list) {
            if (com.b2infosoft.addley.global.Login.isLogin(getApplicationContext())) {
                fragment = new DownLineList();
                setActive(2);
            } else {
                actionSignIn();
            }
        } else if (id == R.id.nav_generate_commission) {
            if (com.b2infosoft.addley.global.Login.isLogin(getApplicationContext())) {
                fragment = new GenerateCommissionT();
                setActive(3);
            } else {
                actionSignIn();
            }
        } else if (id == R.id.nav_refer_earn) {
            if (com.b2infosoft.addley.global.Login.isLogin(getApplicationContext())) {
                int i = 1;
                if (i == 0) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, com.b2infosoft.addley.global.Login.getValue(getApplicationContext(), Tag.REFERRAL_LINK));
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                } else {
                    fragment = new ReferEarn();
                    setActive(4);
                }
            } else {
                actionSignIn();
            }
        } else if (id == R.id.nav_report_missing_sale) {
            if (com.b2infosoft.addley.global.Login.isLogin(getApplicationContext())) {
                fragment = new MissingSale();
                setActive(5);
            } else {
                actionSignIn();
            }
        } else if (id == R.id.nav_sign_out) {
            if (com.b2infosoft.addley.global.Login.isLogin(getApplicationContext())) {
                com.b2infosoft.addley.global.Login.setLogOutAll(getApplicationContext());
                setLogin();
                refreshWallet();
                fragment = new Dashboard();
                setActive(0);
            } else {
                actionSignIn();
            }
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void defaultFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void setActive(int flag) {
        Visit.setActiveSupFragment(this, flag);
    }

    private int getActive() {
        return Visit.getLastActiveSupFragment(this);
    }

    private void defaultFragment(int flag) {
        Fragment fragment = null;
        switch (flag) {
            case 0:
                fragment = new Dashboard();
                break;
            case 1:
                fragment = new HowToWork_1();
                break;
            case 2:
                fragment = new DownLineList();
                break;
            case 3:
                fragment = new GenerateCommissionT();
                break;
            case 4:
                fragment = new ReferEarn();
                break;
            case 5:
                fragment = new MissingSale();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private class InitialTask extends AsyncTask<String, Void, JSONObject> {
        private JSONArray object;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Server server = new Server(Urls.getUserAction());
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.USER_ID, com.b2infosoft.addley.global.Login.getValue(Main2.this, Tag.USER_ID));
                data.put(Tag.USER_ACTION, Tag.INITIAL);
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                // Log.d("Response", jsonObject.toString());
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        return jsonObject;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    if (response.has(Tag.INITIAL_WALLET_AMOUNT)) {
                        String commission = response.getString(Tag.INITIAL_WALLET_AMOUNT);
                        com.b2infosoft.addley.global.Login.setWalletAmount(Main2.this, Float.parseFloat(commission.trim()));
                        refreshWallet();
                    }
                    if (response.has(Tag.INITIAL_USER_NAME)) {
                        String commission = response.getString(Tag.INITIAL_USER_NAME);
                        com.b2infosoft.addley.global.Login.setKey(Main2.this, Tag.USER_NAME, commission);
                    }
                    if (response.has(Tag.INITIAL_USER_EMAIL)) {
                        String commission = response.getString(Tag.INITIAL_USER_EMAIL);
                        com.b2infosoft.addley.global.Login.setKey(Main2.this, Tag.USER_EMAIL, commission);
                    }
                    if (response.has(Tag.INITIAL_USER_REFERRAL_CODE)) {
                        String commission = response.getString(Tag.INITIAL_USER_REFERRAL_CODE);
                    }
                } catch (JSONException e) {

                }
            }
        }
    }
}
