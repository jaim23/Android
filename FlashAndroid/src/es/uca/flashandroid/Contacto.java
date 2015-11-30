package es.uca.flashandroid;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Contacto extends Fragment {
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

		View vista =  inflater.inflate(R.layout.contacto,container, false);
		
		
		
		Context contexto = vista.getContext();
		
		Intent notIntent = new Intent(contexto,MainActivity.class); 
		PendingIntent contIntent = PendingIntent.getActivity(contexto,0, notIntent,0);
		
		NotificationCompat.Builder notificacion =
				new NotificationCompat.Builder(contexto)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("C.D. Flash")
				.setContentText("Volver a General")
				.setAutoCancel(true)
				.setContentIntent(contIntent)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

		
				NotificationManager mNotificationManager = 
				(NotificationManager)contexto.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(1,notificacion.build());
			
		
		
		return vista;
	}
	
}
