package com.example.readcitizenshipcard.ui;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.readcitizenshipcard.R;
import com.example.readcitizenshipcard.model.ImageResponse;
import com.example.readcitizenshipcard.network.ApiService;
import com.example.readcitizenshipcard.network.RetrofitClientInstances;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import kotlinx.coroutines.Delay;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getCanonicalName();
    private Button doneBtn;
    private ImageView ppSizeImage;
    private ImageView frontcitizenshipImage;
    private Uri fileUri1,fileUri2;
    private File file1,file2;
    ProgressBar progressbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ReadCitizenshipCard);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        doneBtn = findViewById(R.id.done_btn);
        progressbar1 = findViewById(R.id.progress_bar_1);
        doneBtn.setOnClickListener(this);
        frontcitizenshipImage = findViewById(R.id.citizenship_pic);
        frontcitizenshipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(MainActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(101);
            }
        });
        ppSizeImage = findViewById(R.id.pp_size_pic);
        ppSizeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(MainActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(102);

            }
        });
    }
    /**
     * Get data from   api site
     */
    @Override
    public void onClick(View v) {

//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
//        byte[] imagefrontInByte = byteArrayOutputStream.toByteArray();
//        String encodedImageFront = Base64.encodeToString(imagefrontInByte,Base64.DEFAULT);
//        Toast.makeText(this, encodedImageFront, Toast.LENGTH_SHORT).show();
//
//        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
//        bitmap2.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream1);
//        byte[] imagefrontInByte1 = byteArrayOutputStream1.toByteArray();
//        String encodedPpzise = Base64.encodeToString(imagefrontInByte1,Base64.DEFAULT);
//        Toast.makeText(this, encodedPpzise, Toast.LENGTH_SHORT).show();
        //for citizenship front
        progressbar1.setVisibility(VISIBLE);
        doneBtn.setText("Please Wait");
        doneBtn.setEnabled(false);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("citizenshipfront", file1.getName(), requestFile);
        RequestBody citizenshipfront1 =
                RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name");
        //for pp size image
        RequestBody requestFile2 =
                RequestBody.create(MediaType.parse("multipart/form-data"), file2);
        MultipartBody.Part body2 =
                MultipartBody.Part.createFormData("photo", file2.getName(), requestFile2);
        RequestBody ppsize =
                RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name");

        ApiService apiService = RetrofitClientInstances.getRetrofitInstance().create(ApiService.class);
        apiService.uploadImage(body,citizenshipfront1,body2,ppsize)
                .enqueue(new Callback<ImageResponse>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        if (response.isSuccessful() && (response.body() != null)) {
                            Log.d(TAG, "onResponse: GOOOOODDD GOOOOOOOD  " + response.body());
                            Log.d(TAG, "onResponse: verified" + response.body().getVerified());

                            if (response.body().getVerified()){
                               // Toast.makeText(MainActivity.this, "Image Verified Successful !!!", Toast.LENGTH_SHORT).show();
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar snackbar = Snackbar.make(parentLayout, "Image Verified Successful !!! ", Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.GREEN);
                                        snackbar.setDuration(1000);
                                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                        snackbar.show();
                                progressbar1.setVisibility(GONE);
                                doneBtn.setText("VERIFIED");
                                doneBtn.setTextColor(Color.GREEN);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent move = new Intent(
                                                MainActivity.this,
                                                BackTextRecogActivity.class);

                                        startActivity(move);
                                    }
                                }, 2000);

                            } else{
                                progressbar1.setVisibility(GONE);
                                doneBtn.setText("NOT VERIFIED");
                                doneBtn.setTextColor(Color.RED);
                                String res = doneBtn.getText().toString();
                                if(res == "NOT VERIFIED"){
                                    frontcitizenshipImage.setImageResource(R.drawable.ic_baseline_add_24);
                                    ppSizeImage.setImageResource(R.drawable.ic_baseline_add_24);
                                    doneBtn.setEnabled(false);
                                    doneBtn.setText("Done");
                                    doneBtn.setTextColor(Color.WHITE);

                                }
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar snackbar = Snackbar.make(parentLayout, "Unsuccessful!! Image doesn't match. ", Snackbar.LENGTH_LONG);
                                snackbar.setDuration(1000);
                                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                snackbar.setBackgroundTint(Color.RED).show();
                            }
                        }else{
                            Log.d(TAG, "onResponse: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure:: " + t.getLocalizedMessage());
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {

         fileUri1 = data.getData();
         frontcitizenshipImage.setImageURI(fileUri1);
            file1 = ImagePicker.Companion.getFile(data);



        } else if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            fileUri2= data.getData();
            ppSizeImage.setImageURI(fileUri2);
            file2= ImagePicker.Companion.getFile(data);
            doneBtn.setEnabled(true);
        }
    }
}