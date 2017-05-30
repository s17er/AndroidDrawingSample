package com.s17er.dev.androiddrawingsample;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.canvas)
    CanvasView canvas;

    @Bind(R.id.button_pen)
    ImageButton buttonPen;

    @Bind(R.id.button_eraser)
    ImageButton buttonEraser;

    @Bind(R.id.button_color)
    ImageButton buttonColor;

    private static final int WIDTH_MIN_PX = 4;
    private static final int WIDTH_MAX_PX = 64;
    private int currentProgress = 30;

    private int currentColor = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width/2);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        canvas.setLayoutParams(params);

        canvas.setMode(CanvasView.Mode.DRAW);
        canvas.setDrawer(CanvasView.Drawer.PEN);

    }

    @OnClick(R.id.button_pen)
    public void onClickButtonPen(View v) {
        switch2Pen();
    }

    @OnClick(R.id.button_color)
    public void onClickButtonColor(View v) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                //.initialColor(Color.BLACK)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Log.i("Chase", "onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        currentColor = selectedColor;
                        canvas.setPaintStrokeColor(selectedColor);
                        refreshColorButton();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @OnClick(R.id.button_eraser)
    public void onClickButtonEraser(View v) {
        switch2Eraser();
    }

    @OnClick(R.id.button_backcolor)
    public void onClickButtonBackColor(View v) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                //.initialColor(Color.BLACK)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Log.i("Chase", "onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        canvas.setBaseColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @OnClick(R.id.button_bold)
    public void onClickButtonBold(View v) {
        DialogBoldFragment fragment = new DialogBoldFragment();
        fragment.setCurrentProgress(currentProgress);
        fragment.setListener(new DialogBoldFragment.OnSelectListener() {
            @Override
            public void onChanged(int progress) {
                currentProgress = progress;
                int range = WIDTH_MAX_PX - WIDTH_MIN_PX;
                float px = WIDTH_MIN_PX + (range * currentProgress / 100);
                canvas.setPaintStrokeWidth(px);
            }
        });
        fragment.show(getSupportFragmentManager(), null);
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

    /*
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
    */

    private void switch2Pen() {
        canvas.setMode(CanvasView.Mode.DRAW);
        buttonPen.setBackgroundResource(R.color.button_bg_light);
        buttonEraser.setBackgroundResource(R.color.button_bg_dark);
    }

    private void switch2Eraser() {
        canvas.setMode(CanvasView.Mode.ERASER);
        buttonPen.setBackgroundResource(R.color.button_bg_dark);
        buttonEraser.setBackgroundResource(R.color.button_bg_light);
    }

    private void refreshColorButton() {
        buttonColor.setBackgroundColor(currentColor);
    }

}
