package com.ikalangirajeev.tsgovtebooks;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private EbookAdapter mAdapter;
    private Toolbar toolbar;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Leave Rules");
        toolbar.setSubtitle(" - by Kalangi Rajeev");
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        //Setup and load BannerAd
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9221045649279850~1355660499");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setTranslationY(-3000);
        adView.animate().translationYBy(3000).setDuration(3000);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        toolbar.setTitle("Leave Rules");
        ArrayList<EbookItem> eBookList = null;
        MyAsycTask myAsycTask = new MyAsycTask(this);
        try {
            eBookList = myAsycTask.execute("leave_rules.xml").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        mAdapter = new EbookAdapter(this, eBookList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setTranslationX(-1500);
        mRecyclerView.animate().translationXBy(1500).setDuration(1000);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_copy) {
           /* clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(MainActivity.this, "Text copied :" + clipData.toString(), Toast.LENGTH_SHORT).show();
           */
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.leave_rules) {
            toolbar.setTitle("Leave Rules");
            ArrayList<EbookItem> eBookList = null;
            MyAsycTask myAsycTask = new MyAsycTask(this);
            try {
                eBookList = myAsycTask.execute("leave_rules.xml").get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            mAdapter = new EbookAdapter(this, eBookList);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setTranslationX(-1500);
            mRecyclerView.animate().translationXBy(1500).setDuration(1000);
        } else if (id == R.id.apdss) {
            toolbar.setTitle("Conduct Rules");
            ArrayList<EbookItem> eBookList = null;
            MyAsycTask myAsycTask = new MyAsycTask(this);
            try {
                eBookList = myAsycTask.execute("conduct_rules.xml").get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            mAdapter = new EbookAdapter(this, eBookList);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setTranslationX(-1500);
            mRecyclerView.animate().translationXBy(1500).setDuration(1000);
        } else if (id == R.id.conduct_rules) {
            toolbar.setTitle("APDSS");
            ArrayList<EbookItem> eBookList = null;
            MyAsycTask myAsycTask = new MyAsycTask(this);
            try {
                eBookList = myAsycTask.execute("apdss.xml").get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            mAdapter = new EbookAdapter(this, eBookList);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setTranslationX(-1500);
            mRecyclerView.animate().translationXBy(1500).setDuration(1000);
        } else if (id == R.id.service_rules) {
            toolbar.setTitle("Service Rules");
            ArrayList<EbookItem> eBookList = null;
            MyAsycTask myAsycTask = new MyAsycTask(this);
            try {
                eBookList = myAsycTask.execute("service_rules.xml").get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            mAdapter = new EbookAdapter(this, eBookList);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setTranslationX(-1500);
            mRecyclerView.animate().translationXBy(1500).setDuration(1000);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static class MyAsycTask extends AsyncTask<String, Void, ArrayList<EbookItem>> {
        private static final String TAG = "MyAsycTask";
        private WeakReference<MainActivity> activityWeakReference;
        ArrayList<EbookItem> eBookList;
        String string;

        MyAsycTask(MainActivity mainActivity) {
            activityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity mainActivity = activityWeakReference.get();
            if (mainActivity == null || mainActivity.isFinishing()) {
                return;
            }
        }

        @Override
        protected ArrayList<EbookItem> doInBackground(String... strings) {
            MainActivity mainActivity = activityWeakReference.get();
            eBookList = new ParseXmlFile(mainActivity, strings[0]).getParsedList();
            string = strings[0];
            return eBookList;
        }

        @Override
        protected void onPostExecute(ArrayList<EbookItem> ebookItems) {
            super.onPostExecute(ebookItems);
            MainActivity mainActivity = activityWeakReference.get();
            if (mainActivity == null || mainActivity.isFinishing()) {
                return;
            }
            Log.d(TAG, "onPostExecute: Data Parsed by DOM Parser for..." + string);
            //Toast.makeText(mainActivity, TAG + " is executed in background for..." + string, Toast.LENGTH_LONG).show();
        }
    }

    /*private static class FilterAsyncTask extends AsyncTask<String, Void, Void> {
        private WeakReference<MainActivity> activityWeakReference;

        public FilterAsyncTask(MainActivity mainActivity) {
            activityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected Void doInBackground(String... strings) {
            MainActivity mainActivity = activityWeakReference.get();
            mainActivity.mAdapter.getFilter().filter(strings[0]);
            mainActivity.mAdapter.notifyDataSetChanged();
            return null;
        }
    }*/

}
