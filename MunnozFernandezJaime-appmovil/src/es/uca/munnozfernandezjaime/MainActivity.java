package es.uca.munnozfernandezjaime;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;
import android.view.View.OnClickListener;


public class MainActivity extends Activity {
	private TextView textView;
	Button MiBoton;
	Button MiBoton2;
	Button MiBoton3;
	public static String nuevalinea = System.getProperty("line.separator");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textView = (TextView)findViewById(R.id.textView1);
		
		registerForContextMenu(textView);
		
		
		MiBoton=(Button)findViewById(R.id.button1);
		MiBoton.setOnClickListener(new View.OnClickListener() 
		{
		@Override
		public void onClick(View view) {
			new LongRunningGetIO().execute();
			Log.v("Test","Después de pulsar"); }
		});
		
		MiBoton2=(Button)findViewById(R.id.button2);
		MiBoton2.setOnClickListener(new View.OnClickListener() 
		{
		@Override
		public void onClick(View view) {
			new LongRunningGetIO_JSON().execute();
			Log.v("Test","Después de pulsar"); }
		});
		
		MiBoton3=(Button)findViewById(R.id.button3);
		MiBoton3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				NotificationCompat.Builder notificacion =
						new NotificationCompat.Builder(MainActivity.this)
						.setSmallIcon(android.R.drawable.stat_sys_warning)
						.setContentTitle("Mensaje de Alerta")
						.setContentText("Ejemplo de notificación.")
						.setContentInfo("Hola")
						.setTicker("¡Jaime!")
						.setAutoCancel(true)
						.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
				Intent notIntent = new Intent(MainActivity.this, 
						Hola.class); 
						PendingIntent contIntent = 
						PendingIntent.getActivity(MainActivity.this,0, notIntent,0);
						notificacion.setContentIntent(contIntent);
						NotificationManager mNotificationManager = 
						(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
						mNotificationManager.notify(1, notificacion.build());
						
						
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		textView = (TextView) findViewById(R.id.textView1);
		switch (item.getItemId()) {
		case R.id.MnuOpc1:
			textView.setText("Opcion 1 pulsada!");
		return true;
		case R.id.MnuOpc2:
			textView.setText("Opcion 2 pulsada!");;
		return true;
		default:
		return super.onOptionsItemSelected(item);
		}
	}
	
	
	private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
		@Override
		protected String doInBackground (Void ...params){
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet("http://10.161.154.90:8080/es.uca.examenFeb/demo/video");
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				Log.v("Test","En la invocación");
				text=EntityUtils.toString(entity);
			} catch (Exception e) { return e.toString(); }
	
		return text; }	
				protected void onPostExecute(String results) {
					if (results!=null) {
						textView=(TextView)findViewById(R.id.textView1);
						textView.setText(results); 
						Toast.makeText(getApplicationContext(),"Get Terminado",Toast.LENGTH_SHORT).show();
					}
					else {Toast.makeText(getApplicationContext(),"Resultado Nulo",Toast.LENGTH_SHORT).show();} 
				}
		}
	
	
	private class LongRunningGetIO_JSON extends AsyncTask <Void, Void, String> {

		
		@Override
		protected String doInBackground (Void ...params){
	 
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://10.161.154.90:8080/es.uca.examenFeb/demo/video/buscar/1");
			httpGet.setHeader("content-type", "application/json");
			String respStr = null;
			try {
				HttpResponse response=httpClient.execute(httpGet);
				respStr = EntityUtils.toString(response.getEntity());
				JSONObject respJSON = new JSONObject(respStr);
				String nombre = respJSON.getString("titulo");
				String autor = respJSON.getString("director");
				respStr= nombre + "-" + autor;
			} catch (Exception e) { return e.toString(); }
		return respStr; 
		
		}	
	
	
		protected void onPostExecute(String results) {
			if (results!=null) {
				textView=(TextView)findViewById(R.id.textView1);
				textView.setText(results); }	
			Toast.makeText(getApplicationContext(),"JSON TERMINADO ",Toast.LENGTH_SHORT).show();
		}
	}	

}
