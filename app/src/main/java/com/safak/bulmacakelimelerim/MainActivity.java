package com.safak.bulmacakelimelerim;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//  REFERENCE - API USAGE:  https://youtu.be/oGWJ8xD2W6k
//  REFERENCE - JSON PARSE: https://youtu.be/y2xtLqP8dSQ
public class MainActivity extends AppCompatActivity {

    final String URL = "http://178.62.45.220:3000/api/random";

    OkHttpClient client = new OkHttpClient();
    JSONObject jsonObject;

    TextView tvQuestion;
    TextView tvAnswer;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuestion = findViewById(R.id.tv_question);
        tvAnswer  =findViewById(R.id.tv_answer);
        cardView = findViewById(R.id.cv_container);

        final Request request = new Request.Builder().url(URL).build();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){

                            final String jsonResponse = response.body().string();

                            try{
                                jsonObject = new JSONObject(jsonResponse);
                                Iterator<String> it = jsonObject.keys();

                                final String key = jsonObject.keys().next();
                                final String value = jsonObject.getString(key);

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvAnswer.setText(key);
                                        tvQuestion.setText(value);
                                    }
                                });

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }
}
