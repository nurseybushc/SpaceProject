package com.spaceproject.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.World;
import com.spaceproject.SpaceProject;
import com.spaceproject.components.PhysicsComponent;
import com.spaceproject.components.TransformComponent;
import com.spaceproject.config.EngineConfig;
import com.spaceproject.screens.GameScreen;
import com.spaceproject.utility.IRequireGameContext;
import com.spaceproject.utility.Mappers;
import com.spaceproject.utility.PhysicsContactListener;

// based off:
// http://gafferongames.com/game-physics/fix-your-timestep/
// http://saltares.com/blog/games/fixing-your-timestep-in-libgdx-and-box2d/
public class FixedPhysicsSystem extends EntitySystem implements IRequireGameContext {
    
    private EngineConfig engineCFG = SpaceProject.configManager.getConfig(EngineConfig.class);
    private int velocityIterations = engineCFG.physicsVelocityIterations;
    private int positionIterations = engineCFG.physicsPositionIterations;
    private float timeStep = 1 / (float) engineCFG.physicsStepPerFrame;
    private float accumulator = 0f;
    
    private World world;
    
    private ImmutableArray<Entity> entities;
    
    @Override
    public void initContext(GameScreen gameScreen) {
        this.world = gameScreen.box2dWorld;
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.all(PhysicsComponent.class, TransformComponent.class).get();
        entities = engine.getEntitiesFor(family);
    
        world.setContactListener(new PhysicsContactListener(engine));
    }
    
    @Override
    public void update(float deltaTime) {
        accumulator += deltaTime;
        while (accumulator >= timeStep) {
            //System.out.println("update: " + deltaTime + ". " + accumulator);
            world.step(timeStep, velocityIterations, positionIterations);
            accumulator -= timeStep;
            
            updateTransform();
        }
        
        interpolate(deltaTime, accumulator);
    }
    
    private void updateTransform() {
        for (Entity entity : entities) {
            PhysicsComponent physics = Mappers.physics.get(entity);
            
            if (!physics.body.isActive()) {
                return;
            }
            
            TransformComponent transform = Mappers.transform.get(entity);
            transform.pos.set(physics.body.getPosition());
            transform.rotation = physics.body.getAngle();
        }
    }
    
    private void interpolate(float deltaTime, float accumulator) {
        /*
        if (physics.body.isActive()) {
            transform.position.x = physics.body.getPosition().x * alpha + old.position.x * (1.0f - alpha);
            transform.position.y = physics.body.getPosition().y * alpha + old.position.y * (1.0f - alpha);
            transform.angle = physics.body.getAngle() * MathUtils.radiansToDegrees * alpha + old.angle * (1.0f - alpha);
        }*/
    }
    
    private int getMovementLimit() {
        //movement limit = 2 * units per step
        //eg step of 60: 60 * 2 = 120,  max velocity = 120
        return 2 * engineCFG.physicsStepPerFrame;
    }
}


