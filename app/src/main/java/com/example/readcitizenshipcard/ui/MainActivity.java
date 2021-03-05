package com.example.readcitizenshipcard.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.readcitizenshipcard.R;
import com.example.readcitizenshipcard.model.ImageResponse;
import com.example.readcitizenshipcard.network.ApiService;
import com.example.readcitizenshipcard.network.RetrofitClientInstances;
import com.orhanobut.hawk.Hawk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getCanonicalName();
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ReadCitizenshipCard);
        setContentView(R.layout.activity_main);
        doneBtn = findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(this);

    }
    /**
     * Get data from   api site
     */
    public void getBooks(String bookname, int limit, String printType) {
        ApiService apiService = RetrofitClientInstances.getRetrofitInstance().create(ApiService.class);
        apiService.getSearchBooks(bookname, limit, printType)
                .enqueue(new Callback<ImageResponse>() {
                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        if (response.isSuccessful() && (response.body() != null)) {
                            //bookAdapter.setBooks(response.body().getItems());
                        }
                    }

                    @Override
                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure:: " + t.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {

        Intent move = new Intent(
                MainActivity.this,
                BackTextRecogActivity.class);
        startActivity(move);
    }
}