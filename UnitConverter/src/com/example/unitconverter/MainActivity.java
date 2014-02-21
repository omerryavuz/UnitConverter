package com.example.unitconverter;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnItemSelectedListener {

	String jsonString;
	ListView listView;
	EditText leftEditText , rightEditText;
	ImageButton changeDirection;
	TextView leftUnitText , rightUnitText;
	TextView leftTitle , rightTitle, currentCatagory;
	SharedPreferences settings;
	public ArrayAdapter<String> array_adapter;
	JSONArray jsonArray = null;
	int clickedListElementPosition = -1;
	String[] catagoryList;
	CustomKeyboard keyboard;
	Spinner leftSpinner,rightSpinner;
	String max_number_of_digits , updateDate;
	TextView leftInfo ;
	String leftStringInfo , rightStringInfo,leftUrl,rightUrl ,leftSelectedItemName,rightSelectedItemName , infoWeb ,infoName;
	int isFirst ;
	Dialog dialog;
	Button showResult;
	int hintNumber ;
	public static MainActivity ma;
	Date interestingDate ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ma = this;
		listView = (ListView)findViewById(R.id.UnitList);
		leftEditText = (EditText)findViewById(R.id.LeftEditText);
		rightEditText = (EditText)findViewById(R.id.RightEditText);
		changeDirection = (ImageButton)findViewById(R.id.DirectionButton);
		leftUnitText = (TextView)findViewById(R.id.leftUnitText);
		rightUnitText = (TextView)findViewById(R.id.rightUnitText);
		leftTitle = (TextView)findViewById(R.id.LeftTextView);
		rightTitle = (TextView)findViewById(R.id.RightTextView);
		currentCatagory = (TextView)findViewById(R.id.CurrentCatagory);
		settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		showResult = (Button) findViewById(R.id.ShowResult);
		readPreferences();
		keyboard= new CustomKeyboard(this, R.id.keyboardview, R.layout.keyboard2 );
		keyboard.registerEditText(R.id.LeftEditText);
		keyboard.registerEditText(R.id.RightEditText);
		leftSpinner = (Spinner)findViewById(R.id.LeftSpinner);
		rightSpinner = (Spinner)findViewById(R.id.RightSpinner);
		rightSpinner.setOnItemSelectedListener(this);
		leftSpinner.setOnItemSelectedListener(this);
		leftEditText.setText("1");
		showResult.setVisibility(View.INVISIBLE);
		interestingDate = new Date();
		hintNumber = 3;
		updateListView(); 		
		setSpinners(0);
		hintNumber = 3;
        listView.setOnItemClickListener( new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
        		setSpinners(arg2);
        	//	long unixTime = System.currentTimeMillis() / 1000L;
        	}
		});   
    }
	//------------------------------------------ set Spinners according to clicked listView item ---------------------------------------------------
	public void setSpinners(int arg2){
		currentCatagory.setText(catagoryList[arg2]+" Birimleri");
		JSONArray jsonElement = new JSONArray();
		try {
			jsonElement = jsonArray.getJSONObject(arg2).getJSONArray("Units");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] currentUnitList = new String[jsonElement.length()];
		for(int i=0;i<jsonElement.length() ; i++ ){
			try {
				currentUnitList[i] = jsonElement.getJSONObject(i).getString("Name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		clickedListElementPosition = arg2;
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinnertext, currentUnitList);
		leftSpinner.setAdapter(spinnerAdapter);
		rightSpinner.setAdapter(spinnerAdapter);
	}
	//------------------------------------------ set Spinners according to clicked listView item ---------------------------------------------------
	public int numberOfUnits(){
		int k=0;
		try {
			JSONArray jsonArray =  new JSONArray(jsonString);
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				JSONArray ja = jsonObject.getJSONArray("Units");
				k += ja.length();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return k;
	}
	public void updateListView(){
		List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
		
		if(isFirst == 0 || jsonString.equals("")){
			jsonString = loadJSONFromAsset();
		}
        try {
			jsonArray = new JSONArray(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catagoryList = new String[jsonArray.length()]; 
        for(int i=0 ; i< jsonArray.length() ; i++){
        	try {
				catagoryList[i] = jsonArray.getJSONObject(i).getString("Category Name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        for(int i=0;i<catagoryList.length;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", catagoryList[i] + " Birimleri");
   //         hm.put("hint"," ------ ");
            //---------------------------------------------------
            String hints = "";
            for(int k=0;k<hintNumber;k++){
            	try {
					hints += jsonArray.getJSONObject(i).getJSONArray("Units").getJSONObject(k).getString("Name") + " ";
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
            	if(k!=hintNumber-1){
            		hints += ",";
            	}
            }
            hints +="...";
            hm.put("hint", hints);
            //---------------------------------------------------
            hm.put("photo", Integer.toString(Birimler.photos[i]) );
            aList.add(hm);
        }   
        String[] from = { "photo","txt","hint" };
        int[] to = { R.id.photo,R.id.txt,R.id.hint};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list, from, to);
        listView = ( ListView ) findViewById(R.id.UnitList);
        listView.setAdapter(adapter);		
	}
	//-------------------------------------------------------------------------------- Update -------------------------------------------------------------------------------------------
	public void onClickUpdateButton(View v){
		if(isOnline()){
			long unixTime = System.currentTimeMillis() / 1000L;
			
			if(unixTime > settings.getLong("unixTime", 0) + 60*60*24){
				new LongOperation().execute();	
				SharedPreferences.Editor editor = settings.edit();
				editor.putLong("unixTime", unixTime);
				editor.commit();
			}
			else{
			}
		}
	}
	private class LongOperation extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
        	asyncJson();
        	return jsonString;
        }        
        @Override
        protected void onPostExecute(String result) {  
			try {
				isFirst = 1;
				setPreferences(isFirst,max_number_of_digits,jsonString);
		//		updateListView();
	        	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;	
			}	
        }
        @Override
        protected void onPreExecute() {
        }	
        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
	public void asyncJson(){
        String url = "http://192.168.2.126:8000/jsonEng.json";
        final AQuery aq = new AQuery(MainActivity.this);
        aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                		if(json != null){
                                jsonString = json.toString();
                                updateListView();
                		}                                
                }
        });
        
	}
	//-------------------------------------------------------------------------------- Update -------------------------------------------------------------------------------------------

	public void onClick(View v) throws JSONException{
		if(R.id.DirectionButton == v.getId()){
			int position1 = leftSpinner.getSelectedItemPosition();
			int position2 = rightSpinner.getSelectedItemPosition();
			leftSpinner.setSelection(position2);
			rightSpinner.setSelection(position1);
		}/*
		else if(R.id.Calculate == v.getId()){
			calculate();
		}*/
	}
	//----------------------------------------------------------------------------- Calculation --------------------------------------------------------------------------------------------
	public void calculate(){
		String enteredValue = null;
		int fromPosition, toPosition;
		if(leftEditText.getText().toString().length() == 0){
			rightEditText.setText("");
			showResult.setVisibility(View.INVISIBLE);
			return;
		}
		enteredValue = (leftEditText.getText().toString());
		if(enteredValue.charAt(0) == 'E'){
			rightEditText.setText("");
			showResult.setVisibility(View.INVISIBLE);
			return ;
		}
		fromPosition = leftSpinner.getSelectedItemPosition();
		toPosition = rightSpinner.getSelectedItemPosition();
		if(fromPosition != -1 && toPosition != -1 && clickedListElementPosition != -1){ 
			JSONObject inputUnit = new JSONObject();
			JSONObject outputUnit = new JSONObject();
			try {
				inputUnit = jsonArray.getJSONObject(clickedListElementPosition).getJSONArray("Units").getJSONObject(fromPosition);
				outputUnit= jsonArray.getJSONObject(clickedListElementPosition).getJSONArray("Units").getJSONObject(toPosition);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BigDecimal resultBig = null;
			try {
				//--------------------------------------------------------------------
				try {
					leftStringInfo = inputUnit.getString("Info");
				} catch (Exception e) {
					leftStringInfo = "";}
				try{
					leftUrl = inputUnit.getString("Web");
				}catch(Exception e){
					leftUrl = "";}
				try {
					rightStringInfo = outputUnit.getString("Info");
				} catch (Exception e) {
					rightStringInfo = "";}
				try{
					rightUrl = outputUnit.getString("Web");
				}catch(Exception e){
					rightUrl = "";
				}
				View vLeft = findViewById(R.id.leftInfoButton);
				View vRight= findViewById(R.id.rightInfoButton);
				if(leftStringInfo.equals("") && leftUrl.equals("")){
					vLeft.setVisibility(View.INVISIBLE);
				}
				else{ vLeft.setVisibility(View.VISIBLE);}
				if(rightStringInfo.equals("") && rightUrl.equals("")){
					vRight.setVisibility(View.INVISIBLE);
				}
				else{ vRight.setVisibility(View.VISIBLE); }
				//--------------------------------------------------------------------
				leftSelectedItemName = inputUnit.getString("Name");
				rightSelectedItemName = outputUnit.getString("Name");
				
				String inputString = inputUnit.getString("Coefficient");
				BigDecimal firstNumber = new BigDecimal(inputString);
				String outputString = outputUnit.getString("Coefficient");
				BigDecimal secondNumber= new BigDecimal(outputString);
				String result = null;
				if(jsonArray.getJSONObject(clickedListElementPosition).getString("Category Name").equals("Sıcaklık")){
					resultBig = new BigDecimal(enteredValue);
					result = sıcaklıkCevir(inputUnit.getString("Name"), resultBig,0);
					BigDecimal tmp = new BigDecimal(result);
					result = sıcaklıkCevir(outputUnit.getString("Name"), tmp,1);
				}
				else if(jsonArray.getJSONObject(clickedListElementPosition).getString("Category Name").equals("Takvim Çevirme")){
					resultBig = new BigDecimal(enteredValue);
					result = takvimCevirme(inputUnit.getString("Name"), outputUnit.getString("Name"), resultBig);
					BigDecimal a = new BigDecimal(result);
					int pos = result.indexOf(".");
					if(pos != -1){
						result = result.substring(0,pos);
					}
				}
				else{
					resultBig = new BigDecimal(enteredValue);
					resultBig = resultBig.multiply(secondNumber);
					resultBig = resultBig.divide(firstNumber,60, BigDecimal.ROUND_FLOOR);
					result = resultBig.toString();
				}
				//-----------------------------------------  Arrange result acc. to decimal number -------------------------------------------------------------
				int dotPosition = -1;
				int Eposition = -1;
				try {
					Eposition  = result.indexOf("E");
					dotPosition = result.indexOf(".");
				} catch (Exception e) {
					// TODO: handle exception
				}
				int endPosition = result.length();
				if(dotPosition != -1 && Eposition == -1){
					for(int i=result.length()-1;i>=dotPosition;i--){
						if(result.charAt(i) =='.' ){
							endPosition--;
							break;
						}
						else if(result.charAt(i) != '0' ){
							break;
						}
						else{
							endPosition--;
						}
					}
					result = result.substring(0,endPosition);
					int dot= -1;
					try{
						dot = result.indexOf(".");
						if(dot != -1 && ( (result.length() - dot -1) > Integer.parseInt(max_number_of_digits))){
							result = result.substring(0,dot+1) + result.substring(dot+1,Integer.parseInt(max_number_of_digits)+dot+1);
						}
					}catch(Exception e){
					}
				}
				rightEditText.setText(result);
				if(result.length() > 24){
					showResult.setVisibility(View.VISIBLE);
				}
				else{
					showResult.setVisibility(View.INVISIBLE);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				leftUnitText.setText(inputUnit.getString("Abbreviation"));
				rightUnitText.setText(outputUnit.getString("Abbreviation"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
		}
	}
	public void showResult(View v){
		if(v.getId() == R.id.ShowResult){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Sonuç");
			String result = rightEditText.getText().toString();
			builder.setMessage(result);
			builder.setNegativeButton("GERİ", new DialogInterface.OnClickListener()
	        {
	            @Override
	            public void onClick(DialogInterface dialog, int id)
	            {
	            }
	        });
			builder.show();
		}
	}
	//----------------------------------------------------------------------------- Calculation --------------------------------------------------------------------------------------------
	
	//----------------------------------------------------------------------------- Info Dialog --------------------------------------------------------------------------------------------
	public void onInfoClick(View v){
		String infoText ="" ;
		AlertDialog.Builder infoBuilder = new  AlertDialog.Builder(this);
		if(v.getId() == R.id.leftInfoButton){
			infoName = leftSelectedItemName;
			infoText = leftStringInfo;
			infoWeb  = leftUrl;
		}
		else{
			infoName = rightSelectedItemName;
			infoText = rightStringInfo;
			infoWeb = rightUrl;
		}
		if(infoText != null && !infoText.equals("")){
			infoBuilder.setTitle(infoName);
			infoBuilder.setMessage(infoText);
			infoBuilder.setNegativeButton("GERİ", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub	
				}
			});
			if(infoWeb!=null && !infoWeb.equals("") ){
				if(!isOnline()){
					showInternetError();
				}
				infoBuilder.setPositiveButton("Daha Fazla Bilgi", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog2, int which) {
						// TODO Auto-generated method stub
						
						dialog = createAlertDialog(infoWeb, infoName);
						dialog = createAlertDialog(infoWeb, infoName);
						Button cancelDialogButton = (Button) dialog.findViewById(R.id.ProgressButton); 
						cancelDialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
					  	dialog.show();
				        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				        lp.copyFrom(dialog.getWindow().getAttributes());
				        lp.width = 800;
				        dialog.getWindow().setAttributes(lp);

					}
				});
			}
			infoBuilder.show();
		}
		else if(infoWeb!=null && !infoWeb.equals("") ){
			if(!isOnline()){
				showInternetError();
			}
			else{
				dialog = createAlertDialog(infoWeb, infoName);
				Button cancelDialogButton = (Button) dialog.findViewById(R.id.ProgressButton); 
				cancelDialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
	            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		        lp.copyFrom(dialog.getWindow().getAttributes());
	            lp.width = 800;
		        dialog.getWindow().setAttributes(lp);
			}
		}
	}

	public void showInternetError(){
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("HATA");
		b.setMessage("Lütfen internet bağlantınızı kontrol ediniz");
		b.setNegativeButton("GERİ", new  DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		b.show();
	}
	public Dialog createAlertDialog(String webUrl , String name ){

        Dialog dialog = new Dialog(this);
		dialog.setTitle(infoName);
		dialog.setContentView(R.layout.webview);
		dialog.setCancelable(true);
		WebView web = (WebView) dialog.findViewById(R.id.WebView);
		WebSettings webSettings = web.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setSupportZoom(true) ;
		webSettings.setBuiltInZoomControls(true);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
		web.loadUrl(infoWeb);
		final RelativeLayout r = (RelativeLayout)dialog.findViewById(R.id.ProgressLine);
		web.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
				  @Override
				  public void run() {
					  r.setVisibility(View.GONE);
				  }
				}, 2000);
            }
        });
		return dialog;
	}
	//----------------------------------------------------------------------------- Info Dialog --------------------------------------------------------------------------------------------
	public String takvimCevirme(String from , String to , BigDecimal big){
		
		if(from.equals("Hicri Yıl") && to.equals("Miladi Yıl")  ){
			BigDecimal big1 = new BigDecimal("32");
			BigDecimal big2 = new BigDecimal("33");
			BigDecimal big3 = new BigDecimal("622");
			big1 = big1.divide(big2,30,BigDecimal.ROUND_FLOOR);
			big = big.multiply(big1);
			big = big.add(big3);
			return big.toString();
		}
		else if(to.equals("Hicri Yıl") && from.equals("Miladi Yıl") ){
			BigDecimal big1 = new BigDecimal("32");
			BigDecimal big2 = new BigDecimal("33");
			BigDecimal big3 = new BigDecimal("-622");
			big = big.add(big3);
			big2 = big2.divide(big1,30,BigDecimal.ROUND_FLOOR);
			big = big.multiply(big2);
			return big.toString();		
		}
		
		return big.toString();
	}
	
	public String sıcaklıkCevir(String birim1 , BigDecimal big , int type){
		String result = null;
		char c = birim1.charAt(0);
		if(type == 0){
			switch (c) {
				case 'C':     		// Hepsini C ye çeviriyor
					return big.toString();
				case 'F':
					BigDecimal big1 = new BigDecimal("5");
					BigDecimal big2 = new BigDecimal("9");
					BigDecimal big3 = new BigDecimal("-32");
					big1 = big1.divide(big2,30, BigDecimal.ROUND_FLOOR);
					big = big.add(big3);
					big1 = big1.multiply(big);
					return big1.toString();
				case 'K':
					BigDecimal big4 = new BigDecimal("-273.15");
					big = big.add(big4);
					return big.toString();
				case 'S':
					return big.toString();
				case 'R':
					if(birim1.equals("Rankine")){
						BigDecimal big6 = new BigDecimal("-491.67");
						BigDecimal big7 = new BigDecimal("1.8");
						big = big.add(big6);
						big = big.divide(big7,30, BigDecimal.ROUND_FLOOR);
						return big.toString();
					}
					else if(birim1.equals("Reaumur")){
						BigDecimal big5 = new BigDecimal("1.25");
						big = big.multiply(big5);
						return big.toString();
					}	
			}
		}
		else if(type == 1){
			switch (c) {
			case 'C':     		// Hepsini C ye çeviriyor
				return big.toString();
			case 'F':
				BigDecimal big1 = new BigDecimal("1.8");
				BigDecimal big3 = new BigDecimal("32");
				big = big.multiply(big1);
				big = big.add(big3);
				return big.toString();
			case 'K':
				BigDecimal big4 = new BigDecimal("273.15");
				big = big.add(big4);
				return big.toString();
			case 'S':
				return big.toString();
			case 'R':
				if(birim1.equals("Rankine")){
					BigDecimal big6 = new BigDecimal("491.67");
					BigDecimal big7 = new BigDecimal("1.8");
					big = big.multiply(big7);
					big = big.add(big6);
					return big.toString();
				}
				else if(birim1.equals("Reaumur")){
					BigDecimal big5 = new BigDecimal("0.8");
					big = big.multiply(big5);
					return big.toString();
				}	
			}		
		}
		return result;
	}
	//------------------------------------------------------------------------------------- Menu -----------------------------------------------------------------------------------------------------------------------------
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
			// TODO Auto-generated method stub
			MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.main, menu);
		    return true;
		}
		@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {

			case R.id.itemRefresh:

				onClickUpdateButton(null);
				
				return true;
			case R.id.MaxDecimal:
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
				alertBuilder.setTitle("Basamak Sayısı");
				alertBuilder.setMessage("En fazla basamak sayısı: ");
				final EditText input= new EditText(this);
				input.setText(max_number_of_digits);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				input.setLayoutParams(lp);
				alertBuilder.setView(input);
				alertBuilder.setPositiveButton("KAYDET", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						max_number_of_digits = input.getText().toString();
						setPreferences(isFirst,max_number_of_digits,jsonString);
						calculate();
					}
				});
			
				alertBuilder.setNegativeButton("KAPAT", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				alertBuilder.show();
				return true;
			default: 
				return true;
			}	
		}
	//------------------------------------------------------------------------------------- Menu -----------------------------------------------------------------------------------------------------------------------------

     public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		calculate();
     }

    //-------------------------------------------------------------------------------Shared Preferences -----------------------------------------------------------------------------------------------------------------
	public void setPreferences(int isFirst,String maxNumberOfDigits,String json){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("isFirst", isFirst);
		editor.putString("Json", json);
		editor.putString("MaxNumberOfDigits", maxNumberOfDigits);
		editor.commit();
	}
	public void readPreferences(){
		isFirst = settings.getInt("isFirst", 0);
		jsonString = settings.getString("Json","") ;
		max_number_of_digits = settings.getString("MaxNumberOfDigits", "6");
	}
	//-------------------------------------------------------------------------------Shared Preferences -----------------------------------------------------------------------------------------------------------------
	
	public String loadJSONFromAsset() {
        String json = null;
        try {
        	InputStream is = getAssets().open("units.json");
        	int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public boolean isOnline()
    {
    	   ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    		    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
    		        return true;
    		    }
    		    return false;
    }
  
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
	