package com.uminho.uce15.cityroots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import android.location.Location;
import android.location.LocationManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.TableRow;

public class ListarPOIs extends ActionBarActivity {

    private LocationManager locMan;
    private Marker userMarker;
    private Marker[] placeMarkers;
    private final int MAX_PLACES = 20;
    private MarkerOptions[] places;

    private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;
    private GoogleMap theMap=null;

    private ListView list;
    private Lista adapter;

    JSONObject[] pontos=null;
    String[] web =null;
    //String[] web= {"1","2","3","4"};
    Integer[] imageId = {
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_listar_pois);
        SlidingPaneLayout slidingPaneLayout = (SlidingPaneLayout)findViewById(R.id.pane);
        final ListView listView = (ListView) findViewById(R.id.list);
        final SupportMapFragment supportMapFragment = (SupportMapFragment)
                this.getSupportFragmentManager().findFragmentById(R.id.map);

        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                view.invalidate();
                listView.invalidate();
            }

            @Override
            public void onPanelOpened(View view) {

            }

            @Override
            public void onPanelClosed(View view) {

            }
        });

        userIcon = R.drawable.mark_blue;
        //foodIcon = R.drawable.red_point;
        //drinkIcon = R.drawable.blue_point;
        //shopIcon = R.drawable.green_point;
        otherIcon = R.drawable.green_point;

        if(theMap==null){
            //map not instantiated yet
            SupportMapFragment mapFrag=
                    (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            theMap=mapFrag.getMap();

        }
        if(theMap != null){
            //ok - proceed
            theMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            placeMarkers = new Marker[MAX_PLACES];
            updatePlaces();
        }

        list=(ListView)findViewById(R.id.list);
        list.setEmptyView(findViewById(android.R.id.empty));

        /*String readBD = readBD();

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(readBD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        //adapter = new Lista(this,web,imageId);
        //list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem

                // Starting new intent


                Intent intent = new Intent(ListarPOIs.this, DetalhesPOI.class);
                intent.putExtra("id",pontos[position].toString());
                //intent.putExtra("id",pos.toString());

                // Sending place refrence id to single place activity
                // place refrence id used to get "Place full details"
                startActivity(intent);
            }
        });

    }


    private void updatePlaces(){
        if(userMarker!=null) userMarker.remove();
        //update location
        //locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //double lat = lastLoc.getLatitude();
        //double lng = lastLoc.getLongitude();
        double lat=41.561653;
        double lng=-8.397139;

        LatLng lastLatLng = new LatLng(lat, lng);

        userMarker = theMap.addMarker(new MarkerOptions()
                .position(lastLatLng)
                .title("Está aqui")
                .icon(BitmapDescriptorFactory.fromResource(userIcon))
                .snippet("Sua última localização"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(lastLatLng)
                .zoom(12)                   // Sets the zoom
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        theMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));




        //theMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);

        String types = "food|bar|store|museum|art_gallery";
        try {
            types = URLEncoder.encode(types, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		/*String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
		"json?location="+lat+","+lng+
		"&radius=1000&sensor=true" +
		"&types=" + types +
		"&key=AIzaSyCSbIjUOgbOhQ9JJ4njs3hyPkdkyhXBxnQ";*/

        String placesSearchStr = "http://193.136.19.202:8080/attractions.json";

        //String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=41.561653,-8.397139&radius=1000&sensor=true&types=bar&key=AIzaSyCSbIjUOgbOhQ9JJ4njs3hyPkdkyhXBxnQ";

        new GetPlaces().execute(placesSearchStr);
        //locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
    }

    private class GetPlaces extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch places

            //build result as string
            StringBuilder placesBuilder = new StringBuilder();
            //process search parameter string(s)
            for (String placeSearchURL : placesURL) {
                HttpClient placesClient = new DefaultHttpClient();
                try {
                    //try to fetch the data

                    //HTTP Get receives URL string
                    HttpGet placesGet = new HttpGet(placeSearchURL);
                    //execute GET with Client - return response
                    HttpResponse placesResponse = placesClient.execute(placesGet);
                    //check response status
                    StatusLine placeSearchStatus = placesResponse.getStatusLine();
                    //only carry on if response is OK
                    if (placeSearchStatus.getStatusCode() == 200) {
                        //get response entity
                        HttpEntity placesEntity = placesResponse.getEntity();
                        //get input stream setup
                        InputStream placesContent = placesEntity.getContent();
                        //create reader
                        InputStreamReader placesInput = new InputStreamReader(placesContent);
                        //use buffered reader to process
                        BufferedReader placesReader = new BufferedReader(placesInput);
                        //read a line at a time, append to string builder
                        String lineIn;
                        while ((lineIn = placesReader.readLine()) != null) {
                            placesBuilder.append(lineIn);
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            return placesBuilder.toString();
            //return "{\"attraction\":[{\"id\":1,\"name\":\"Bom Jesus\",\"description\":\"A Igreja do Bom Jesus foi desenhada pelo arquitecto Carlos Amarante, por encomenda do Arcebispo D. Gaspar de Bragança, para substituir uma primitiva igreja, mandada construir por D. Rodrigo de Moura Teles que se encontrava em ruínas. As obras começaram em 1 de Junho de 1784, tendo ficado concluídas em 1811.\\nÉ um dos primeiros edifícios neoclássicos em Portugal, e a fachada é ladeada por duas torres e termina num frontão triangular.\",\"schedule\":null,\"site\":\"www.estanciadobomjesus.com\",\"email\":\"geral@estanciadobomjesus.com\",\"address\":\"Monte do Bom Jesus 4715-056 Braga\",\"latitude\":41.5263,\"longitude\":-8.39647,\"transport\":null,\"active\":true,\"timestamp\":1387218092,\"reference_point\":true,\"price\":null,\"attraction_type_id\":5,\"city_id\":3,\"web_user_id\":1},{\"id\":2,\"name\":\"Sé de Braga\",\"description\":\"A Sé Catedral é considerada como um centro de irradiação episcopal e um dos mais importantes templos do românico português, a sua história remonta à obra do primeiro bispo, D. Pedro de Braga, correspondendo à restauração da Sé episcopal em 1070, de que não se conservam vestígios.\\nNesta catedral encontram-se os túmulos de Henrique de Borgonha e sua mulher, Teresa de Leão, os condes do Condado Portucalense, pais do rei D. Afonso Henriques.\",\"schedule\":null,\"site\":\"www.se-braga.pt\",\"email\":\"info@se-braga.pt, tmsb.educar@gmail.com\",\"address\":\"Monte do Bom Jesus 4715-056 Braga\",\"latitude\":41.5438,\"longitude\":-8.43046,\"transport\":null,\"active\":true,\"timestamp\":1387218092,\"reference_point\":true,\"price\":null,\"attraction_type_id\":5,\"city_id\":3,\"web_user_id\":1},{\"id\":3,\"name\":\"Tesouro - Museu da Sé de Braga\",\"description\":\"As coleções do Tesouro-Museu da Sé de Braga (TMSB) testemunham, no seu conjunto, mais de XV séculos da história da Arte e da vida da Igreja em Braga. Em formação desde a sua fundação, em 1930, o TMSB acolhe um valioso espólio, constituído por coleções de cerâmica, escultura, medalhística, mobiliário, numismática, ourivesaria, pintura, têxtil.\\nA Exposição Permanente, Raízes de Eternidade. Jesus Cristo – Uma Igreja, consagrada à arte sacra, permite, através dos diferentes núcleos, revisitar a vida de Jesus Cristo e a história da Igreja em Braga. Esta é contada tomando como referência alguns arcebispos, desde o século V até ao século XX. A narração é complementada com os núcleos dedicados à paramentaria e ourivesaria.\",\"schedule\":\"De 3ª feira a domingo\\nEncerra à 2ª feira.\\n09h00-12h30 | 14h00-17h30 (18h30 no Verão)\",\"site\":\"www.se-braga.pt/tesouro_museu.php\",\"email\":\"catedralbraga@hotmail.com,tmsb.educar@gmail.com\",\"address\":\"Tesouro-Museu da Sé de Braga\\nR. D. Paio Mendes, s/n 4700-424 Braga\",\"latitude\":41.5474,\"longitude\":-8.42085,\"transport\":null,\"active\":true,\"timestamp\":1387218092,\"reference_point\":false,\"price\":\"Adultos e crianças a partir dos 10 anos: 3 €\\nCrianças de 6 a 10 anos: 50% de desconto\\nCrianças e jovens integrados em visitas escolares e acompanhantes: 50% de desconto\\nCrianças com menos de 6 anos: gratuito\",\"attraction_type_id\":6,\"city_id\":3,\"web_user_id\":1},{\"id\":4,\"name\":\"Santuário do Sameiro\",\"description\":\"O Santuário de Nossa Senhora do Sameiro é um santuário mariano localizado em Braga, Portugal, cuja construção se iniciou a 14 de Julho de 1863. O fundador deste santuário foi o vigário de Braga, Padre Martinho António Pereira da Silva.\",\"schedule\":null,\"site\":null,\"email\":null,\"address\":null,\"latitude\":41.554,\"longitude\":8.376,\"transport\":null,\"active\":true,\"timestamp\":1387218092,\"reference_point\":true,\"price\":\"\",\"attraction_type_id\":5,\"city_id\":3,\"web_user_id\":1},{\"id\":5,\"name\":\"Mosteiro de S. Martinho de Tibães\",\"description\":\"O Mosteiro de S. Martinho de Tibães, antiga Casa-Mãe da Congregação Beneditina Portuguesa, situa-se na região norte de Portugal, a 6 kms a noroeste de Braga.\",\"schedule\":\"todos os dias - 09.30 às 18.00 horas exceto-  1 de Janeiro, Dia de Páscoa, 1 de Maio e 25 de Dezembro\",\"site\":\"http://www.mosteirodetibaes.org/\",\"email\":\"msmtibaes@culturanorte.pt\",\"address\":\"Rua do Mosteiro 4700-565 Mire de Tibães\",\"latitude\":41.566,\"longitude\":8.477,\"transport\":null,\"active\":true,\"timestamp\":1387218092,\"reference_point\":true,\"price\":\"Bilhete normal € 4,00 Bilhete com 50% de desconto (Reformados e jovens entre os 15 e os 25 anos) Aos Domingos de manhã a entrada é gratuita\",\"attraction_type_id\":5,\"city_id\":3,\"web_user_id\":1},{\"id\":6,\"name\":\"Museu dos Biscainhos\",\"description\":\"O Palácio dos Biscainhos localiza-se na freguesia da Sé, cidade e concelho de Braga.\",\"schedule\":\"10.00h-12h15m / 14.00h-17h30m\",\"site\":null,\"email\":null,\"address\":\"Rua dos Biscaínhos, 4700-415 Braga\",\"latitude\":41.566,\"longitude\":8.477,\"transport\":null,\"active\":true,\"timestamp\":1387218092,\"reference_point\":false,\"price\":\"\",\"attraction_type_id\":6,\"city_id\":3,\"web_user_id\":1},{\"id\":7,\"name\":\"Museu Pio XII\",\"description\":\"O Museu Pio XII é um museu dedicado à Arte Sacra e Arqueologia.\",\"schedule\":\"09:30–12:30, 14:30–18:00\",\"site\":null,\"email\":null,\"address\":\"Largo de San Tiago 47, 4704-532 Braga\",\"latitude\":41.566,\"longitude\":8.477,\"transport\":null,\"active\":true,\"timestamp\":1387218092,\"reference_point\":false,\"price\":\"\",\"attraction_type_id\":6,\"city_id\":3,\"web_user_id\":1},{\"id\":8,\"name\":\"Museu dos Cordofones\",\"description\":\"O Museu dos Cordofones pertence ao artesão Domingos Machado, foi inaugurado a 22 de Setembro de 1995. É um museu de referência em Portugal sobre os cordofones.\",\"schedule\":null,\"site\":null,\"email\":null,\"address\":\"Rua de Santo António 3, 4705-630 Tebosa - Braga\",\"latitude\":41.586,\"longitude\":8.577,\"transport\":null,\"active\":true,\"timestamp\":1387218092,\"reference_point\":false,\"price\":\"\",\"attraction_type_id\":6,\"city_id\":3,\"web_user_id\":1}],\"photo_attraction\":[{\"id\":1,\"url\":\"http://static4.depositphotos.com/1000160/276/i/950/depositphotos_2763524-Bom-Jesus-de-Braga-in-Portugal.jpg\",\"name\":\"amet,\",\"description\":null,\"extension\":\"jpg\",\"attraction_id\":1},{\"id\":2,\"url\":\"http://fotos.sapo.pt/topazio1950/pic/0004c6z0\",\"name\":\"amet,\",\"description\":null,\"extension\":\"jpg\",\"attraction_id\":2},{\"id\":3,\"url\":\"http://fotos.sapo.pt/topazio1950/pic/0004c6z0\",\"name\":\"amet,\",\"description\":null,\"extension\":\"jpg\",\"attraction_id\":3}]}";
        }
        //process data retrieved from doInBackground
		/*protected void onPostExecute(String result) {
			//parse place data returned from Google Places
			//remove existing markers
			if(placeMarkers!=null){
				for(int pm=0; pm<placeMarkers.length; pm++){
					if(placeMarkers[pm]!=null)
						placeMarkers[pm].remove();
				}
			}
			try {
				//parse JSON

				//create JSONObject, pass stinrg returned from doInBackground
				JSONObject resultObject = new JSONObject(result);
				//get "results" array
				JSONArray placesArray = resultObject.getJSONArray("results");
				//marker options for each place returned
				places = new MarkerOptions[placesArray.length()];
				//loop through places
				for (int p=0; p<placesArray.length(); p++) {
					//parse each place
					//if any values are missing we won't show the marker
					boolean missingValue=false;
					LatLng placeLL=null;
					String placeName="";
					String vicinity="";
					int currIcon = otherIcon;
					try{
						//attempt to retrieve place data values
						missingValue=false;
						//get place at this index
						JSONObject placeObject = placesArray.getJSONObject(p);
						//get location section
						JSONObject loc = placeObject.getJSONObject("geometry")
								.getJSONObject("location");
						//read lat lng
						placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
								Double.valueOf(loc.getString("lng")));
						//get types
						JSONArray types = placeObject.getJSONArray("types");
						//loop through types
						for(int t=0; t<types.length(); t++){
							//what type is it
							String thisType=types.get(t).toString();
							//check for particular types - set icons
							if(thisType.contains("food")){
								currIcon = foodIcon;
								break;
							}
							else if(thisType.contains("bar")){
								currIcon = drinkIcon;
								break;
							}
							else if(thisType.contains("store")){
								currIcon = shopIcon;
								break;
							}
						}
						//vicinity
						vicinity = placeObject.getString("vicinity");
						//name
						placeName = placeObject.getString("name");
					}
					catch(JSONException jse){
						Log.v("PLACES", "missing value");
						missingValue=true;
						jse.printStackTrace();
					}
					//if values missing we don't display
					if(missingValue)	places[p]=null;
					else
						places[p]=new MarkerOptions()
					.position(placeLL)
					.title(placeName)
					.icon(BitmapDescriptorFactory.fromResource(currIcon))
					.snippet(vicinity);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			if(places!=null && placeMarkers!=null){
				for(int p=0; p<places.length && p<placeMarkers.length; p++){
					//will be null if a value was missing
					if(places[p]!=null)
						placeMarkers[p]=theMap.addMarker(places[p]);
				}
			}

		}*/

        protected void onPostExecute(String result) {
            //parse place data returned from Google Places
            //remove existing markers
            if(placeMarkers!=null){
                for(int pm=0; pm<placeMarkers.length; pm++){
                    if(placeMarkers[pm]!=null)
                        placeMarkers[pm].remove();
                }
            }
            try {
                //parse JSON

                //create JSONObject, pass stinrg returned from doInBackground
                JSONObject placesobj=new JSONObject(result);
                JSONArray placesArray = placesobj.getJSONArray("attraction");
                //get "results" array
                //marker options for each place returned
                pontos=new JSONObject[placesArray.length()];
                web=new String[placesArray.length()];
                places = new MarkerOptions[placesArray.length()];
                //loop through places
                for (int p=0; p<placesArray.length(); p++) {
                    //parse each place
                    //if any values are missing we won't show the marker
                    boolean missingValue=false;
                    LatLng placeLL=null;
                    String placeName="";
                    String vicinity="";
                    int currIcon = otherIcon;
                    try{
                        //attempt to retrieve place data values
                        missingValue=false;
                        //get place at this index
                        JSONObject placeObject = placesArray.getJSONObject(p);
                        //get location section
                        //read lat lng
                        placeLL = new LatLng(Double.valueOf(placeObject.getString("latitude")),
                                Double.valueOf(placeObject.getString("longitude")));

                        vicinity =placeObject.getString("address");
                        //name
                        placeName = placeObject.getString("name");
                        web[p]=placeObject.getString("name");
                        pontos[p]=placeObject;
                    }
                    catch(JSONException jse){
                        Log.v("JSON", "missing value");
                        missingValue=true;
                        jse.printStackTrace();
                    }
                    //if values missing we don't display
                    if(missingValue)	places[p]=null;
                    else
                        places[p]=new MarkerOptions()
                                .position(placeLL)
                                .title(placeName)
                                .icon(BitmapDescriptorFactory.fromResource(currIcon))
                                .snippet(vicinity);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(places!=null && placeMarkers!=null){
                for(int p=0; p<places.length && p<placeMarkers.length; p++){
                    //will be null if a value was missing
                    if(places[p]!=null)
                        placeMarkers[p]=theMap.addMarker(places[p]);
                }
            }

            adapter = new Lista(ListarPOIs.this,web,imageId);
            list.setAdapter(adapter);
        }
    }

    public String readBD() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://193.136.19.202:8080/attractions.json");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(ListarPOIs.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listar_pois, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public void onClickPOI(View view){
        //Integer pos=list.getSelectedItemPosition();
        Intent intent = new Intent(this, DetalhesPOI.class);
        intent.putExtra("id",pontos[0].toString());
        //intent.putExtra("id",pos.toString());
        startActivity(intent);
    }*/
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_listar_pois, container, false);
            return rootView;
        }
    }

    public void onLocationChanged(Location location) {
        Log.v("MainActivity", "location changed");
        updatePlaces();
    }
    public void onProviderDisabled(String provider){
        Log.v("MainActivity", "provider disabled");
    }
    public void onProviderEnabled(String provider) {
        Log.v("MainActivity", "provider enabled");
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v("MainActivity", "status changed");
    }


	/*@Override
	protected void onResume() {
		super.onResume();
		if(theMap!=null){
			locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(theMap!=null){
			locMan.removeUpdates(this);
		}
	}*/

}
