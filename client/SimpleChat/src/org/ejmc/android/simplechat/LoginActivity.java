package org.ejmc.android.simplechat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Main activity.
 * 
 * Shows login config.
 * 
 * @author startic
 * 
 */
public class LoginActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button btnLogin =(Button) findViewById(R.id.button1);
		final EditText name = (EditText) findViewById(R.id.editText2);
		final EditText ip = (EditText) findViewById(R.id.editText1);
		
		
		
		btnLogin.setOnClickListener(new Button.OnClickListener() {  
	        public void onClick(View v){
	        	
	        	/* Controlamos que los campos no estén vacios */
	        	if(name.getText().toString().equals("")){
	        		Toast toast1 = Toast.makeText(getApplicationContext(),"You must specify a name!", Toast.LENGTH_SHORT);
	        	    toast1.show();
	        	}
	        	
	        	else if(ip.getText().toString().equals("")){
	        		Toast toast1 = Toast.makeText(getApplicationContext(),"You must specify an IP!", Toast.LENGTH_SHORT);
	        	    toast1.show();
	        	}
	        	
	        	else{
	        	
	        		String nameText = name.getText().toString();
	        		String ipText = ip.getText().toString();
	        	
	        		SharedPreferences settings = getSharedPreferences("perfil", MODE_PRIVATE);
	        		SharedPreferences.Editor editor = settings.edit();
	        		editor.putString("user",nameText);
	        		editor.putString("ip", ipText);
	        		editor.commit();
	        	
	        		Intent inten = new Intent(LoginActivity.this, ChatActivity.class);
	        		startActivity(inten);
	        	
	        	}
	        	
	        }
	    });
		
		
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
		
}
