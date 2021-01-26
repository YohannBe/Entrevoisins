package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;


/**
 * Neighbour API client
 */
public interface NeighbourApiService {

    /**
     * Get all my Neighbours
     * @return {@link List}
     */
    List<Neighbour> getNeighbours();

    /**
     * Get all my favorite Neighbours
     * @return {@link List}
     */
    List<Neighbour> getNeighboursFavorite();

    /**
     * Deletes a neighbour
     * @param neighbour
     */
    void deleteNeighbour(Neighbour neighbour);

    /**
     * Create a neighbour
     * @param neighbour
     */
    void createNeighbour(Neighbour neighbour);

    /**
     * add a neighbour to favorite
     * @param neighbour
     */
    void addNeighbourToFavorite(Neighbour neighbour);

    /**
     * Deletes a neighbour from the favorite list
     * @param neighbour
     */
    void deleteFromFavoriteList(Neighbour neighbour);

}
