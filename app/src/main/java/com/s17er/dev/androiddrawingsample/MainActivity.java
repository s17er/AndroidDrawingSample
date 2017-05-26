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
    CanvasView canvas;

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

        canvas.setMode(CanvasView.Mode.DRAW);
        canvas.setDrawer(CanvasView.Drawer.PEN);

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
                canvas.setPaintStrokeWidth(px);
            }
        });
    }

    @OnClick(R.id.button_color_black)
    public void onClickButtonBlack(View v) {
        canvas.setMode(CanvasView.Mode.DRAW);
        canvas.setPaintStrokeColor(Color.BLACK);
    }
    @OnClick(R.id.button_color_red)
    public void onClickButtonRed(View v) {
        canvas.setMode(CanvasView.Mode.DRAW);
        canvas.setPaintStrokeColor(Color.RED);
    }
    @OnClick(R.id.button_color_blue)
    public void onClickButtonBlue(View v) {
        canvas.setMode(CanvasView.Mode.DRAW);
        canvas.setPaintStrokeColor(Color.BLUE);
    }
    @OnClick(R.id.button_eraser)
    public void onClickButtonEraser(View v) {
        canvas.setMode(CanvasView.Mode.ERASER);
        canvas.setPaintStrokeColor(Color.BLUE);
        //canvas.clearPaintStrokeColor();
    }

    @OnClick(R.id.button_redo)
    public void onClickButtonRedo(View v) {
        canvas.redo();
    }
    @OnClick(R.id.button_undo)
    public void onClickButtonUndo(View v) {
        canvas.undo();
    }
    @OnClick(R.id.button_clear)
    public void onClickButtonClear(View v) {
        canvas.undoAll();
    }

    @OnClick(R.id.button_bg_white)
    public void onClickButtonBgWhite(View v) {
        canvas.setBaseColor(Color.WHITE);
    }
    @OnClick(R.id.button_bg_yellow)
    public void onClickButtonBgYellow(View v) {
        canvas.setBaseColor(Color.YELLOW);
    }
}
