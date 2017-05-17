package com.example.nawwa.geevsimplepublish.fragments;


import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.nawwa.geevsimplepublish.R;
import com.example.nawwa.geevsimplepublish.interfaces.CreateAnnounceActivityView;
import com.example.nawwa.geevsimplepublish.presenters.CreateAnnounceActivityPresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class CreateAnnounceActivity extends Fragment
        implements CreateAnnounceActivityView,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private CreateAnnounceActivityPresenter presenter;

    private Button                          publishbtn;
    private ImageButton                     imageBtn;
    private EditText                        title;
    private EditText                        desc;
    private RadioGroup                      radioGroup;

    private boolean                         isImageChange;
    private int                             MAX_IMG_NB = 1;

    /* Variable pour les fonctionalités de la GoggleMap */
    GoogleMap                               mGoogleMap;
    private boolean                         mLocationPermissionGranted;
    private final   LatLng                  mDefaultLocation = new LatLng(48.866667, 2.333333);
    private Location                        mLastKnownLocation;
    private GoogleApiClient                 mGoogleApiClient;
    private CameraPosition                  mCameraPosition;
    private static final int                DEFAULT_ZOOM = 15;
    private static final int                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    View rootView;

    public CreateAnnounceActivity() {
        //il faut un constructeur vide lorsque on utilise les fragments
    }

    public void setPresenter(CreateAnnounceActivityPresenter presenter){
        this.presenter = presenter;
    }

    /*  Constructeur personnel pour init ce qu'on veut */
    private void ctor_CreateAnnounceActivity(){
        publishbtn = (Button) rootView.findViewById(R.id.buttonPusblish);
        imageBtn = (ImageButton) rootView.findViewById(R.id.imageContainer);
        title = (EditText) rootView.findViewById(R.id.textTitleObject);
        desc = (EditText) rootView.findViewById(R.id.textDescObject);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroupState);

        publishbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.HandleButtonPublish();
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Choisissez une image"), MAX_IMG_NB);
            }
        });

        /* Création et connexion à l'API GooglePlayServices */
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }


    /* Methode qui va être appeler lorsque la vue du fragement va être créer, lorsque la vue est disponible
    *  alors on peut appeler notre construceur privé et initialiser les variables.
    * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create_announce, container, false);
        ctor_CreateAnnounceActivity();
        return rootView;
    }


    private Bitmap      resizeImage(Uri uri){
        int             inWidth, inHeight = 0;
        int             dstWidth = imageBtn.getWidth();
        int             dstHeight = imageBtn.getHeight();
        InputStream     in;
        Bitmap          decodedBitmap;

        try
        {
            in = this.getActivity().getApplicationContext().getContentResolver().openInputStream(uri);

            // On decode avec "inJustDecodeBounds = true" pour n'avoir que les metadata
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();

            // Sauvegarde la taille de l'image original
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // on décode l'image avec la taille qu'on souhaite
            in = this.getActivity().getApplicationContext().getContentResolver().openInputStream(uri);
            options = new BitmapFactory.Options();
            options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dstHeight);
            decodedBitmap = BitmapFactory.decodeStream(in, null, options);

            return  decodedBitmap;
        }
        catch (Exception e)
        {
            Log.e("ImageProblem", e.getMessage(), e);
            return null;
        }
    }

    /* Callback appelé lorsque l'utilisateur choisi son image */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAX_IMG_NB && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            imageBtn.setImageBitmap(resizeImage(uri));
            isImageChange = true;
        }
    }


    private AlertDialog buildDialog(String message, String btnText, boolean cancel) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        alertDialogBuilder
                .setMessage(message)
                .setCancelable(cancel)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return alertDialogBuilder.create();
    }

    @Override
    public void showPopupSucces() {
        AlertDialog alertDialog = buildDialog("[OK] Votre objet à été correctement publié", "OK", false);
        alertDialog.show();
    }

    @Override
    public void showPopupError(){
        AlertDialog alertDialog = buildDialog("[Erreur] Votre objet n'est pas publié", "OK", false);
        alertDialog.show();
    }

    @Override
    public void showPopupWarning(String message){
        AlertDialog alertDialog = buildDialog(message, "OK", false);
        alertDialog.show();
    }

    @Override
    public void resetForm(){
        title.setText("");
        desc.setText("");
        radioGroup.clearCheck();
        imageBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_image_default, null));
        isImageChange = false;
    }

    /* Serie de getter pour que le presenter puisse utiliser les données */
    @Override
    public boolean isImageChanged(){return isImageChange;}
    @Override
    public String getAnnonceTitle(){ return title.getText().toString();}
    @Override
    public String getAnnonceDesc() {return desc.getText().toString();}
    @Override
    public Integer getAnnonceRadioGroup() { return radioGroup.indexOfChild(this.rootView.findViewById(radioGroup.getCheckedRadioButtonId()));}
    @Override
    public ImageButton getImage() {return imageBtn;}
    @Override
    public Location getLocation() {return mLastKnownLocation;}


    /**
     * MAP FUNCTIONS
     */


    /* On va build la map seulement quand le client du GooglePlayService est bien connecté */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapF);
        /* Declenge onMapReady() lorsque la map est prête*/
        mapFragment.getMapAsync(this);
    }

    /* On peut commencer à utiliser la map quand elle est dispo */
    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        /* Demande de la permission de localiser le télephone à l'user */
        canWeHaveLocation();

        /* On va maintenant localiser le telephone */
        gePhoneLocation();

    }

    @Override
    public void gePhoneLocation() {

        /* Regarde si on a la permission, au cas ou */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // On bouge la camera de la map vers la position du téléphone si on a la permission
        if (mCameraPosition != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d("MAP", "Using defautl loc");
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

    }

    public void canWeHaveLocation() {
        if (mGoogleMap == null) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("BUG", "Connexion suspendu");
    }

    /* Quand la connection au service GooglePlay n'a pas réussi */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("BUG", "Connexion failed" + connectionResult.getErrorCode());
        AlertDialog alertDialog = buildDialog("Erreur] Activez votre GPS et votre connexion internet si vous voulez accedez à la map", "OK", false);
        alertDialog.show();
    }
}
