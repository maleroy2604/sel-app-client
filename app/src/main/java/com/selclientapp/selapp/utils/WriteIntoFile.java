package com.selclientapp.selapp.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.selclientapp.selapp.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WriteIntoFile implements Runnable {
    private File image;
    private Uri uri;


    public WriteIntoFile(File image, Uri uri) {
        this.image = image;
        this.uri = uri;
    }

    private OutputStream configFileOutput() {
        try {
            return new FileOutputStream(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(App.context.getContentResolver(), uri);
            Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 300,300, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapResized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeIntoFile() {
        OutputStream fos = configFileOutput();
        try {
            fos.write(getByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        writeIntoFile();
    }
}
