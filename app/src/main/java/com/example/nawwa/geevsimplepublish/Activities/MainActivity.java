package com.example.nawwa.geevsimplepublish.Activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nawwa.geevsimplepublish.R;
import com.example.nawwa.geevsimplepublish.fragments.CreateAnnounceActivity;
import com.example.nawwa.geevsimplepublish.fragments.ListAnnounceActivity;
import com.example.nawwa.geevsimplepublish.interfaces.IAnnounceRepository;
import com.example.nawwa.geevsimplepublish.presenters.CreateAnnounceActivityPresenter;
import com.example.nawwa.geevsimplepublish.presenters.ListAnnounceActivityPresenter;
import com.example.nawwa.geevsimplepublish.repositories.AnnounceRepository;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter                mSectionsPagerAdapter;
    private ViewPager                           mViewPager;

    /* Declaration des deux View */
    private  ListAnnounceActivity               listAnnounceActivity;
    private  CreateAnnounceActivity             createAnnounceActivity;

    /* Declaration des deux Presenter */
    private CreateAnnounceActivityPresenter     createAnnouncePresenter;
    private ListAnnounceActivityPresenter       listAnnouncePresenter;

    /* Declaration du Repositry (model de données)*/
    private IAnnounceRepository                 annonceRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //Listener pour savoir quand le switch de fragement s'effectue
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            /* Fonction appelé lors du changement de fragment
            *  Appel de onFocus() pour refresh la liste des annonces
            * */
            @Override
            public void onPageSelected(int position) {

                if (listAnnounceActivity != null && position == 1)
                    listAnnounceActivity.onFocus();
            }
                @Override
                public void onPageScrollStateChanged(int state) {
                }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /* Création du répository (qui contient les annonces) */
        annonceRepository = new AnnounceRepository();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /* Fonction appelé lors de la création des fragements*/
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    /* On créer le fragment (la View) qui contient le form de creation d'annonce */
                    createAnnounceActivity = new CreateAnnounceActivity();

                    /* On créer le presenter, et on lui donne une reference de la View et la collection d'annonce(les données)
                    *  Il va donc interagir avec la vue et remplir la collection avec ce que l'user a entré
                    * */
                    createAnnouncePresenter = new CreateAnnounceActivityPresenter(createAnnounceActivity, annonceRepository);

                    /* On set le presenter dans la View, car la vue en à besoin lors du clique sur le bouton 'publier'
                     * la View va donc appeler une methode dans le presenter, pour vérifier que les entrées utilisateur sont bonne
                    * */
                    createAnnounceActivity.setPresenter(createAnnouncePresenter);

                    /* On retourne le fragment*/
                    return createAnnounceActivity;
                case 1:
                    /* On fait la meme chose avec l'autre fragement (La View de la liste d'annonce) */
                    listAnnounceActivity = new ListAnnounceActivity();
                    listAnnouncePresenter = new ListAnnounceActivityPresenter(listAnnounceActivity, annonceRepository);
                    listAnnounceActivity.setPresenter(listAnnouncePresenter);
                    return listAnnounceActivity;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Créer une annonce";
                case 1:
                    return "Liste de mes annonces";
                default:
            }
            return null;
        }
    }
}
