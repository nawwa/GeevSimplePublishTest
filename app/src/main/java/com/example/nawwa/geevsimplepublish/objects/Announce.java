package com.example.nawwa.geevsimplepublish.objects;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;

/**
 * Created by nawwa on 15/05/2017.
 */

public class            Announce {
    private String      description;
    private String      title;
    private Drawable    image;
    private Integer     state;
    private Location    location;

    public Announce(){

    }

    public String getDescription() {return this.description;}
    public void setDescription(String description){ this.description = description;}

    public String getTitle() {return this.title;}
    public void setTitle(String title){this.title = title;}

    public Integer getState() {return this.state;}
    public void setState(Integer state){this.state = state;}

    public Drawable getImage(){return this.image;}
    public void setImage(Drawable image){this.image = image;}

    public Location getLocation(){return this.location;}
    public void setLocation(Location location){this.location = location;}

}
