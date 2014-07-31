package de.hfu.digisign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import AniketaSharma.rohit.R;


public class SignSendFileActivity extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    private Uri uriData = null;
    private String picturePath;
    //private File filePath;
    //private String filename = "lastSignedFile.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_send_file);

        findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("application/image");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Image");
                Uri uri = Uri.parse("file://" + picturePath);
                Log.i("Send" + "", String.valueOf(uriData));
                //String new_path = savefile(uri);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Send via..."));
            }
        });

        /*button_sign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (uriData != null) {
                    signPicture(30, 30, true);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please pick a Picture", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_send_file, menu);
        //openPicture();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) return true;
        if (id == R.id.new_pic_icon) {
            openPicture();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openPicture() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Log.i("Gallery", "Open Gallery!");

        startActivityForResult(i, RESULT_LOAD_IMAGE);
        Log.i("Gallery", "Start new Activity");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            uriData = selectedImage;
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Log.i("open" + "", MediaStore.Images.Media.DATA);

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.i("open", picturePath);
            cursor.close();
            try {
                ExifInterface exif = new ExifInterface(picturePath);
                Log.d("MainActivity", "---Changing the MODEL TAG INITIAL --- " + exif.getAttribute(ExifInterface.TAG_MODEL));
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                exif.setAttribute(ExifInterface.TAG_MODEL, ""+telephonyManager.getDeviceId());
                exif.saveAttributes();
                Log.d("SignSendFileActivity", "--- INITIAL VALUE OF MODEL TAG --- " + exif.getAttribute(ExifInterface.TAG_MODEL));
                Toast.makeText(getApplicationContext(), "Ready to send.", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(getScaledBitmap(picturePath, 800, 800));

            ExifInterface exif2;
            try {
                exif2 = new ExifInterface(picturePath);
                Log.d("MainActivity", "--- TAG MODEL --- " + exif2.getAttribute(ExifInterface.TAG_MODEL));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
