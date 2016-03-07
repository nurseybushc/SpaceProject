package com.spaceproject;

import com.badlogic.gdx.Game;
import com.spaceproject.config.CelestialConfig;
import com.spaceproject.config.KeyConfig;
import com.spaceproject.screens.MainMenuScreen;
import com.spaceproject.screens.SpaceScreen;
import com.spaceproject.screens.TestNoiseScreen;
import com.spaceproject.screens.TestShipGenerationScreen;
import com.spaceproject.screens.WorldScreen;

public class SpaceProject extends Game {

	public static long SEED = 4; //test seed
	
	public static CelestialConfig celestcfg;
	public static KeyConfig keycfg;
	
	@Override
	public void create() {
		
		//load values for things like key mapping, settings, default values for generation
		loadConfigs();
		
		//setScreen(new TestShipGenerationScreen(this));
		//setScreen(new TestNoiseScreen(this));
		
		setScreen(new SpaceScreen(this));
		//setScreen(new WorldScreen(this));
		
		//setScreen(new MainMenuScreen(this));
	}
	
	private static void loadConfigs() {
		//KEYS
		keycfg = new KeyConfig();
		keycfg.loadDefault();
		keycfg.saveToJson();
		
		/*
		FileHandle keyFile = Gdx.files.local("controls.txt");
		if (keyFile.exists()) {
			Json json = new Json();
			json.setUsePrototypes(false);
			
			keycfg = json.fromJson(KeyConfig.class, keyFile.readString());
			System.out.println("Loaded keys from json: " + json.toJson(keycfg));
		} else {
			keycfg = new KeyConfig();
			keycfg.loadDefault();
			//keycfg.saveToJson();
			System.out.println("No key file found. Loaded defaults.");
		}*/
		
		
		//CELESTIAL OBJECTS
		celestcfg = new CelestialConfig();
		celestcfg.loadDefault();
		celestcfg.saveToJson();
	}		
	
}
