package com.pixplicity.multiwindowdemo;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class DragActivity extends AppCompatActivity {

    private static final String TAG = DragActivity.class.getSimpleName();

    private ImageButton mIbDrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);

        mIbDrag = (ImageButton) findViewById(R.id.ib_drag);
        mIbDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText(TAG, "Hello from " + TAG);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    // Note that this view allows dragging outside of our app through DRAG_FLAG_GLOBAL
                    view.startDragAndDrop(data, shadowBuilder, view, View.DRAG_FLAG_GLOBAL);
                    view.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    view.setVisibility(View.VISIBLE);
                    return false;
                }
            }
        });
    }

}
