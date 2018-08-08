package com.spaceproject.generation;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.spaceproject.SpaceProject;
import com.spaceproject.components.AIComponent;
import com.spaceproject.components.BarycenterComponent;
import com.spaceproject.components.BoundsComponent;
import com.spaceproject.components.CannonComponent;
import com.spaceproject.components.CharacterComponent;
import com.spaceproject.components.ControllableComponent;
import com.spaceproject.components.ExpireComponent;
import com.spaceproject.components.HealthComponent;
import com.spaceproject.components.MapComponent;
import com.spaceproject.components.MissileComponent;
import com.spaceproject.components.OrbitComponent;
import com.spaceproject.components.PlanetComponent;
import com.spaceproject.components.StarComponent;
import com.spaceproject.components.TextureComponent;
import com.spaceproject.components.TransformComponent;
import com.spaceproject.components.VehicleComponent;
import com.spaceproject.utility.IDGen;
import com.spaceproject.utility.MyMath;
import com.spaceproject.utility.SimpleTimer;


public class EntityFactory {

	//region characters
	public static Entity createCharacter(float x, float y) {
		Entity entity = new Entity();

		TransformComponent transform = new TransformComponent();
		transform.pos.set(x, y, 0);

		TextureComponent texture = new TextureComponent();
		texture.texture = TextureFactory.generateCharacter();
		texture.scale = SpaceProject.scale;

		BoundsComponent bounds = new BoundsComponent();
		float width = texture.texture.getWidth() * SpaceProject.scale;
		float height = texture.texture.getHeight() * SpaceProject.scale;
		bounds.poly = new Polygon(new float[]{0, 0, width, 0, width, height, 0, height});
		bounds.poly.setOrigin(width/2, height/2);

		CharacterComponent character = new CharacterComponent();
		character.walkSpeed = 300f;//70f;

		HealthComponent health = new HealthComponent();
		health.health = 100;
		health.maxHealth = 100;


		ControllableComponent control = new ControllableComponent();
		control.timerVehicle = new SimpleTimer(1000);
		control.timerDodge = new SimpleTimer(500);


		entity.add(health);
		entity.add(control);
		entity.add(bounds);
		entity.add(transform);
		entity.add(texture);
		entity.add(character);

		return entity;
	}

	public static Entity createCharacterAI(float x, float y) {
		Entity character = createCharacter(x, y);
		character.add(new AIComponent());
		return character;
	}
	//endregion



	//region Astronomical / Celestial objects and bodies
	public static Array<Entity> createAstronomicalObjects(float x, float y) {
		long seed = MyMath.getSeed(x, y);
		MathUtils.random.setSeed(seed);

		//return createPlanetarySystem(x,y,seed);

		switch (MathUtils.random(2)) {
			case 0:
				return createPlanetarySystem(x, y, seed);
			case 1:
				return createBinarySystem(x, y, seed);
			case 2:
				return createRoguePlanet(x, y, seed);
			default:
				return createPlanetarySystem(x,y,seed);
		}
	}

	public static Array<Entity> createPlanetarySystem(float x, float y) {
		long seed = MyMath.getSeed(x, y);
		return createPlanetarySystem(x, y, seed);
	}
	public static Array<Entity> createPlanetarySystem(float x, float y, long seed) {
		MathUtils.random.setSeed(seed);

		//number of planets in a system
		int numPlanets = MathUtils.random(SpaceProject.celestcfg.minPlanets, SpaceProject.celestcfg.maxPlanets);

		//distance between planets
		float distance = SpaceProject.celestcfg.minPlanetDist/3; //add some initial distance between star and first planet
		
		//rotation of system (orbits and spins)
		boolean rotDir = MathUtils.randomBoolean();
		
		//collection of planets/stars
		Array<Entity> entities = new Array<Entity>();
	
		//add star to center of planetary system
		Entity star = createStar(seed, x, y, rotDir);
		BarycenterComponent barycenter = new BarycenterComponent();
		barycenter.seed = seed;
		barycenter.bodyType =  numPlanets == 0 ? BarycenterComponent.AstronomicalBodyType.lonestar : BarycenterComponent.AstronomicalBodyType.uniStellar;
		star.add(barycenter);
		entities.add(star);
		
		//create planets around star
		for (int i = 0; i < numPlanets; ++i) {
			//add some distance from previous entity
			distance += MathUtils.random(SpaceProject.celestcfg.minPlanetDist, SpaceProject.celestcfg.maxPlanetDist);
			entities.add(createPlanet(MyMath.getSeed(x, y + distance), star, distance, rotDir));
		}
		
		System.out.println("Planetary System: (" + x + ", " + y + ") Objects: " + (numPlanets));
		
		return entities;
		
	}

