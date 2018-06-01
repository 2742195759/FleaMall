package com.example.homepage.View;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.example.homepage.Account;
import com.example.homepage.R;
import com.example.homepage.Store.Picture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/3.
 */

class PictureShowAdapter extends RecyclerView.Adapter<PictureShowAdapter.PictureHolder> {
    static int MaxPicNum = 6 ;
    public static ArrayList<Picture> pictures = new ArrayList<Picture>() ;
    public static  int actual_size = 0 ;
    public AppCompatActivity activity = null;
    static final int REQUEST_IMAGE_CAPTURE = 1 ;
    public static final int CHOOSE_PHOTO = 2;
    public int holder_w , holder_h ;

    public void openAlbum() {
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
                        Intent intend = new Intent(v.getContext(),ShowLargePicture.class);
                         intend.putExtra("extra_int",x);
                        v.getContext().startActivity(intend) ;
                }
            }
        });
        return holder ;
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
        Bitmap bitmap = pictures.get(position).getBitmapInBound(holder_w ,
                holder_h) ;
        holder.img.setImageBitmap(bitmap);
        //Toast.makeText(activity , "图片大小:" + bitmap.getByteCount()/1024+"kb" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        if(pictures == null) return 0 ;
        else return pictures.size();
    }
}

public class PictureShowView extends LinearLayout {
    RecyclerView rview = null ;
    public  PictureShowAdapter adapter = new PictureShowAdapter() ;
    public PictureShowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.picture_show_view , this) ;
        rview = (RecyclerView) this.findViewById(R.id.recyclerview) ;
        rview.setLayoutManager(new GridLayoutManager(context , 4));
        setHolderSize(Account.global_width / 4 , Account.global_width / 4) ;
        addExtraPicture();
        rview.setAdapter(adapter);
    }
    ///show function ;
    public PictureShowView setLayoutManager(RecyclerView.LayoutManager lm , int num_in_width) {
        rview.setLayoutManager(lm);
        setHolderSize(Account.global_width / num_in_width , Account.global_width / num_in_width);
        return this ;
    }
    public void setHolderSize(int w, int h) {
        adapter.holder_w = w ;
        adapter.holder_h = h ;
    }
    protected void addExtraPicture() {
        ///How to get the View's height and widht ?
        __addActualPicture(new Picture(getResources() , R.drawable.camera_icon)) ;
        __addActualPicture(new Picture(getResources() , R.drawable.album_icon)) ;
        adapter.actual_size -= 2 ; /// Nice
    }
    protected void __addActualPicture(Picture pic) {
        if(adapter.pictures == null) {
            adapter.pictures = new ArrayList<Picture>() ;
            adapter.actual_size = 0 ;
        }
        if(adapter.pictures.size() > adapter.actual_size) {
            adapter.pictures.set(adapter.actual_size , pic) ;
        }
        else {
            adapter.pictures.add(pic) ;
        }
        adapter.actual_size ++ ;
    }

    public PictureShowView setPictureList(ArrayList<Picture>pics) {
        adapter.pictures = pics ; adapter.actual_size = pics.size() ;
        addExtraPicture();
        adapter.notifyDataSetChanged();
        return this ;
    }
    public PictureShowView addPicture (Picture pic) {
        __addActualPicture(pic) ;
        addExtraPicture();
        adapter.notifyDataSetChanged();
        return this ;
    }

    public void setActivity(AppCompatActivity activity) {
        adapter.activity = activity ;
    }

    public void onTakePicture() {
        //adapter.galleryAddPic();
        //Toast.makeText(adapter.activity , "图片大小:"+bitmap.getByteCount()/1024+"kb" ,Toast.LENGTH_SHORT).show(); ;
        addPicture(new Picture(adapter.mCurrentPhotoPath)) ;

    }
    public int getPictureNumber() {
        return adapter.actual_size ;
    }
    public byte[] getPictureByteArray(int index) {
        ByteArrayOutputStream baos ;
        Bitmap bitmap = adapter.pictures.get(index).getBitmapInBound() ;
        int quality = 100 ;
        int k ;
        do {
            quality -= 10 ;
            baos = new ByteArrayOutputStream() ;
            bitmap.compress(Bitmap.CompressFormat.JPEG , quality , baos) ;
        }
        while((k = baos.size()) > 200*1024) ;
        return baos.toByteArray() ;
    }
    public int getPictureOriginOrder(int index) {
        return -1 ;
    }
    public Uri getCurrentUri(Activity ac) {
        return FileProvider.getUriForFile(ac,
                "com.example.homepage.fileprovider",
                new File(adapter.mCurrentPhotoPath));
    }
    public String getCurrentPath(Activity ac) {
        return adapter.mCurrentPhotoPath ;
    }
}
