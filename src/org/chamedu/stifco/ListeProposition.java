package org.chamedu.stifco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class ListeProposition extends Activity {
	
	String json ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listeproposition);
		
		Intent liste = getIntent();
		json = liste.getStringExtra("json");
		Toast.makeText(ListeProposition.this, json , Toast.LENGTH_LONG).show();
		
	}

}
