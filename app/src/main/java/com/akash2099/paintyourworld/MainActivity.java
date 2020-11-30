package com.akash2099.paintyourworld;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MyPaintView paintView;
    //    Button toggleScroll;
    Button colorPicker;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        paintView=new MyPaintView(this,null);
//        setContentView(paintView);
        setContentView(R.layout.activity_main);
        System.out.println("hi 1");
        paintView = (MyPaintView) findViewById(R.id.paint_view);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

//        toggleScroll=(Button)findViewById(R.id.toggle_scroll);

        colorPicker = (Button) findViewById(R.id.colorPicker);
        // change shape color programatically left
//        Drawable d=colorPicker.getBackground();
        colorPicker.setBackgroundColor(paintView.get_color());
//        colorPicker.setBackground(d);


        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(25);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                int pp=seekBar.getProgress()/40;
//                pp+=10;
//                paintView.set_stroke_size(pp);
//                Toast.makeText(getApplicationContext(),"Brush size: "+pp, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int pp = seekBar.getProgress() * 40 / 100;
                pp += 10;
                paintView.set_stroke_size(pp);
                Toast.makeText(getApplicationContext(), "Brush size: " + pp, Toast.LENGTH_SHORT).show();
            }
        });
    }
/*

    public void toggle_Scroll_button_onclick(View view) {
        if(toggleScroll.getText().toString().equalsIgnoreCase("Scroll")){
            toggleScroll.setText("Draw");
            paintView.activate_scroll();
        }
        else{
            toggleScroll.setText("Scroll");
            paintView.normal();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        invalidateOptionsMenu();

        switch (item.getItemId()) {
            case R.id.save:
                CheckUserPermsions();
                return true;
            case R.id.undo:
                paintView.onClickUndo();
                return true;
            case R.id.redo:
                paintView.onClickRedo();
                return true;
//            case R.id.normal:
//                System.out.println("Hi normal");
//                Drawable icon_menu=item.getIcon();
//                Drawable blur_on=getDrawable(R.drawable.ic_blur_on_black_24dp);
//                Drawable blur_off=getDrawable(R.drawable.ic_blur_off_black_24dp);
//                if(icon_menu==blur_off){
//                    System.out.println("Hi blur mode");
//                    paintView.blur();
//                    item.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_blur_on_black_24dp));
//                }
//                else{
//                    System.out.println("Hi normal mode");
//                    paintView.normal();
////                    invalidateOptionsMenu();
//                    item.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_blur_off_black_24dp));
//                }
//                return true;
//            case R.id.emboss:
//                paintView.emboss();
//                return true;
            case R.id.normal:
                paintView.normal();
                return true;
            case R.id.blur:
                paintView.blur();
                return true;
            case R.id.clear:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete");

                // Setting Dialog Message
                alertDialog.setMessage("Clear the whole canvas?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_clear_black_24dp);

                // Setting Positive "Yes" Btn
                alertDialog.setPositiveButton("CLEAR",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                paintView.clear();
                            }
                        });
                // Setting Negative "NO" Btn
                alertDialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
//                            Toast.makeText(getApplicationContext(),
//                                    "You clicked on NO", Toast.LENGTH_SHORT)
//                                    .show();
                                dialog.cancel();
                            }
                        });

                // Showing Alert Dialog
                alertDialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void color_picker_dialog(View view) {
        int currentBackgroundColor = paintView.get_color();

        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        Toast.makeText(MainActivity.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("SET", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        changeBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    void changeBackgroundColor(int change_color) {
        paintView.set_color(change_color);
        colorPicker.setBackgroundColor(change_color);
//        Toast.makeText(MainActivity.this, "Color changed to : " + change_color, Toast.LENGTH_SHORT).show();
    }

    //access to permsions
    void CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

        save_image_to_galary();// function

    }

    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    save_image_to_galary();// function
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Give write permission for saving image to galary", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    void save_image_to_galary() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Save");

        // Setting Dialog Message
        alertDialog.setMessage("Save the image to Galary?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_file_download_black_24dp);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        System.out.println("hi 2");
                        try {
                            System.out.println("created 1");
                            download_view_screenshot();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(),
                                "Saved to Galary", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
//                            Toast.makeText(getApplicationContext(),
//                                    "You clicked on NO", Toast.LENGTH_SHORT)
//                                    .show();
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
    }


    void download_view_screenshot() throws IOException {
        // taking screenshot
        Bitmap bitmap;
//        View v1 = findViewById(R.id.paint_view);// get ur root view id
        View v1 = paintView;// get ur root view id
        v1.setDrawingCacheEnabled(true);
//        Bitmap bm = v1.getDrawingCache();
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);


        // saving to sdcard
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        System.out.println("created");
        String folder_main = "PaintYourWorld";
        File outerFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM", folder_main);
        if (!outerFolder.exists()) {
            outerFolder.mkdirs();
            System.out.println("FOlder created");
        }
        System.out.println("created 5");
        String resultPath = Environment.getExternalStorageDirectory() + "/DCIM/" + folder_main + "/Paint_" + System.currentTimeMillis() + ".jpg";
        File inerDire = new File(resultPath);

        System.out.println("created 4");


        System.out.println("created 3");
        inerDire.createNewFile();
        FileOutputStream fo = new FileOutputStream(inerDire);
        fo.write(bytes.toByteArray());
        fo.close();
        System.out.println(System.currentTimeMillis() + " File created");

//        ContentResolver resolver = getContentResolver();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
//        String path = String.valueOf(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));


        // Best Solution works both for API 29 and below
        MediaScannerConnection.scanFile(this, new String[]{inerDire.getPath()}, new String[]{"image/jpeg"}, null);

//
//            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri contentUri = Uri.fromFile(inerDire);
//            mediaScanIntent.setData(contentUri);
//            this.sendBroadcast(mediaScanIntent);
//
//
//            ContentValues values = new ContentValues();
//            values.put(MediaStore.Images.Media.TITLE, "Photo");
//            values.put(MediaStore.Images.Media.DESCRIPTION, "Edited");
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
//            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
////            values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
////            values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
//            values.put("_data", resultPath);
//
//            ContentResolver cr = getContentResolver();
//            cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        System.out.println(path+ " " +System.currentTimeMillis() + " File created and saved to galary");
        System.out.println(" " + System.currentTimeMillis() + " File created and saved to galary");

//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//            {
////                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////                File f = new File("file://"+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
//////                File f = new File("file://"+ Environment.getExternalStorageDirectory());
////                File file = ..... // Save file
////                Intent i;
//                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(inerDire)));
////
////                Uri contentUri = Uri.fromFile(f);
////                mediaScanIntent.setData(contentUri);
////                this.sendBroadcast(mediaScanIntent);
//            }
//            else
//            {
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
//            }


    }
}
