package com.example.readcitizenshipcard.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

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
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileBack);
                MultipartBody.Part body = MultipartBody.Part.createFormData(
                        "citizenshipback",
                        fileBack.getName(),
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
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent move = new Intent(
                                            BackTextRecogActivity.this,
                                            FormActivity.class);
                                    move.putExtra("citizenshipnumber", citizenshipNUMBER);
                                    move.putExtra("sex", sex);
                                    move.putExtra("fullname", fullNAME);
                                    move.putExtra("dobyear", dobYEAR);
                                    move.putExtra("dobmonth", dobMONTH);
                                    move.putExtra("dobday", dobDAY);
                                    move.putExtra("birthpalcedistrict", birthPlaceDistrict);
                                    move.putExtra("birthplacearea", birthPlaceArea);
                                    move.putExtra("birthplaceward", birthPlaceWARD);
                                    move.putExtra("permanentaddressdistrict", pAddressDISTRICT);
                                    move.putExtra("permanentaddressarea", pAddressAREA);
                                    move.putExtra("permanentaddressward", pAddressWARD);
                                    startActivity(move);
                                }
                            }, 2000);


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
        backcitizenshipImage = findViewById(R.id.citizenship_back_pic);
        backcitizenshipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(BackTextRecogActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(101);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {

            fileUriBack = data.getData();
            backcitizenshipImage.setImageURI(fileUriBack);
            fileBack = ImagePicker.Companion.getFile(data);

doneBtn2.setEnabled(true);
        }
    }
}