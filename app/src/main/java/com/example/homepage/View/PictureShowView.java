package com.example.homepage.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.homepage.R;
import com.example.homepage.Store.Picture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/3.
 */
class PictureShowAdapter extends RecyclerView.Adapter<PictureShowAdapter.PictureHolder> {
    static int MaxPicNum = 6 ;
    public ArrayList<Bitmap> pictures = new ArrayList<Bitmap>() ;
    public int actual_size = 0 ;
    public AppCompatActivity activity = null;
    static final int REQUEST_IMAGE_CAPTURE = 1 ;
    public static final int CHOOSE_PHOTO = 2;
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        activity.startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    public String mCurrentPhotoPath ;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        "com.example.homepage.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }
    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    class PictureHolder extends RecyclerView.ViewHolder{
        ImageView img = null ;
        View view = null ;
        public PictureHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageView) ;
        }
    }
    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_show_single
                , parent , false) ;
        final PictureHolder holder = new PictureHolder(view) ;
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = holder.getAdapterPosition() ;
                if(x == pictures.size()-1) {
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                    } else {
                        openAlbum();
                    }
                }
                else if(x == pictures.size()-2) {
                    if(ContextCompat.checkSelfPermission(activity , Manifest.permission.CAMERA) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);

                    }else {
                        dispatchTakePictureIntent() ;
                    }
                }
                else{
                    /// Show the detail of photograph ;
                }
            }
        });
        return holder ;
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
        holder.img.setImageBitmap(pictures.get(position));
    }

    @Override
    public int getItemCount() {
        if(pictures == null) return 0 ;
        else return pictures.size();
    }
}

public class PictureShowView extends LinearLayout {
    RecyclerView rview = null ;
    PictureShowAdapter adapter = new PictureShowAdapter() ;
    public PictureShowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.picture_show_view , this) ;
        rview = this.findViewById(R.id.recyclerview) ;
        rview.setLayoutManager(new GridLayoutManager(context , 4));
        addExtraPicture();
        rview.setAdapter(adapter);
    }
    ///show function ;
    public PictureShowView setLayoutManager(RecyclerView.LayoutManager lm) {
        rview.setLayoutManager(lm);
        return this ;
    }
    protected void addExtraPicture() {
        __addActualPicture(BitmapFactory.decodeResource(getResources() , R.drawable.camera_icon)) ;
        __addActualPicture(BitmapFactory.decodeResource(getResources() , R.drawable.album_icon)) ;
        adapter.actual_size -= 2 ; /// Nice
    }
    protected void __addActualPicture(Bitmap bitmap) {
        if(adapter.pictures == null) {
            adapter.pictures = new ArrayList<Bitmap>() ;
            adapter.actual_size = 0 ;
        }
        if(adapter.pictures.size() > adapter.actual_size) {
            adapter.pictures.set(adapter.actual_size , bitmap) ;
        }
        else {
            adapter.pictures.add(bitmap) ;
        }
        adapter.actual_size ++ ;
    }

    public PictureShowView setPictureList(ArrayList<Bitmap>pics) {
        adapter.pictures = pics ; adapter.actual_size = pics.size() ;
        addExtraPicture();
        adapter.notifyDataSetChanged();
        return this ;
    }
    public PictureShowView addPicture (Bitmap bitmap) {
        __addActualPicture(bitmap) ;
        addExtraPicture();
        adapter.notifyDataSetChanged();
        return this ;
    }

    public void setActivity(AppCompatActivity activity) {
        adapter.activity = activity ;
    }

    public void onTakePicture() {
        //adapter.galleryAddPic();
        try {
            Bitmap bitmap = Picture.getBitmapFromPath(adapter.mCurrentPhotoPath, width, height) ;
            Toast.makeText(adapter.activity , "图片大小:"+bitmap.getByteCount()/1024+"kb" ,Toast.LENGTH_SHORT).show(); ;
            addPicture(bitmap) ;
        } catch (FileNotFoundException e) {
            Toast.makeText(adapter.activity , "没有找到文件",Toast.LENGTH_SHORT).show(); ;
        } catch (IOException e) {
            Toast.makeText(adapter.activity , "旋转失败",Toast.LENGTH_SHORT).show();
        }
    }
    public int getPictureNumber() {
        return adapter.actual_size ;
    }
    public byte[] getPictureByteArray(int index) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        adapter.pictures.get(index).compress(Bitmap.CompressFormat.JPEG ,10 , baos) ;
        return baos.toByteArray() ;
    }
    public int getPictureOriginOrder(int index) {
        return -1 ;
    }
}
