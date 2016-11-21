package by.bsu.fpm.atroschens.lab33;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    public static final int PICK_IMAGE_CODE = 25565;
    private RelativeLayout rootLayout;
//    private Button changeBgBtn;
    private int bgColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = (RelativeLayout) findViewById(R.id.activity_main);
//        changeBgBtn = (Button) findViewById(R.id.choose_bg_btn);

        bgColor = Color.WHITE;
        ColorDrawable rootBg = new ColorDrawable(bgColor);
    }

    public void onChangeBgColorClick(View view) {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.color_chooser);
        SeekBar red = (SeekBar) dialog.findViewById(R.id.red_seek_bar);
        SeekBar green = (SeekBar) dialog.findViewById(R.id.green_seek_bar);
        SeekBar blue = (SeekBar) dialog.findViewById(R.id.blue_seek_bar);

        red.setProgress(Color.red(bgColor));
        green.setProgress(Color.green(bgColor));
        blue.setProgress(Color.blue(bgColor));

        red.setOnSeekBarChangeListener(new OnColorChooserProgressChangedListener());
        green.setOnSeekBarChangeListener(new OnColorChooserProgressChangedListener());
        blue.setOnSeekBarChangeListener(new OnColorChooserProgressChangedListener());

        dialog.setTitle("Choose a colour");
        dialog.show();
    }

    public void updateColor(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        if (seekBar.getId() == R.id.red_seek_bar) {
            bgColor = Color.rgb(
                    progress,
                    Color.green(bgColor),
                    Color.blue(bgColor)
            );
        } else if (seekBar.getId() == R.id.green_seek_bar) {
            bgColor = Color.rgb(
                    Color.red(bgColor),
                    progress,
                    Color.blue(bgColor)
            );
        } else if (seekBar.getId() == R.id.blue_seek_bar) {
            bgColor = Color.rgb(
                    Color.red(bgColor),
                    Color.green(bgColor),
                    progress
            );
        }

        ColorDrawable bg = new ColorDrawable(bgColor);
        rootLayout.setBackground(bg);
    }

    public void onChangeBgImgClick(View view) {
        Intent getIntent = new Intent();

        getIntent.setType("image/*");
        getIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent imageChooser = Intent.createChooser(
                getIntent, "Choose background image");

        try {
            startActivityForResult(
                    imageChooser,
                    PICK_IMAGE_CODE);
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "Please install file manager",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: Make it work.
        switch (requestCode) {
            case PICK_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        MediaStore.Images.Media.getBitmap(
                                getContentResolver(),
                                selectedImage
                        );
                    } catch (IOException e) {
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }

    class OnColorChooserProgressChangedListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            updateColor(seekBar, i, b);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
