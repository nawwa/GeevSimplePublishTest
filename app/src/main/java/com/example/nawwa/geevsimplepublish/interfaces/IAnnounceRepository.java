package com.example.nawwa.geevsimplepublish.interfaces;

import com.example.nawwa.geevsimplepublish.objects.Announce;

import java.util.List;

/**
 * Created by nawwa on 15/05/2017.
 */

public interface IAnnounceRepository {
    List<Announce> getAnnounces();
    void addAnnounce(Announce objAd);
    void deleteAnnounce(Announce toDel);
    void updateAnnounce(Announce toUp);
}
