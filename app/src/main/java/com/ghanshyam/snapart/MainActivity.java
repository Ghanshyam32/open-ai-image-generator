package com.ghanshyam.snapart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.img);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        Button button = findViewById(R.id.generate);

        button.setOnClickListener(view -> {
            String text = editText.getText().toString().trim();
            if (text.isEmpty()) {
//                    Toast.makeText(getApplicationContext(),"Please enter something!",Toast.LENGTH_SHORT).show();
                editText.setError("Text can't be empty");
                return;
            }
            callAPI(text);
        });
    }

    private void callAPI(String text) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("prompt", text);
            jsonObject.put("size", "256x256");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/images/generations")
                .header("Authorization", "Bearer ---paste Open AI api here---")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(getApplicationContext(), "Failed to generate image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                try {
//                    JSONObject jsonObject1 = new JSONObject(response.body().string());
//                    String imgUrl = jsonObject1.getJSONArray("data").getJSONObject(0).getString("url");
//                    loadImageUrl(imgUrl);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Log.i("response : ", response.body().string());
            }
        });
    }

    void loadImageUrl(String url) {
        runOnUiThread(() -> {
            Picasso.get().load(url).into(imageView);
        });

    }

}