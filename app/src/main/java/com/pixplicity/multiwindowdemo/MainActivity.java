package com.pixplicity.multiwindowdemo;

import android.content.ClipData;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private View mVgRoot;
    private TextView mTvSize;
    private ImageButton mIbDrag;
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
        mIbDrag = (ImageButton) findViewById(R.id.ib_drag);
        mIbDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText(TAG, "Hello from " + TAG);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    // Note that this view doesn't allow being dragged outside of our app;
                    // to do so, provide DRAG_FLAG_GLOBAL
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    view.setVisibility(View.VISIBLE);
                    return false;
                }
            }
        });
        mLvInfo = (ListView) findViewById(R.id.lv_events);
        mLvInfo.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        view.setBackgroundResource(R.drawable.bg_list_dragged);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        view.setBackgroundResource(R.drawable.bg_list);
                        break;
                    case DragEvent.ACTION_DROP:
                        // Dropped, reassign View to ViewGroup
                        View draggedView = (View) dragEvent.getLocalState();
                        String event = "onDrag " + draggedView;
                        for (int i = 0; i < dragEvent.getClipData().getItemCount(); i++) {
                            event += "\n- " + dragEvent.getClipData().getItemAt(i).getText();
                        }
                        addEvent(event);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        view.setBackgroundResource(R.drawable.bg_list);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mAdapter = new SimpleAdapter(this, sEventList, android.R.layout.simple_list_item_1,
                new String[]{"event"},
                new int[]{android.R.id.text1});
        mLvInfo.setAdapter(mAdapter);

        addEvent("onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        PackageManager pm = getPackageManager();
        menu.findItem(R.id.action_pip).setVisible(pm.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                sEventList.clear();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_pip:
                enterPictureInPictureMode();
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
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        addEvent("onMultiWindowModeChanged: " + isInMultiWindowMode);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        addEvent("onPictureInPictureModeChanged: " + isInPictureInPictureMode);
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
