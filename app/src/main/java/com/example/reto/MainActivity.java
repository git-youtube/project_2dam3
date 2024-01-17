package com.example.reto;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.osgeo.proj4j.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private ArrayList<String> itemList;
    private GoogleMap googleMap;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String urlIncidences = "https://api.euskadi.eus/traffic/v1.0/incidences";
    private String urlCameras = "https://api.euskadi.eus/traffic/v1.0/cameras";
    ArrayList<Incidencias> listaIncidencias = new ArrayList<>();
    ArrayList<Camaras> listaCamaras = new ArrayList<>();

    private ViewFlipper flipper;

    private ImageView imagenCamara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                // Después de llamar a getDataCameras() y getDataIncidences(), configura el OnMarkerClickListener
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // Obtén la información del marcador
                        Object tag = marker.getTag();
                        String title = marker.getTitle();

                        // Verifica si el marcador es de incidencia o cámara
                        if (title.equals("Incidencia")) {
                            // Marcador de incidencia
                            mostrarPopUp((String) tag);
                        } else {
                            // Marcador de cámara
                            mostrarPopUpCameras(title, (String) tag);
                        }

                        return true;
                    }
                });
            }

        });
        getDataCameras();
        getDataIncidences();
        ImageView imageView = findViewById(R.id.imageView);
        itemList = new ArrayList<>();
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getY() < e2.getY()) {
                    expandBottomDialog(imageView);
                    return true;
                } else if (e1.getX() < e2.getX()) {
                    expandBottomDialog(imageView);
                    return true;
                }
                return false;
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });



    }



    public void expandBottomDialog(View view) {
        // Crea el BottomSheetDialogFragment con la lista de elementos como argumento
        MyBottomSheetDialogFragment bottomSheet = MyBottomSheetDialogFragment.newInstance(itemList);
        bottomSheet.show(getSupportFragmentManager(), "MyBottomSheet");
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                showConfirmationDialog();
            }
        });
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage("¿Quieres realizar esta acción?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

//                        dialog.dismiss();
//
//                        // Añade un "Hola" a la lista de elementos
//                        itemList.add("Hola");
//
//                        // Crea el BottomSheetDialogFragment con la lista de elementos como argumento
//                        MyBottomSheetDialogFragment bottomSheet = MyBottomSheetDialogFragment.newInstance(itemList);
//                        bottomSheet.show(getSupportFragmentManager(), "MyBottomSheet");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getDataIncidences() {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, urlIncidences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray incidencesArray = jsonResponse.getJSONArray("incidences");

                    Log.d(TAG, "longitud: " + incidencesArray.length());


                    for (int i = 0; i < incidencesArray.length(); i++) {
                        try {
                            Log.d(TAG, "i: " + i);
                            JSONObject incidence = incidencesArray.getJSONObject(i);

                            String id = incidence.getString("incidenceId");
                            String tipo = incidence.getString("incidenceType");
                            String causa = incidence.getString("cause");
                            String ciudad = incidence.optString("cityTown", ""); // Utiliza optString para obtener el valor o una cadena vacía si no existe
                            String nivel = incidence.optString("incidenceLevel", ""); // Utiliza optString para obtener el valor o una cadena vacía si no existe
                            String carretera = incidence.getString("road");

                           // Omitir si la ciudad es una cadena vacía
                                Incidencias nuevaIncidencia = new Incidencias(id, tipo, causa, ciudad, nivel, carretera);

                                // Agregar la nueva incidencia a la lista
                                listaIncidencias.add(nuevaIncidencia);

                                // Obtener las coordenadas y agregar el marcador (opcional)
                                double latitude = Double.parseDouble(incidence.getString("latitude"));
                                double longitude = Double.parseDouble(incidence.getString("longitude"));
                                Log.d(TAG, "latitude: " + latitude);
                                Log.d(TAG, "longitude: " + longitude);
                                LatLng latLng = new LatLng(latitude, longitude);
                                String title = incidence.getString("incidenceType");
                                String IncidenceId = incidence.getString("incidenceId");
                                agregarMarcador(latLng, "Incidencia", IncidenceId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "Error en el índice: ");
                            Log.d(TAG, "Mensaje de error: " + e.getMessage());
                        }
                    }



                    // Ahora puedes trabajar con tu lista de incidencias (listaIncidencias)
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error en el índice: ");
                    Log.d(TAG, "Mensaje de error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void getDataCameras() {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, urlCameras, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray camerasArray = jsonResponse.getJSONArray("cameras");

                    Log.d(TAG, "longitud: " + camerasArray.length());

                    for (int i = 0; i < camerasArray.length(); i++) {
                        try {
                            Log.d(TAG, "i: " + i);
                            JSONObject cameraObject = camerasArray.getJSONObject(i);

                            String cameraId = cameraObject.getString("cameraId");
                            String cameraName = cameraObject.getString("cameraName");
                            String urlImage = cameraObject.optString("urlImage", ""); // Utiliza optString para obtener el valor o una cadena vacía si no existe
                            double latitude = Double.parseDouble(cameraObject.getString("latitude"));
                            double longitude = Double.parseDouble(cameraObject.getString("longitude"));
                            String road = cameraObject.getString("road");
                            String kilometer = cameraObject.getString("kilometer");
                            Camaras nuevaCamara = new Camaras(cameraId, cameraName, urlImage,latitude,longitude, road, kilometer);

                            // Agregar la nueva incidencia a la lista
                            listaCamaras.add(nuevaCamara);
                            LatLng transformedLatLng = calcularLatLong(longitude, latitude, "32630");
                            agregarMarcadorCamara(transformedLatLng, "Camara", cameraId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "Error en el índice: ");
                            Log.d(TAG, "Mensaje de error: " + e.getMessage());
                        }
                    }

                    // Ahora puedes trabajar con tu lista de cámaras
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error en el índice: ");
                    Log.d(TAG, "Mensaje de error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    // Método para agregar marcador de cámara al mapa
    private void agregarMarcadorCamara(LatLng latLng, String titulo, String cameraId) {
        // Lógica para agregar marcador de cámara al mapa, similar a la función agregarMarcador
        // Puedes personalizarlo según tus necesidades
        int iconR = R.drawable.map;

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(iconR);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(titulo)
                .icon(icon);

        // Asigna el cameraId como tag al marcador
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(cameraId);


        Log.d("Marcador", "Marcador de cámara agregado con éxito: " + cameraId);
    }

    private void agregarMarcador(LatLng latLng, String titulo, String incidenceId) {
        // Obtén el BitmapDescriptor personalizado desde la imagen en recursos
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.warning));

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(titulo)
                .icon(icon);

        // Asigna el incidenceId como tag al marcador
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(incidenceId);

        Log.d("Marcador", "Marcador agregado con éxito: " + incidenceId);
    }



    // Función para convertir un recurso de imagen en un Bitmap
    private Bitmap getBitmapFromDrawable(int drawableId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId, options);

        if (bitmap == null) {
            Log.e("Error", "Error al decodificar la imagen del recurso: " + drawableId);
        } else {
            Log.d("Imagen", "Imagen decodificada correctamente del recurso: " + drawableId);
        }

        return bitmap;
    }


    private void mostrarPopUp(final String incidenceId) {
        for (int i = 0; i < listaIncidencias.size(); i++) {
            Log.d(TAG, "mostrarPopUp: Hola");
            if (incidenceId == listaIncidencias.get(i).getId()) {

                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_pop_up);

                TextView tipoIncidencia = dialog.findViewById(R.id.tipoIncidencia);
                TextView causaIncidencia = dialog.findViewById(R.id.causaIncidencia);
                TextView ciudadIncidencia = dialog.findViewById(R.id.ciudadIncidencia);
                TextView nivelIncidencia = dialog.findViewById(R.id.nivelIncidencia);
                TextView carreteraIncidencia = dialog.findViewById(R.id.carreteraIncidencia);

                tipoIncidencia.setText("Tipo de incidente: " + listaIncidencias.get(i).getTipo());
                causaIncidencia.setText("Causa del incidente: " + listaIncidencias.get(i).getCausa());
                ciudadIncidencia.setText("Ciudad: " + listaIncidencias.get(i).getCiudad());
                nivelIncidencia.setText("Nivel de incidencia: " + listaIncidencias.get(i).getNivel());
                carreteraIncidencia.setText("Carretera: " + listaIncidencias.get(i).getCarretera());

                Button button = dialog.findViewById(R.id.btnAceptar);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // Mostrar el diálogo
                dialog.show();
            }
        }
    }



    private void mostrarPopUpCameras(String titulo, final String cameraId) {
        ImageView imagenCamara;
        for (int i = 0; i < listaCamaras.size(); i++) {
            if (cameraId.equals(listaCamaras.get(i).getCameraId())) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_pop_cameras);

                TextView nombreCamara = dialog.findViewById(R.id.nombreCamara);
                TextView carreteraCamara = dialog.findViewById(R.id.carretera);
                TextView kilometroCamara = dialog.findViewById(R.id.kilometro);

                nombreCamara.setText("Nombre Cámara: " + listaCamaras.get(i).getCameraName());
                carreteraCamara.setText("Carretera: " + listaCamaras.get(i).getRoad());
                kilometroCamara.setText("Kilómetro: " + listaCamaras.get(i).getKilometer());
                flipper=dialog.findViewById(R.id.viewFlipper);
                imagenCamara = dialog.findViewById(R.id.imageTransicion);
                imagenCamara.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.my_card_view2)));



                        WebView webView = dialog.findViewById(R.id.imageCamara);
                        WebSettings webSettings = webView.getSettings();
                        webSettings.setJavaScriptEnabled(true); // Habilita JavaScript si es necesario

                        String html = "<html><body><img src=\"" + "https://images.wikidexcdn.net/mwuploads/wikidex/thumb/a/ad/latest/20211225033009/EP1181_Gengar_de_Ash.png/640px-EP1181_Gengar_de_Ash.png" + "\" width=\"100%\" height=\"100%\"></body></html>";
                        webView.loadData(html, "text/html", "UTF-8");

                    }

                });
                Button button = dialog.findViewById(R.id.btnAceptar);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // Mostrar el diálogo
                dialog.show();
                break;  // Rompe el bucle después de mostrar el diálogo para evitar mostrar múltiples diálogos para la misma cámara
            }
        }
    }

    private LatLng calcularLatLong(double easting, double northing, String zone) {
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CRSFactory csFactory = new CRSFactory();

        String sourceCRSCode = "EPSG:" + zone; // UTM zone
        String targetCRSCode = "EPSG:4326"; // WGS84 LatLon

        CoordinateReferenceSystem sourceCRS = csFactory.createFromName(sourceCRSCode);
        CoordinateReferenceSystem targetCRS = csFactory.createFromName(targetCRSCode);

        CoordinateTransform trans = ctFactory.createTransform(sourceCRS, targetCRS);

        ProjCoordinate sourceCoord = new ProjCoordinate(easting, northing); // UTM coordinates
        ProjCoordinate targetCoord = new ProjCoordinate();

        trans.transform(sourceCoord, targetCoord);

        double transformedLatitude = targetCoord.y;
        double transformedLongitude = targetCoord.x;
        Log.d(TAG, "Long: "+transformedLongitude);
        Log.d(TAG, "Lat: "+transformedLatitude);
        return new LatLng(transformedLatitude, transformedLongitude);
    }

}





