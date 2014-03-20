package org.chamedu.stifco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import network.OnResultListener;
import network.RestClient;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class Rechercher extends Activity implements View.OnClickListener  {
	
	private Spinner spinnerMois ;    
	Button soumettre;
	    
	    // Varaibles pour la lecture du flux Json
		private String jsonString;
		JSONObject jsonResponse;
		JSONArray arrayJson;
		AutoCompleteTextView tvGareAuto;
		ArrayList<String> items = new ArrayList<String>();
		
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rechercher);

			spinnerMois = (Spinner) findViewById(R.id.spinnerMois);
			List<String> list = new ArrayList<String>();
			list.add("janvier");
			list.add("fevrier");
			list.add("mars");
			list.add("avril");
			list.add("mai");
			list.add("juin");
			list.add("juillet");
			list.add("aout");
			list.add("septembre");
			list.add("octobre");
			list.add("novembre");
			list.add("decembre");
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerMois.setAdapter(dataAdapter);
			
			// Traitement du textView en autocompl�tion � partir de la source Json
			jsonString = lireJSON();
			
			try {
				jsonResponse = new JSONObject(jsonString);
				// Cr�ation du tableau g�n�ral �partir d'un JSONObject
				JSONArray jsonArray = jsonResponse.getJSONArray("gares");
				
				// Pour chaque �l�ment du tableau
				for (int i = 0; i < jsonArray.length(); i++) {

					// Cr�ation d'un tableau �l�ment � partir d'un JSONObject
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					// R�cup�ration �partir d'un JSONObject nomm�
					JSONObject fields  = jsonObj.getJSONObject("fields");

					// R�cup�ration de l'item qui nous int�resse
					String nom = fields.getString("nom_de_la_gare");

					// Ajout dans l'ArrayList
					items.add(nom);		
				}
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, items);
				tvGareAuto = (AutoCompleteTextView)findViewById(R.id.actvGare);
				tvGareAuto.setAdapter(adapter);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			soumettre = (Button)findViewById(R.id.btOffre);
			soumettre.setOnClickListener(this);
			
		}
		private void setDialogOnClickListener(int buttonId, final int dialogId) {
			Button b = (Button)findViewById(buttonId);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					showDialog(dialogId);
				}
			});
		}
		
		public String lireJSON() {
			InputStream is = getResources().openRawResource(R.raw.gares);
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return writer.toString();
		}
		
		
		public void onClick(View v) {
			if ( v == soumettre ) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gare",""+tvGareAuto.getText()));
			nameValuePairs.add(new BasicNameValuePair("mois",""+spinnerMois.getSelectedItem().toString()));
		
			String mois = spinnerMois.getSelectedItem().toString();
			
			Log.i("mois", mois);
			
		
			
			try {				
				RestClient.doPost("/recherche.php", nameValuePairs, new OnResultListener() {					
					@Override
					public void onResult(String json) {
						if ( json.equals("insertion_ok")) {
							Toast.makeText(Rechercher.this, "Les propositions :", Toast.LENGTH_LONG).show();
							finish();
						} else {
							Toast.makeText(Rechercher.this, json, Toast.LENGTH_LONG).show();
						}					
					}
					
				});
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			}
	   }
		
		
		public void addListenerOnButton(){
			spinnerMois = (Spinner) findViewById(R.id.spinnerMois);
		}
}

