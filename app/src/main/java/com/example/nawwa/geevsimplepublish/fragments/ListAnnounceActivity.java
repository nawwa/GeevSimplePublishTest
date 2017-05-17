package com.example.nawwa.geevsimplepublish.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nawwa.geevsimplepublish.R;
import com.example.nawwa.geevsimplepublish.interfaces.ListAnnounceActivityView;
import com.example.nawwa.geevsimplepublish.presenters.ListAnnounceActivityPresenter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListAnnounceActivity extends Fragment implements ListAnnounceActivityView {
    private View                                rootView;
    private ListAnnounceActivityPresenter       presenter;
    private ListView                            listView;

    public ListAnnounceActivity() {
        // Besoin d'un constructeur vide pour les fragements
    }

    public void setPresenter(ListAnnounceActivityPresenter presenter){
        this.presenter = presenter;
    }

    /* Constructeur personnel pour init ce qu'on veut*/
    private void ctor_ListAnnounceActivity(){
        listView = (ListView) this.rootView.findViewById(R.id.listAnnounce);
    }


    /* Methode qui va être appeler lorsque la vue du fragement va être créer, lorsque la vue est disponible
    *  alors on peut appeler notre construceur privé et initialiser les variables.
    * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_list_announce, container, false);
        ctor_ListAnnounceActivity();
        return rootView;
    }

    /* Fonction appelé par la View lors du focus de ce fragment*/
    public void onFocus(){
        loadAnnounce();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadAnnounce();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAnnounce();
    }

    @Override
    public void loadAnnounce(){
        presenter.getAnnounces();
    }

    /* Fonction appelé par le Presenter, affiche les annonce dans une liste*/
    @Override
    public void showAnnounce(ArrayList<String> listItems) {
        ArrayAdapter ad = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, listItems);
        this.listView.setAdapter(ad);
    }
}
