package com.example.nawwa.geevsimplepublish.repositories;

import android.support.annotation.NonNull;

import com.example.nawwa.geevsimplepublish.interfaces.IAnnounceRepository;
import com.example.nawwa.geevsimplepublish.objects.Announce;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by nawwa on 15/05/2017.
 */

public class AnnounceRepository implements IAnnounceRepository {
    private ArrayList<Announce>          list = new ArrayList<Announce>();

    @Override
    public ArrayList<Announce> getAnnounces(){
        return list;
    }

    @Override
    public void addAnnounce(Announce objAd) {
        list.add(objAd);
    }

    @Override
    public void deleteAnnounce(Announce toDel) {
        list.remove(toDel);
    }

    @Override
    public void updateAnnounce(Announce toUp) {
        for (Announce i : list){
        }
    }

}