	public static Array<Entity> createRoguePlanet(float x, float y) {
		long seed = MyMath.getSeed(x, y);
		return createRoguePlanet(x, y, seed);
	}
	public static Array<Entity> createRoguePlanet(float x, float y, long seed) {
		MathUtils.random.setSeed(seed);

		//Entity anchorEntity = new Entity();
		BarycenterComponent barycenter = new BarycenterComponent();
		barycenter.seed = seed;
		barycenter.bodyType = BarycenterComponent.AstronomicalBodyType.roguePlanet;

		Entity planet = createPlanet(seed, null, 0, MathUtils.randomBoolean());
		planet.add(barycenter);
		planet.getComponent(TransformComponent.class).pos.set(x, y, 0);


		Array<Entity> entities = new Array<Entity>();
		//entities.add(anchorEntity);
		entities.add(planet);
		System.out.println("Rougue Planet: (" + x + ", " + y + ")");
		return entities;
	}

	public static Array<Entity> createBinarySystem(float x, float y) {
		long seed = MyMath.getSeed(x, y);
		return createBinarySystem(x, y, seed);
	}
	public static Array<Entity> createBinarySystem(float x, float y, long seed) {
		Entity anchorEntity = new Entity();
		BarycenterComponent barycenter = new BarycenterComponent();
		barycenter.seed = seed;
		barycenter.bodyType = BarycenterComponent.AstronomicalBodyType.multiStellar;
		TransformComponent transform = new TransformComponent();
		transform.pos.set(x,y,0);
		anchorEntity.add(transform);
		anchorEntity.add(barycenter);

		//distance between planets
		float distance = SpaceProject.celestcfg.maxPlanetSize *2 + SpaceProject.celestcfg.maxPlanetDist * 2; //add distance between stars

		//rotation of system (orbits and spins)
		boolean rotDir = MathUtils.randomBoolean();

		//collection of planets/stars
		Array<Entity> entities = new Array<Entity>();

		//add stars
		float startAngle = MathUtils.random(MathUtils.PI2);
		float tangentialSpeed = MathUtils.random(SpaceProject.celestcfg.minPlanetTangentialSpeed, SpaceProject.celestcfg.maxPlanetTangentialSpeed);
		Entity starA = createStar(MyMath.getSeed(x+distance, y), x+distance, y, rotDir);
		OrbitComponent orbitA = starA.getComponent(OrbitComponent.class);
		orbitA.parent = anchorEntity;
		orbitA.radialDistance = distance;
		orbitA.startAngle = startAngle;
		orbitA.tangentialSpeed = tangentialSpeed;

		Entity starB = createStar(MyMath.getSeed(x-distance, y), x-distance, y, rotDir);
		OrbitComponent orbitB = starB.getComponent(OrbitComponent.class);
		orbitB.parent = anchorEntity;
		orbitB.radialDistance = distance;
		orbitB.startAngle = startAngle + MathUtils.PI;
		orbitB.tangentialSpeed = tangentialSpeed;


		entities.add(anchorEntity);
		entities.add(starA);
		entities.add(starB);


		System.out.println("Binary System: (" + x + ", " + y + ")");
		return entities;

	}

	
	private static Entity createStar(long seed, float x, float y, boolean rotationDir) {
		MathUtils.random.setSeed(seed);
		Entity entity = new Entity();

		// create star texture
		TextureComponent texture = new TextureComponent();
		int radius = MathUtils.random(SpaceProject.celestcfg.minStarSize, SpaceProject.celestcfg.maxStarSize);	
		texture.texture = TextureFactory.generateStar(seed, radius);
		texture.scale = SpaceProject.scale;
		
		// set position
		TransformComponent transform = new TransformComponent();
		transform.pos.set(x, y, 0); 
		
		//orbit for rotation of self (kinda hacky; not really orbiting, just rotating)
		OrbitComponent orbit = new OrbitComponent();
		orbit.parent = null;//set to null to negate orbit, but keep rotation
		orbit.rotateClockwise = rotationDir;
		orbit.rotSpeed = MathUtils.random(SpaceProject.celestcfg.minStarRot, 
				SpaceProject.celestcfg.maxStarRot); //rotation speed of star
		
		//drawMap
		MapComponent map = new MapComponent();
		map.color = new Color(0.9f, 0.9f, 0.15f, 0.9f);
		map.distance = 80000;

		StarComponent star = new StarComponent();
		star.seed = seed;

		//add components to entity
		entity.add(orbit);
		entity.add(transform);
		entity.add(texture);
		entity.add(map);
		entity.add(star);

		return entity;
	}
	
