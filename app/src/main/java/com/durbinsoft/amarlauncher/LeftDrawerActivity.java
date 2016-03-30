package com.durbinsoft.amarlauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LeftDrawerActivity extends Activity {

    EditText signatureET;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    ImageView imageView;

    private String signature;
    private String imagePath;

    private String SP_NAME = "AMAR_LAUNCHER_SP";
    private String SP_SIG = "SIGNATURE";
    private String SP_SETT_IMG_PATH = "SETTINGS_IMAGE";

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_drawer_settings);
        initiateSpData();
    }

    private void initiateSpData() {
        imageView = (ImageView) findViewById(R.id.leftSidePicSettings);
        sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        signature = sp.getString(SP_SIG, "Durbin Launcher");
        imagePath = sp.getString(SP_SETT_IMG_PATH, "null");

        signatureET = (EditText) findViewById(R.id.signatureLeftSide);
        signatureET.setText(signature);

        if (imagePath.equals("null")) {
            Bitmap tmpImg = BitmapFactory.decodeResource(getResources(), R.drawable.defaultaddicon);
            imageView.setImageBitmap(tmpImg);
        } else {
            Bitmap bmp = null;
           // BitmapFactory.Options options = new BitmapFactory.Options();
          //  options.inSampleSize = 2;
        //    bmp = BitmapFactory
         //           .decodeFile(imagePath, options);
       //     bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), false);

           bmp = getThumbnail("signatureProPic.png");

            imageView.setImageBitmap(bmp);
        }
    }

    public void cancelClicked(View v) {
        finish();
    }

    public void saveClicked(View v) {
        edit = sp.edit();
        String sign = signatureET.getText().toString();
        if((sign.equals(""))||(sign.equals(null))){
            sign = "Durbin Launcher";
        }
        edit.putString(SP_SIG, sign);
        edit.putString(SP_SETT_IMG_PATH, imagePath);
        edit.commit();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imagePath = picturePath;

            Bitmap bmp = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            bmp = BitmapFactory
                    .decodeFile(picturePath, options);
          //  bmp = Bitmap.createScaledBitmap(bmp, 300, 200, false);

            bmp = Bitmap.createScaledBitmap(bmp,  bmp.getWidth(), bmp.getHeight(), false);

            imageView.setImageBitmap(bmp);
            saveImageToInternalStorage(bmp);

        }


    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        Bitmap bmp;
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image;
        bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);

        parcelFileDescriptor.close();
        image = Bitmap.createScaledBitmap(bmp, 300, 200, false);
        return image;
    }

    public void selectImage(View v) {
        final CharSequence[] items = {"Choose From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Picture!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Choose From Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"), RESULT_LOAD_IMAGE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public boolean saveImageToInternalStorage(Bitmap image) {

        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = openFileOutput("signatureProPic.png", Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Bitmap getThumbnail(String filename) {
        Bitmap thumbnail = null;

        // If no file on external storage, look in internal storage
        if (thumbnail == null) {
            try {
                File filePath = getFileStreamPath(filename);
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
            }
        }
        return thumbnail;
    }

}
