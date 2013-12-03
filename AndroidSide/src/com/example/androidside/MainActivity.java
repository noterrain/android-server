package com.example.androidside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import com.example.androidside.serviceScreen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    // Change the following variable to your server IP address.
    static final private String server_address = "172.20.10.2";
    static final private int server_port = 8080;

    private TextView textview_value_from_arduino; // GET_VALUE_FROM_ARDUINO
    private EditText text_value_to_arduino; // SET_VALUE_FROM_ANDROID
    private Button button_refresh_and_submit;
    private Timer myTimer;
    private int postSeconds=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, serviceScreen.class));
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 10000);
        this.textview_value_from_arduino = (TextView) findViewById(R.id.textView1);
        this.text_value_to_arduino = (EditText) findViewById(R.id.editText1);
        this.button_refresh_and_submit = (Button) findViewById(R.id.button1);
        this.button_refresh_and_submit.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
           

                new AsyncTask<String, Void, String>() {

                    @Override
                    protected String doInBackground(String... args) {
                        String result = null;

                        try{
                            HttpClient httpclient = new MyHttpClient();
                            HttpGet httpget = new HttpGet(String.format("http://%s:%d/homework/action.jsp?action=GET_VALUE_FROM_ARDUINO",
                                    server_address, server_port));

                            HttpResponse response = httpclient.execute(httpget);

                            // To read the whole http response. 
                            InputStream in = response.getEntity().getContent();
                            BufferedReader r = new BufferedReader(new InputStreamReader(in));
                            String s;
                            result = "";
                            while((s = r.readLine()) != null){
                                result += s;
                            }
                            Log.d("Http Response:", result);
                            result = "From Arduino:" + result;

                        }catch(Exception e){
                            e.printStackTrace();
                            result = "From Arduino:" + e.toString();
                        }

                        return result;
                    }

                    protected void onPostExecute(String result) {
                        textview_value_from_arduino.setText(result);
                    }

                }.execute();

            }
        });

        this.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Keyword.BROADCAST_FILTER_MAIN_ACTIVITY)){
                    if(intent.hasExtra(Keyword.KEYWORD_COMMAND)){
                        String command = intent.getStringExtra(Keyword.KEYWORD_COMMAND);
                        if(command.equals(Keyword.KEYWORD_COMMAND_REPORT_DURATION)){
                            long screen_on_timestamp = intent.getLongExtra(Keyword.KEYWORD_LAST_SCREENON_TIME, -1);
                            long screen_off_timestamp = intent.getLongExtra(Keyword.KEYWORD_LAST_SCREENOFF_TIME, -1);
                            long last_duration = intent.getLongExtra(Keyword.KEYWORD_LAST_DURATION, -1);
                           // last_duration
                            //int seconds=(int)last_duration/1000;
                            postSeconds+=last_duration;
                            
                            
                            //Log.i("mamahow", String.format("screen_on_timestamp: %d", screen_on_timestamp));
                            //Log.i("mamahow", String.format("screen_off_timestamp: %d", screen_off_timestamp));
                            //Log.i("mamahow", String.format("duration: %d", last_duration));

                        }else{
                            Log.e("mamahow", "Unsupport command: " + command);
                        }
                    }
                }
            }
        }, new IntentFilter(Keyword.BROADCAST_FILTER_MAIN_ACTIVITY));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void TimerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {

        public void run() {
        	String value_to_arduino = text_value_to_arduino.getText().toString();
        	
            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... args) {
                	
                    String result = null;
                    String value_to_arduino = args[0];

                    try{
                    	//Log.d("Http Response:", "aaaa");
                        HttpClient httpclient = new MyHttpClient();
                        HttpPost httppost = new HttpPost(String.format("http://%s:%d/homework/action.jsp", server_address, server_port));
                        //Log.d("Http Response:", "bbb");
                        // Put your http parameters HERE!
                        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                        nameValuePair.add(new BasicNameValuePair("action", "SET_VALUE_FROM_ANDROID"));
                        nameValuePair.add(new BasicNameValuePair("input", Integer.toString(postSeconds)));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                        HttpResponse response = httpclient.execute(httppost);

                        // To read the whole http response. 
                        //Log.d("Http Response:", "cccc");
                        InputStream in = response.getEntity().getContent();
                        BufferedReader r = new BufferedReader(new InputStreamReader(in));
                        String s;
                        result = "";
                        //Log.d("Http Response:", "dddd");
                        while((s = r.readLine()) != null){
                            result += s;
                        }
                        Log.d("Http Response:", result);
                        result = "Result:" + result;
    
                        //text_value_to_arduino.setText(Integer.toString(postSeconds),TextView.BufferType.EDITABLE);
                        Log.d("Timerkick", Integer.toString(postSeconds));
                        postSeconds=0;
                    }catch(Exception e){
                        e.printStackTrace();
                        result = "Result:" + e.toString();
                    }

                    return result;
                }

            }.execute(value_to_arduino);
        	
            //This method runs in the same thread as the UI.               

            //Do something to the UI thread here
            
          
            // Send Intent to serviceScreen (command: get_duration).
            Intent intent = new Intent();
            intent.setAction(Keyword.BROADCAST_FILTER_SERVICE_SCREEN);
            Bundle bundle = new Bundle();
            bundle.putString(Keyword.KEYWORD_COMMAND, Keyword.KEYWORD_COMMAND_GET_DURATION);
            intent.putExtras(bundle);
            sendBroadcast(intent);
        }
    };
}

class MyHttpClient extends DefaultHttpClient {

    public MyHttpClient() {
        super();
    }

    public MyHttpClient(HttpParams param) {
        super(param);
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", SslSocketFactoryNoCert(), 443));

        return new SingleClientConnManager(getParams(), registry);
    }

    private MySSLSocketFactory SslSocketFactoryNoCert() {
        try{
            KeyStore trusted = KeyStore.getInstance(KeyStore.getDefaultType());
            trusted.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trusted);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return sf;
        }catch(Exception e){
            throw new AssertionError(e);
        }
    }

}

// for Https.
class MySSLSocketFactory extends SSLSocketFactory {

    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException,
            UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
