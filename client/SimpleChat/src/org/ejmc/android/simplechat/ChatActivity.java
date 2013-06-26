package org.ejmc.android.simplechat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.ejmc.android.simplechat.model.ChatList;
import org.ejmc.android.simplechat.model.Message;

import org.ejmc.android.simplechat.net.NetRequests;
import org.ejmc.android.simplechat.net.NetResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Chat activity.
 * 
 * @author startic
 * @param <X>
 */
public class ChatActivity<X> extends Activity {

	
	private NetResponseHandler<ChatList> handler = new NetResponseHandler<ChatList>();
	private NetRequests netReq = new NetRequests();
	private TextView txtMensajes;
	private EditText txtNuevoMensaje;
	private ScrollView scroll;
	private String ip;
	private String user;
	int last_seq = 0;
	
	
	
	/* Handler que comunica el hilo que hace el GET con el hilo principal */
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msgAnd) {
			ChatList chatList = (ChatList) msgAnd.obj;
			last_seq = chatList.getLast_seq();
			ArrayList<Message> listaMensajes = chatList.getListaChat();
			for(int i=0; i< listaMensajes.size(); i++){
				String auxMsg = listaMensajes.get(i).getNick() + ": " + listaMensajes.get(i).getMessage() + "\n\n";
				txtMensajes.append(auxMsg);
				scroll.fullScroll(View.FOCUS_DOWN);
			}			
		}
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		txtMensajes = (TextView) findViewById(R.id.textView2);
		txtNuevoMensaje = (EditText) findViewById(R.id.editText1);
		scroll = (ScrollView) findViewById(R.id.scrollView1);
		Button btnEnviar =(Button) findViewById(R.id.button1);
		
		
		/* Rescatamos los valores almacenados en local */
		SharedPreferences settings = getSharedPreferences("perfil", MODE_PRIVATE);
		ip = settings.getString("ip", "valorpordefecto");
		user = settings.getString("user", "Desconocido");
		
		
		/* Cuando pulsamos el botón enviar */
		btnEnviar.setOnClickListener(new Button.OnClickListener() { 
	        public void onClick(View v){
	        	
	        	
	        	if(txtNuevoMensaje.getText().toString().equals("")){
	        		Toast toast1 = Toast.makeText(getApplicationContext(),"You must specify a message!", Toast.LENGTH_SHORT);
	        	    toast1.show();
	        	}
	        	else{
	        	
		        	new Thread(new Runnable() {
		        		String auxTxt = txtNuevoMensaje.getText().toString();
		        		public void run() {
		        			Message newMessage = new Message();
		    	        	newMessage.setNick(user);
		    	        	newMessage.setMessage(auxTxt);
		    	        	netReq.chatPOST(newMessage, null, ip);
		    	        }
		        	}).start();
	        	}
	        	
	        	
	        	txtNuevoMensaje.setText("");
	        	
	        }
	    });
		
		
		/* Creamos un hilo que se encargue de buscar nuevos mensajes (Peticion GET) */
		new Thread(new Runnable() {
			
			
			/* Creamos un timer para que haga la consulta cada 2 segundos */
			Timer timer;
	        @Override
	        public void run() {
	            timer = new Timer();
	            timer.scheduleAtFixedRate(timerTask, 0, 2000);
	        }

	        TimerTask timerTask = new TimerTask() {
	            public void run() {
	            	netReq.chatGET(last_seq, handler, ip);
					android.os.Message mensajeAnd = new android.os.Message();
					mensajeAnd.obj = handler.getMsg();
					mHandler.sendMessage(mensajeAnd);
	            }
	        };

	        public void stopTimer() {
	            timer.cancel();
	        }

	    }).start();
		
		
		
		// Show the Up button in the action bar.
		setupActionBar();
				

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().hide();
	}

}