	private static Entity createPlanet(long seed, Entity parent, float radialDistance, boolean rotationDir) {
		MathUtils.random.setSeed(seed);
		
		Entity entity = new Entity();	
		
		//create placeholder texture. real texture will be generated by a thread
		TextureComponent texture = new TextureComponent();
		int size = (int) Math.pow(2, MathUtils.random(7, 10));
		texture.texture = TextureFactory.generatePlanet(size);
		texture.scale = 16;
		
		//orbit 
		OrbitComponent orbit = new OrbitComponent();
		orbit.parent = parent; //object to orbit around
		orbit.radialDistance = radialDistance; //distance relative to star
		orbit.tangentialSpeed = MathUtils.random(SpaceProject.celestcfg.minPlanetTangentialSpeed, SpaceProject.celestcfg.maxPlanetTangentialSpeed);
		orbit.startAngle = MathUtils.random(MathUtils.PI2); //angle relative to parent
		orbit.rotSpeed = MathUtils.random(SpaceProject.celestcfg.minPlanetRot, SpaceProject.celestcfg.maxPlanetRot);
		orbit.rotateClockwise = rotationDir;
		
		//drawMap
		MapComponent map = new MapComponent();
		map.color = new Color(0.15f, 0.5f, 0.9f, 0.9f);
		map.distance = 10000;
		
		//planet
		PlanetComponent planet = new PlanetComponent();
		planet.seed = seed;
		planet.mapSize = size;
		//TODO: randomize features/load from feature profile
		planet.scale = 100;
		planet.octaves = 4;
		planet.persistence = 0.68f;
		planet.lacunarity = 2.6f;
		
		//add components to entity
		entity.add(new TransformComponent());
		entity.add(texture);
		entity.add(orbit);
		entity.add(map);
		entity.add(planet);
		
		return entity;
	}
	//endregion

	//region ships
	public static Entity createShip3(float x, float y) {
		return createShip3(x, y, null);
	}
	
	public static Entity createShip3(float x, float y, Entity driver) {
		return createShip3(x, y, MyMath.getSeed(x, y), driver);
	}
	
	public static Entity createShip3(float x, float y, long seed, Entity driver) {
		Entity entity = new Entity();

		MathUtils.random.setSeed(seed);

		//transform
		TransformComponent transform = new TransformComponent();
		transform.pos.set(x, y, -10);
		transform.rotation = (float) Math.PI/2; //face upwards
		
		//generate random even size 
		int size;
		int minSize = 10;
		int maxSize = 36;
		
		do {
			//generate even size
			size = MathUtils.random(minSize, maxSize);
		} while (size % 2 == 1);
		TextureComponent texture = new TextureComponent();
		Texture pixmapTex = TextureFactory.generateShip(seed, size);
		texture.texture = pixmapTex;// give texture component the generated pixmapTexture
		texture.scale = SpaceProject.scale;
		
		//collision detection
		BoundsComponent bounds = new BoundsComponent(); 
		float width = texture.texture.getWidth() * SpaceProject.scale;
		float height = texture.texture.getHeight() * SpaceProject.scale;
		bounds.poly = new Polygon(new float[]{0, 0,0, height,  width, height, width, 0});
	    bounds.poly.setOrigin(width/2, height/2);
	    
		//weapon
		CannonComponent cannon = new CannonComponent();
		cannon.damage = 15;
		cannon.maxAmmo = 5;
		cannon.curAmmo = cannon.maxAmmo;
		cannon.timerFireRate = new SimpleTimer(200);//lower is faster
		cannon.size = 1; //higher is bigger
		cannon.velocity = 680; //higher is faster
		cannon.acceleration = 200;
		cannon.timerRechargeRate = new SimpleTimer(1000);//lower is faster
		
		//engine data and marks entity as drive-able
		VehicleComponent vehicle = new VehicleComponent();
		vehicle.driver = driver;
		vehicle.thrust = 320;//higher is faster
		vehicle.maxSpeed = vehicle.NOLIMIT;
		vehicle.id = IDGen.get();
		vehicle.seed = seed;
		
		//health
		HealthComponent health = new HealthComponent();
		health.health = 100;
		health.maxHealth = health.health;
		
		//drawMap
		MapComponent map = new MapComponent();
		map.color = new Color(1, 1, 1, 0.9f);
		map.distance = 3000;


		ControllableComponent control = new ControllableComponent();
		control.timerVehicle = new SimpleTimer(1000);
		control.timerDodge = new SimpleTimer(500);


		//add components to entity
		entity.add(health);
		entity.add(cannon);
		entity.add(bounds);
		entity.add(texture);
		entity.add(transform);
		entity.add(vehicle);
		entity.add(map);
		entity.add(control);
		return entity;
	}
	
