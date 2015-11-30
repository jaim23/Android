package es.uca.flashandroid;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class Rest extends Fragment {
	TextView MiLabel;
	public static String nuevalinea = System.getProperty("line.separator");
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		View vista = inflater.inflate(R.layout.rest,container, false);

		Button MiBoton2=(Button)vista.findViewById(R.id.botLocal);
		MiBoton2.setOnClickListener(new View.OnClickListener() 
		{
		@Override
		public void onClick(View view) {
			new LongRunningGetIO().execute();
			}
		});
		

	      return vista;
	}
	public void mensaje(){
		Toast.makeText(getActivity(), "Resultados Obtenidos",Toast.LENGTH_SHORT).show();
	}
	
	private class LongRunningGetIO extends AsyncTask <Void, Void, String> {

	
		@Override
		protected String doInBackground (Void ...params){

		
		HttpClient httpClient = new DefaultHttpClient();
		 
			final EditText txtTexto = (EditText)getActivity().findViewById(R.id.inputCalle);
			String calle = txtTexto.getText().toString();
			calle.toLowerCase();
			calle=espacios(calle);
			final EditText txtNumero = (EditText)getActivity().findViewById(R.id.inputNumero);
			String numero = txtNumero.getText().toString();
			numero.toLowerCase();
			numero=espacios(numero);
			final EditText txtCiudad = (EditText)getActivity().findViewById(R.id.inputCiudad);
			String ciudad = txtCiudad.getText().toString();
			ciudad.toLowerCase();
			ciudad=espacios(ciudad);

			
			HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?address="+calle+"+"+numero+"+"+ciudad+"&sensor=false");
			httpGet.setHeader("content-type", "application/json");
			String respStr = null;
			try {
				HttpResponse response=httpClient.execute(httpGet);
				respStr = EntityUtils.toString(response.getEntity());
				String texto=nuevalinea;
				JSONObject respJSON = new JSONObject(respStr);
				JSONArray jArray = respJSON.optJSONArray("results");
				String estado = respJSON.getString("status");
				
				 for (int i = 0; i < jArray.length(); i++) {
					 JSONObject jsonObject = jArray.getJSONObject(i);
					 
					 String direccion = jsonObject.getString("formatted_address");
					 JSONObject geo = jsonObject.getJSONObject("geometry");
					 JSONObject lonlat = geo.getJSONObject("location");
					 
					 String lat = lonlat.getString("lat");
					 String lng = lonlat.getString("lng");
					 
					 texto+=nuevalinea+direccion+nuevalinea+nuevalinea+"Latitud: "+lat+nuevalinea+"Longitud: "+lng+nuevalinea;
				 }

				 if(estado.equals("ZERO_RESULTS"))
					 respStr=null;
				else	 
				 respStr= texto;
			} catch (Exception e) { return e.toString(); }
			
			return respStr; 
		
		}	
		
		
		protected void onPostExecute(String results) {
			
			if (results!=null) {	
				mensaje();
				MiLabel=(TextView)getActivity().findViewById(R.id.textPosicion);
				MiLabel.setText(results); 
				}
			else Toast.makeText(getActivity(), "No hay Resultados",Toast.LENGTH_SHORT).show();
		}
		
		
		public String espacios(String cadena){
			String cadena2="";
			for (int x=0; x < cadena.length(); x++) {
				  if (cadena.charAt(x) != ' ')
				    cadena2 += cadena.charAt(x);
			}
			return cadena2;
		}
		 

		}
	
}