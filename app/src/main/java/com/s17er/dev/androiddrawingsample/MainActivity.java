package com.s17er.dev.androiddrawingsample;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.rm.freedrawview.FreeDrawView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

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

    @OnClick(R.id.button_save)
    public void onClickButtonSave(View v) {
        byte[] bArray = canvas.getBitmapAsByteArray();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sample.bmp");
        try {
            FileOutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(bArray);
            outputStream.flush();

            // 保存した画像を端末に認識させる
            String[] paths = new String[]{file.getPath()};
            MediaScannerConnection.scanFile(this, paths, null, null);
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Chase", e.toString());
        }
    }

    @OnClick(R.id.button_load)
    public void onClickButtonLoad(View v) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sample.bmp");
        try {
            final InputStream inputStream = new FileInputStream(file);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while(true) {
                int len = inputStream.read(buffer);
                if (len < 0) {
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, len);
            }
            canvas.drawBitmap(byteArrayOutputStream.toByteArray());
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Chase", e.toString());
        }
    }

}
