package com.example.readcitizenshipcard.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.readcitizenshipcard.R;
import com.example.readcitizenshipcard.model.TextResponse;
import com.example.readcitizenshipcard.network.ApiService;
import com.example.readcitizenshipcard.network.RetrofitClientInstances;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackTextRecogActivity extends AppCompatActivity {
    private static final String TAG = BackTextRecogActivity.class.getSimpleName();
    File fileBack;
    Uri fileUriBack;
    private Button doneBtn2;
    private ImageView backcitizenshipImage;
    ProgressBar progress2;
    Bitmap bitmap = null;
    int REQUEST_CODE = 99;
    byte[] bitmapdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ReadCitizenshipCard);
        setContentView(R.layout.activity_back_text_recog);
        getSupportActionBar().hide();
        progress2 = findViewById(R.id.progress_bar_2);
        doneBtn2 = findViewById(R.id.done_btn2);
        doneBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress2.setVisibility(View.VISIBLE);
                doneBtn2.setText("Please Wait");
                doneBtn2.setEnabled(false);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bitmapdata);
                MultipartBody.Part body = MultipartBody.Part.createFormData(
                        "citizenshipback",
                        String.valueOf(bitmapdata),
                        requestFile);
                RequestBody citizenship = RequestBody.create(MediaType.parse("multipart/form-data"), "yourNAME");
                ApiService apiService = RetrofitClientInstances.getRetrofitInstance().create(ApiService.class);
                apiService.uploadText(body, citizenship).enqueue(new Callback<TextResponse>() {

                    @Override
                    public void onResponse(Call<TextResponse> call, Response<TextResponse> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        Log.d(TAG, "onResponse: " + response.body().getCitizenNo());
                        if (response.isSuccessful()) {
                            View parentLayout = findViewById(android.R.id.content);
                            progress2.setVisibility(View.GONE);
                            doneBtn2.setText("VERIFIED");
                            doneBtn2.setTextColor(Color.GREEN);
                            Snackbar snackbar = Snackbar.make(parentLayout, "Text detected Successfully !!! ", Snackbar.LENGTH_LONG);
                            snackbar.setDuration(1000);
                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                            snackbar.setBackgroundTint(Color.GREEN).show();
                            String citizenshipNUMBER = response.body().getCitizenNo();
                            String sex = response.body().getSex();
                            String fullNAME = response.body().getFullName();
                            String dobYEAR = response.body().getDobYear();
                            String dobMONTH = response.body().getDobMonth();
                            String dobDAY = response.body().getDobDay();
                            String birthPlaceDistrict = response.body().getBirthPlaceDistrict();
                            String birthPlaceArea = response.body().getBirthPlaceArea();
                            String birthPlaceWARD = response.body().getBirthPlaceWardNo();
                            String pAddressDISTRICT = response.body().getPAddressDistrict();
                            String pAddressAREA = response.body().getPAddressArea();
                            String pAddressWARD = response.body().getPAddressWardNo();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Intent move = new Intent(
//                                            BackTextRecogActivity.this,
//                                            FormActivity.class);
//                                    move.putExtra("citizenshipnumber", citizenshipNUMBER);
//                                    move.putExtra("sex", sex);
//                                    move.putExtra("fullname", fullNAME);
//                                    move.putExtra("dobyear", dobYEAR);
//                                    move.putExtra("dobmonth", dobMONTH);
//                                    move.putExtra("dobday", dobDAY);
//                                    move.putExtra("birthpalcedistrict", birthPlaceDistrict);
//                                    move.putExtra("birthplacearea", birthPlaceArea);
//                                    move.putExtra("birthplaceward", birthPlaceWARD);
//                                    move.putExtra("permanentaddressdistrict", pAddressDISTRICT);
//                                    move.putExtra("permanentaddressarea", pAddressAREA);
//                                    move.putExtra("permanentaddressward", pAddressWARD);
//                                    startActivity(move);
//                                }
//                            }, 2000);


                        } else{
                            progress2.setVisibility(View.VISIBLE);
                            doneBtn2.setText("NOT VERIFIED");
                            doneBtn2.setTextColor(Color.RED);
                            String res = doneBtn2.getText().toString();
                            if(res == "NOT VERIFIED"){
                                backcitizenshipImage.setImageResource(R.drawable.ic_baseline_add_24);
                                doneBtn2.setEnabled(false);
                                doneBtn2.setText("Done");
                                doneBtn2.setTextColor(Color.WHITE);
                            }
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar snackbar = Snackbar.make(parentLayout, "unsuccessful To detected Text!!! ", Snackbar.LENGTH_LONG);
                            snackbar.setDuration(1000);
                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                            snackbar.setBackgroundTint(Color.RED).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<TextResponse> call, Throwable t) {
                        Log.d(TAG, "onResponse: " + t.getLocalizedMessage());
                    }
                });
//                Intent move = new Intent(
//                        BackTextRecogActivity.this,
//                        FormActivity.class);
//                startActivity(move);

            }
        });
        backcitizenshipImage = findViewById(R.id.citizenship_back_pic1);
        backcitizenshipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int preference = ScanConstants.OPEN_MEDIA;
                Intent intent = new Intent(BackTextRecogActivity.this, ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
             fileUriBack = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUriBack);
                getContentResolver().delete(fileUriBack, null, null);
                backcitizenshipImage.setImageBitmap(bitmap);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos); // YOU can also save it in JPEG
                 bitmapdata = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        doneBtn2.setEnabled(true);
    }

}