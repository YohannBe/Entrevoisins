package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

public class FavoriteEvent {

    /**
     * Neighbour to add to the favorite list
     */
    public Neighbour neighbour;
    public boolean action;

    /**
     * Constructor.
     * @param neighbour
     */
    public FavoriteEvent(Neighbour neighbour, boolean action) {
        this.neighbour = neighbour;
        this.action = action;
    }
}
