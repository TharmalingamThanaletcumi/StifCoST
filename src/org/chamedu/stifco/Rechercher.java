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

public class Rechercher extends Activity implements ViewSwitcher.ViewFactory,View.OnClickListener {

	AutoCompleteTextView tvGareAuto;
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
	
	nameValuePairs.add(new BasicNameValuePair("gare",""+tvGareAuto.getText()));
	
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
}
