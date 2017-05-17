package com.example.nawwa.geevsimplepublish.presenters;


import com.example.nawwa.geevsimplepublish.interfaces.IAnnounceRepository;
import com.example.nawwa.geevsimplepublish.interfaces.CreateAnnounceActivityView;
import com.example.nawwa.geevsimplepublish.objects.Announce;

/**
 * Created by nawwa on 15/05/2017.
 */

//activity call method here
public class CreateAnnounceActivityPresenter {
    private CreateAnnounceActivityView view;
    private IAnnounceRepository repository;

    public CreateAnnounceActivityPresenter(CreateAnnounceActivityView view, IAnnounceRepository repository){
        this.view = view;
        this.repository = repository;

    }

    private boolean isFormValid(){
        if (this.view.getAnnonceDesc().length() <= 0 ||
                this.view.getAnnonceTitle().length() <= 0){
            this.view.showPopupWarning("[Attention] Le titre ou la description est vide");
            return false;
        }
        if (this.view.getAnnonceRadioGroup() < 0){
            this.view.showPopupWarning("[Attention] Vous n'avez pas choisi l'état de l'objet");
            return false;
        }
        if (!this.view.isImageChanged()){
            this.view.showPopupWarning("[Attention] Vous n'avez pas choisi d'image");
            return false;
        }
        if (this.view.getLocation() == null){
            this.view.showPopupWarning("[Attention] Votre map ne fonctionne pas");
            this.view.canWeHaveLocation();
            this.view.gePhoneLocation();
            return false;
        }
        return true;
    }

    public void HandleButtonPublish() {
        if (!isFormValid())
            return;
        Announce new_announce = new Announce();

        new_announce.setTitle(this.view.getAnnonceTitle());
        new_announce.setDescription(this.view.getAnnonceDesc());
        new_announce.setImage(this.view.getImage().getDrawable());
        new_announce.setState(this.view.getAnnonceRadioGroup());
        new_announce.setLocation(this.view.getLocation());
        repository.addAnnounce(new_announce);
        this.view.showPopupSucces();
        this.view.resetForm();
    }

}
