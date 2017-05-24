package com.s17er.dev.androiddrawingsample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.rm.freedrawview.FreeDrawView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.canvas)
    FreeDrawView canvas;

    @Bind(R.id.seek_width)
    SeekBar widthSeek;

    private static final int WIDTH_MIN_PX = 4;
    private static final int WIDTH_MAX_PX = 64;
    private int currentProgress = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        widthSeek.setProgress(currentProgress);
        widthSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int range = WIDTH_MAX_PX - WIDTH_MIN_PX;
                float px = WIDTH_MIN_PX + (range * currentProgress / 100);
                canvas.setPaintWidthPx(px);
            }
        });
    }

    @OnClick(R.id.button_color_black)
    public void onClickButtonBlack(View v) {
        canvas.setPaintColor(Color.BLACK);
    }
    @OnClick(R.id.button_color_red)
    public void onClickButtonRed(View v) {
        canvas.setPaintColor(Color.RED);
    }
    @OnClick(R.id.button_color_blue)
    public void onClickButtonBlue(View v) {
        canvas.setPaintColor(Color.BLUE);
    }
    @OnClick(R.id.button_redo)
    public void onClickButtonRedo(View v) {
        canvas.redoLast();
    }
    @OnClick(R.id.button_undo)
    public void onClickButtonUndo(View v) {
        canvas.undoLast();
    }
    @OnClick(R.id.button_clear)
    public void onClickButtonClear(View v) {
        canvas.undoAll();
    }
}
