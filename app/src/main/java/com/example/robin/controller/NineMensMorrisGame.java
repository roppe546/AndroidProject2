package com.example.robin.controller;

import com.example.robin.test.NineMensMorrisView;

/**
 * Created by Peonsson on 2015-12-01.
 */

/**
 * This class handles the flow of the game.
 */
public class NineMensMorrisGame {

    private NineMensMorrisView view;

    public NineMensMorrisGame(NineMensMorrisView view) {
        this.view = view;
        clearObsoleteCode();
    }

    private void clearObsoleteCode() {
        view.getCheckers().clear();
    }

}
