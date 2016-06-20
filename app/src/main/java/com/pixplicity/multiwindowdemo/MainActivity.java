package com.pixplicity.multiwindowdemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View mVgRoot;
    private TextView mTvSize;
    private ListView mLvInfo;

    private BaseAdapter mAdapter;
    private static List<HashMap<String, String>> sEventList = new LinkedList<>();

    private View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            mTvSize.setText((int) Math.ceil(right / metrics.density) + "dp × " + (int) Math.ceil(bottom / metrics.density) + "dp");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVgRoot = findViewById(R.id.vg_root);
        mTvSize = (TextView) findViewById(R.id.tv_size);
        mLvInfo = (ListView) findViewById(R.id.lv_events);
        mAdapter = new SimpleAdapter(this, sEventList, android.R.layout.simple_list_item_1,
                new String[]{"event"},
                new int[]{android.R.id.text1});
        mLvInfo.setAdapter(mAdapter);

        addEvent("onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                sEventList.clear();
                mAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addEvent("onStart");

        mVgRoot.addOnLayoutChangeListener(mOnLayoutChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvent("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        addEvent("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        addEvent("onStop");

        mVgRoot.removeOnLayoutChangeListener(mOnLayoutChangeListener);
    }

    @Override
    protected void onDestroy() {
        addEvent("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        addEvent("onConfigurationChanged");
    }

    private void addEvent(String event) {
        HashMap<String, String> map = new HashMap<>();
        map.put("event", event);
        sEventList.add(map);
        mAdapter.notifyDataSetChanged();
    }

}
