package com.example.testing.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;

public class MainActivity extends AppCompatActivity {
    private static final int TAKE_PHOTO_CODE = 1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.ls_select_pic_image);
    }

    public void takePicture(View view){
       // Intent intent = new Intent(this, CameraActivity.class);
       //startActivity(intent);
        // the surface where the preview will be displayed

        AnncaConfiguration.Builder videoLimited = new AnncaConfiguration.Builder(this, TAKE_PHOTO_CODE);
        videoLimited.setMediaAction(AnncaConfiguration.MEDIA_ACTION_PHOTO);

      //  AnncaConfiguration.Builder builder = new AnncaConfiguration.Builder(this, TAKE_PHOTO_CODE);
        new Annca(videoLimited.build()).launchCamera();



    }

    public void takePicture2(View view){
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/test/";
        File newdir = new File(dir);
        newdir.mkdirs();

        String filePath = dir + "selectedimage.jpg";
        File newfile = new File(filePath);
        try {
            newfile.createNewFile();
        }
        catch (IOException e) {}

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO_CODE) {
            String filePath = data.getStringExtra(AnncaConfiguration.Arguments.FILE_PATH);
            bitmap = BitmapFactory.decodeFile(filePath);

        }
        if (bitmap != null) {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            if (height > width) {
                width = (640 * width) / height;
                height = 640;

            }
            else {
                height = (640 * height) / width;
                width = 640;
            }


            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

            imageView.setImageBitmap(bitmap);

        }
    }
}
