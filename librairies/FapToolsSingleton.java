package com.app.fap.librairies;

import com.app.fap.models.User;

/****************************************************
 * Created by Tahiana-MadiApps on 08/08/2016.
 ****************************************************/
public class FapToolsSingleton {

    private User user;
    public long idUser;

    private static FapToolsSingleton instance;

    public static FapToolsSingleton getInstance(){
        if(instance==null){
            instance = new FapToolsSingleton();
        }
        return instance;
    }

    public static void destroyInstance(){
        instance=null;
    }

    public FapToolsSingleton() {
    }


   /* public long getIdUser() {
        return idUser;
    }*/

    /*public void setIdUser(long idUser) {
        this.idUser = idUser;
    }*/

   /* public User getCurrentUser(){
        return user;
    }*/

   /* public void setUser(User user) {
        this.user = user;
        if(this.user!=null)
            this.idUser=user.getIdUser();
    }*/
}
