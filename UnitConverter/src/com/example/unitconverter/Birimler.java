package com.example.unitconverter;




public class Birimler{
	
	public static String [] catagories = { "Açı" , "Ağırlık" ,  "Alan" , "Aydınlatma" , "Basınç" , "Bilgisayar" ,
			"Elektrik Akımı" , "Enerji" ,"Frekans" , "Güç" , " Hacim" , "Hız" ,
			"İvme" , "Kapasitans" , "Kuvvet" , "Parlaklık" , "Sıcaklık" ,   "Takvim Çevirici" ,
			"Uzunluk" , "Yoğunluk" , "Zaman" 
			};

	public static int[] photos = new int[]{
		R.drawable.aci_olculeri,
		R.drawable.agirlik_olculeri,
		R.drawable.alan_olculeri,
		R.drawable.basinc_olculeri,
		R.drawable.bilgisayar_olculeri,
		R.drawable.elektrik_akimi_olculeri,
		R.drawable.enerji_olculeri,
		R.drawable.frekans_olculeri,
		R.drawable.guc_olculeri,
		R.drawable.hacim_olculeri,
		R.drawable.hiz_olculeri,
		R.drawable.ivme_olculeri,
		R.drawable.elektrik_sigasi_olculeri,
		R.drawable.kuvvet_olculeri,
		R.drawable.aydinlatma_olculeri,
		R.drawable.parlaklik_olculeri,
		R.drawable.sicaklik_olculeri,
		R.drawable.miladi_hicri_takvim_cevirici,
		R.drawable.uzunluk_olculeri,
		R.drawable.yogunluk_olculeri,
		R.drawable.zaman_olculeri
	};

	/*
	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	builder.setTitle(name);
	WebView web = new WebView(MainActivity.this);
	WebSettings webSettings = web.getSettings();
	webSettings.setJavaScriptEnabled(true);
	webSettings.setLoadsImagesAutomatically(true);
	webSettings.setSupportZoom(true) ;
	webSettings.setBuiltInZoomControls(true);
	web.loadUrl(webUrl);
       builder.setNegativeButton("KAPAT", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
	
	web.setWebViewClient(new WebViewClient()
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }

    });
    builder.setView(web);
    AlertDialog alertDialog = builder.create();
    return alertDialog;
    */
    
	
	
    /*		currentCatagory.setText(catagoryList[arg2]+" Birimleri");
	JSONArray jsonElement = new JSONArray();
	try {
		jsonElement = jsonArray.getJSONObject(arg2).getJSONArray("Birimler");
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
	rightSpinner.setAdapter(spinnerAdapter);*/
    

};
