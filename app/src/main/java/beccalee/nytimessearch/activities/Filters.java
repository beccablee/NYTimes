package beccalee.nytimessearch.activities;

import java.io.Serializable;

/**
 * Created by beccalee on 6/24/16.
 */
public class Filters implements Serializable{

    String beginDate;
    String sort;

    Boolean arts = false;
    Boolean fashion = false;
    Boolean health = false;
    Boolean sports = false;

    public Filters(String beginDate, String sort, Boolean arts, Boolean fashion, Boolean health, Boolean sports) {
        this.beginDate = beginDate;
        this.sort = sort;
        this.arts = arts;
        this.fashion = fashion;
        this.health = health;
        this.sports = sports;
    }

    public Filters() {

    }
}
