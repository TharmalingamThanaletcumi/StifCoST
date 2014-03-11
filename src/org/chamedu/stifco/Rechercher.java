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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class Rechercher extends Activity implements View.OnClickListener  {
	
	    Button soumettre;
	    
	    // Varaibles pour la lecture du flux Json
		private String jsonString;
		JSONObject jsonResponse;
		JSONArray arrayJson;
		AutoCompleteTextView tvGareAuto;
		ArrayList<String> items = new ArrayList<String>();
		
		// Variables pour la date et l'heure
		private TextView mDateDisplay;
		private int mYear;
		private int mMonth;
		private int mDay;
		
		static final int DATE_DIALOG_ID = 2;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rechercher);
			
			// Traitement du changement de la date et de l'heure
			mDateDisplay = (TextView) findViewById(R.id.tvLaDate);
			
			setDialogOnClickListener(R.id.btChangeDate, DATE_DIALOG_ID);
			
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);

			updateDisplay();
			
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
			Log.i("test2", "recher .java");
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
		
		@Override
		protected Dialog onCreateDialog(int id) {
			switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this,
						mDateSetListener,
						mYear, mMonth, mDay);
			}
			return null;
		}
	
		private void updateDisplay() {
			mDateDisplay.setText(
					new StringBuilder()
					// Month is 0 based so add 1
					.append(mMonth + 1).append("-")
					.append(mDay).append("-")
					.append(mYear));
		}
		
		private DatePickerDialog.OnDateSetListener mDateSetListener =
				new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDisplay();
			}
		};
		
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
			}
	   }
}

