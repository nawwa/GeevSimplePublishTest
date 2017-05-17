package com.example.nawwa.geevsimplepublish.interfaces;

import android.location.Location;
import android.widget.ImageButton;

/**
 * Created by nawwa on 15/05/2017.
 */

public interface CreateAnnounceActivityView {
    void        showPopupSucces();
    void        showPopupError();
    void        showPopupWarning(String message);
    void        resetForm();
    void        gePhoneLocation();
    void        canWeHaveLocation();

    //getter
    boolean     isImageChanged();
    String      getAnnonceTitle();
    String      getAnnonceDesc();
    Integer     getAnnonceRadioGroup();
    ImageButton getImage();
    Location    getLocation();




}
