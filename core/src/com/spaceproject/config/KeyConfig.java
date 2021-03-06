package com.spaceproject.config;

import com.badlogic.gdx.Input.Keys;

public class KeyConfig extends Config {
    //Todo: controls shouldn't be limited to keys. what about mapping to mouse and keyboard or controller
    
    //---player controls---
    public int forward;
    public int right;
    public int left;
    public int back;
    public int alter;
    public int attack;
    public int defend;
    public int changeVehicle;
    public int land;
    public int actionA;
    public int actionB;
    public int actionC;
    
    //---UI controls---
    public int toggleHUD;
    public int toggleEdgeMap;
    public int toggleSpaceMap;
    
    //---screen controls---
    public int fullscreen;
    public int vsync;
    public int zoomOut;
    public int zoomIn;
    public int resetZoom;
    public int zoomCharacter;
    public int zoomSpace;
    public int rotateLeft;
    public int rotateRight;
    
    //---debug menu controls---
    public int toggleDebug;
    public int toggleEngineViewer;
    public int togglePos;
    public int toggleComponents;
    public int toggleBounds;
    public int toggleFPS;
    public int toggleOrbit;
    public int toggleVector;
    public int toggleMenu;
    
    
    public void loadDefault() {
        //player
        forward = Keys.W;
        right = Keys.D;
        left = Keys.A;
        back = Keys.S;
        alter = Keys.SHIFT_LEFT;
        attack = Keys.SPACE;
        defend = Keys.ALT_LEFT;
        changeVehicle = Keys.G;
        land = Keys.T;
        actionA = Keys.NUM_1;
        actionB = Keys.NUM_2;
        actionC = Keys.NUM_3;
        
        //ui
        toggleHUD = Keys.H;
        toggleEdgeMap = Keys.N;
        toggleSpaceMap = Keys.M;
        
        //screen
        fullscreen = Keys.F11;
        vsync = Keys.F8;
        zoomOut = Keys.MINUS;
        zoomIn = Keys.EQUALS;
        resetZoom = Keys.PERIOD;
        zoomCharacter = Keys.SLASH;
        zoomSpace = Keys.COMMA;
        rotateLeft = Keys.LEFT_BRACKET;
        rotateRight = Keys.RIGHT_BRACKET;
        
        //debug menu
        toggleDebug = Keys.F3;
        toggleEngineViewer = Keys.F9;
        togglePos = Keys.NUMPAD_0;
        toggleComponents = Keys.NUMPAD_1;
        toggleBounds = Keys.NUMPAD_2;
        toggleFPS = Keys.NUMPAD_3;
        toggleOrbit = Keys.NUMPAD_4;
        toggleVector = Keys.NUMPAD_5;
        toggleMenu = Keys.NUMPAD_9;
    }
    
}
