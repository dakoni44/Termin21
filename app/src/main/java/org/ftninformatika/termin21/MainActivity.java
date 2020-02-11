package org.ftninformatika.termin21;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvText;
    Button bStart;
    ProgressBar progressBar;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvText=findViewById(R.id.tvText);
        bStart=findViewById(R.id.bStart);
        progressBar=findViewById(R.id.progressBar);
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startInBackground(10);
                startAsyncTask(5);
            }
        });
    }

    private void startInBackground(final int secs){
        tvText.setText(""+ secs);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<secs;i++){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int remaining=secs-i-1;
                    tvText.post(new Runnable() {
                        @Override
                        public void run() {
                            tvText.setText("" + remaining);
                            if (tvText.getText().equals("0")) {
                            tvText.setText("Boom");
                        }
                    }
                    });
                }
                progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

    private void startAsyncTask(int secs){
        new MyAsyncTask().execute(secs);
    }

    private void showProgressDialog(){
        if(pd==null){
            pd=new ProgressDialog(this);
        }
        if(pd.isShowing()){
            pd.dismiss();
        }
        pd.setTitle("Sacekajte");
        pd.setMessage("Odbrojavanje pocinje");
        pd.show();
    }

    private void closeProgressDialog(){
        if(pd!=null){
            pd.dismiss();
        }
    }

    private void updateProgressDialog(int secs){
        if(pd!=null){
            pd.setMessage("Preostalo " +secs+ " sekundi");
        }
    }

    public class MyAsyncTask extends AsyncTask<Integer, Integer, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvText.setTextSize(10f);
            tvText.setTextColor(getResources().getColor(R.color.colorStart));
           // progressBar.setVisibility(View.VISIBLE);
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvText.setText("BOOM!!!");
            tvText.setTextSize(150f);
            tvText.setTextColor(getResources().getColor(R.color.colorAccent));
            //progressBar.setVisibility(View.INVISIBLE);
            closeProgressDialog();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tvText.setText(""+values[0]);
            updateProgressDialog(values[0]);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            for(int i=0;i<integers[0];i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(integers[0]-i);
            }
            return null;
        }
    }
}