	@Deprecated
	public static Entity createShip2(int x, int y) {
		MathUtils.random.setSeed((x + y) * SpaceProject.SEED);
		Entity entity = new Entity();

		TransformComponent transform = new TransformComponent();
		TextureComponent texture = new TextureComponent();

		transform.pos.set(x, y, -10);
		transform.rotation = (float) Math.PI/2; //face upwards
		
		//generate random even size 
		int size;
		int minSize = 8;
		int maxSize = 36;
		do {		
			size = MathUtils.random(minSize, maxSize);
		} while (size % 2 == 1);
		
		// generate pixmap texture
		//int size = 24;
		Pixmap pixmap = new Pixmap(size, size/2, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fillRectangle(0, 0, size, size);
		
		pixmap.setColor(0.7f,0.7f,0.7f,1);
		pixmap.drawRectangle(0, 0, size-1, size-1/2);
		
		Texture pixmapTex = new Texture(pixmap);
		pixmap.dispose(); // clean up
		texture.texture = pixmapTex;// give texture component the generated pixmapTexture
		texture.scale = SpaceProject.scale;
		
		BoundsComponent bounds = new BoundsComponent();
		float width = texture.texture.getWidth() * SpaceProject.scale;
		float height = texture.texture.getHeight() * SpaceProject.scale;
		bounds.poly = new Polygon(new float[]{0, 0, width, 0, width, height, 0, height});
	    bounds.poly.setOrigin(width/2, height/2);
		
		entity.add(bounds);
		entity.add(texture);
		entity.add(transform);
		entity.add(new VehicleComponent());
		
		return entity;
	}

	@Deprecated
	public static Entity createShip(int x, int y) {
		Entity entity = new Entity();

		TransformComponent transform = new TransformComponent();
		TextureComponent texture = new TextureComponent();

		transform.pos.set(x, y, -10);
		transform.rotation = (float) Math.PI/2; //face upwards
		
		// generate pixmap texture
		int size = 16;
		Pixmap pixmap = new Pixmap(size, size, Format.RGB565);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fillTriangle(0, 0, 0, size-1, size-1, size/2);
		
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawLine(size, size/2, size/2, size/2);
		
		
		Texture pixmapTex = new Texture(pixmap);
		pixmap.dispose(); // clean up
		texture.texture = pixmapTex;// give texture component the generated pixmapTexture
		texture.scale = SpaceProject.scale;
		
		BoundsComponent bounds = new BoundsComponent();
		float width = texture.texture.getWidth() * SpaceProject.scale;
		float height = texture.texture.getHeight() * SpaceProject.scale;
		bounds.poly = new Polygon(new float[]{0, 0, width, 0, width, height, 0, height});
	    bounds.poly.setOrigin(width/2, height/2);

		
		entity.add(bounds);
		entity.add(texture);
		entity.add(transform);
		entity.add(new VehicleComponent());
		
		return entity;
	}
	//endregion


	public static Entity createMissile(TransformComponent source, Vector2 velocity, CannonComponent cannon, Entity owner) {
		Entity entity = new Entity();

		//create texture
		TextureComponent texture = new TextureComponent();
		texture.texture = TextureFactory.generateProjectile(cannon.size);
		texture.scale = SpaceProject.scale;

		//bounding box
		BoundsComponent bounds = new BoundsComponent();
		float width = texture.texture.getWidth() * SpaceProject.scale;
		float height = texture.texture.getHeight() * SpaceProject.scale;
		bounds.poly = new Polygon(new float[]{0, 0, width, 0, width, height, 0, height});
		bounds.poly.setOrigin(width/2, height/2);

		//set position, orientation, velocity and acceleration
		TransformComponent transform = new TransformComponent();
		transform.pos.set(source.pos);
		transform.rotation = source.rotation;
		transform.velocity.add(velocity);
		transform.accel.add(velocity.cpy().setLength(cannon.acceleration));//speed up over time

		//set expire time
		ExpireComponent expire = new ExpireComponent();
		expire.time = 5;//in seconds ~approx

		//missile damage
		MissileComponent missile = new MissileComponent();
		missile.damage = cannon.damage;
		missile.owner = owner;


		entity.add(missile);
		entity.add(expire);
		entity.add(texture);
		entity.add(bounds);
		entity.add(transform);

		return entity;
	}



}
