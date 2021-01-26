package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

public class SeeProfileEvent {


    /**
     * Neighbour to show
     */
    public Neighbour neighbour;

    /**
     * Constructor.
     * @param neighbour
     */
    public SeeProfileEvent(Neighbour neighbour) {
        this.neighbour = neighbour;
    }
}
