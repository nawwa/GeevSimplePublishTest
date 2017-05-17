package com.example.nawwa.geevsimplepublish.presenters;

import com.example.nawwa.geevsimplepublish.interfaces.IAnnounceRepository;
import com.example.nawwa.geevsimplepublish.interfaces.ListAnnounceActivityView;
import com.example.nawwa.geevsimplepublish.objects.Announce;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nawwa on 15/05/2017.
 */

public class ListAnnounceActivityPresenter {

    private ListAnnounceActivityView view;
    private IAnnounceRepository repository;

    public ListAnnounceActivityPresenter(ListAnnounceActivityView view, IAnnounceRepository repository){
        this.view = view;
        this.repository = repository;
    }

    public void getAnnounces(){
        List<Announce> l = this.repository.getAnnounces();
        if (l == null || l.isEmpty()){
            return ;
        }
        ArrayList<String> Titles = new ArrayList<String>();
        for (final Announce An : l ) {
            Titles.add(An.getTitle() + " - " + An.getDescription()
                    + " - [ " + new Double(An.getLocation().getLatitude()).toString() + ","
                    + new Double(An.getLocation().getLongitude()).toString() + "]");
        }
        this.view.showAnnounce(Titles);
    }
}
