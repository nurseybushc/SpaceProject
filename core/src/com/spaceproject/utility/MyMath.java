package com.spaceproject.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.spaceproject.screens.GameScreen;

import java.util.Random;

public abstract class MyMath {
    
    public static long getSeed(float x, float y) {
        return getSeed((int) x, (int) y);
    }
    
    public static long getSeed(int x, int y) {
        //long is 64 bits. store x in upper bits, y in lower bits
        return (x << 32) + y + GameScreen.getGalaxySeed();
    }
    
    public static long getNewGalaxySeed() {
        long newSeed = new Random().nextLong();
        
        if (GameScreen.isDebugMode) {
            newSeed = 4; //test seed
        }
        Gdx.app.log("MyMath", "galaxy seed: " + newSeed);
        
        return newSeed;
    }
    
    static final Vector2 tmpVec = new Vector2();
    
    public static Vector2 vector(float direction, float magnitude) {
        float dx = MathUtils.cos(direction) * magnitude;
        float dy = MathUtils.sin(direction) * magnitude;
        return tmpVec.set(dx, dy);
    }
    
    
    public static Vector2 logVec(Vector2 vec, float scale) {
        float length = (float) Math.log(vec.len()) * scale;
        float angle = vec.angle() * MathUtils.degreesToRadians;
        return vector(angle, length);
    }
    
    
    public static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.hypot(x2 - x1, y2 - y1);
    }
    
    
    /**
     * Get angle from position 1 to position 2.
     * TODO despite working, this seems strange..
     */
    public static float angleTo(int x1, int y1, int x2, int y2) {
        return (float) -(Math.atan2(x2 - x1, y2 - y1)) - 1.57f;
    }
   
    public static float angleTo(Vector2 a, Vector2 b) {
        return angleTo((int) a.x, (int) a.y, (int) b.x, (int) b.y);
    }
    public static float angle2(Vector2 a, Vector2 b) {
        return angle2((int) a.x, (int) a.y, (int) b.x, (int) b.y);
    }
    public static float angle2(int x1, int y1, int x2, int y2) {
        //return (float) Math.atan2(y2 - y1, x2 - x1);
        return (float) -(Math.atan2(x2 - x1, y2 - y1)) ;//- 1.57f;
    }
    /**
     * Round value with specified precision.
     *
     * @param value
     * @param precision number of decimal points
     * @return rounded value
     */
    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
    
    /**
     * Returns the percentage of the range max-min that corresponds to value.
     *
     * @param min
     * @param max
     * @param value
     * @return interpolant value within the range [min, max].
     */
    public static float inverseLerp(float min, float max, float value) {
        return (value - min) / (max - min);
    }
    
    /**
     * Convert bytes to a human readable format.
     * Eg: 26673720 -> 25.44MB
     * Credit: icza, stackoverflow.com/questions/3758606/#24805871
     *
     * @param bytes
     * @return bytes with SI postfix
     */
    public static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int z = (63 - Long.numberOfLeadingZeros(bytes)) / 10;
        return String.format("%.2f%sB", (double) bytes / (1L << (z * 10)), " KMGTPE".charAt(z));
    }
    
    
    public static BoundingBox calculateBoundingBox(Body body) {
        BoundingBox boundingBox = null;
        
        for (Fixture fixture : body.getFixtureList()) {
            if (boundingBox == null) {
                boundingBox = calculateBoundingBox(fixture);
            } else {
                boundingBox.ext(calculateBoundingBox(fixture));
            }
        }
        
        return boundingBox;
    }
    
    /**
     * Calculates a {@link BoundingBox} for the given {@link Fixture}. It will
     * be in physics/world coordinates.
     * credit: gist.github.com/nooone/8363982
     */
    public static BoundingBox calculateBoundingBox(Fixture fixture) {
        BoundingBox boundingBox = new BoundingBox();
        switch (fixture.getShape().getType()) {
            case Polygon: {
                PolygonShape shape = (PolygonShape) fixture.getShape();
                
                Vector2 tmp = new Vector2();
                shape.getVertex(0, tmp);
                tmp = fixture.getBody().getWorldPoint(tmp);
                boundingBox = new BoundingBox(new Vector3(tmp, 0), new Vector3(tmp, 0));
                for (int v = 1; v < shape.getVertexCount(); v++) {
                    shape.getVertex(v, tmp);
                    boundingBox.ext(new Vector3(fixture.getBody().getWorldPoint(tmp), 0));
                }
                
                break;
            }
            case Circle:
                // TODO implement
                //fixture.getShape().getRadius()
                break;
            case Chain:
                // TODO implement
                break;
            case Edge:
                // TODO implement
                break;
        }
        
        return boundingBox;
    }
 
    
    public static float getAngularImpulse(Body body, float targetAngle, float delta) {
        //https://www.iforce2d.net/b2dtut/rotate-to-angle
        float nextAngle = body.getAngle() + body.getAngularVelocity() * delta;
        float totalRotation = targetAngle - nextAngle;
        while (totalRotation < -180 * MathUtils.degRad) totalRotation += 360 * MathUtils.degRad;
        while (totalRotation >  180 * MathUtils.degRad) totalRotation -= 360 * MathUtils.degRad;
        float desiredAngularVelocity = totalRotation * 60;
        float change = 50 * MathUtils.degRad; //max degrees of rotation per time step
        desiredAngularVelocity = Math.min(change, Math.max(-change, desiredAngularVelocity));
        float impulse = body.getInertia() * desiredAngularVelocity;
        return impulse;
    }
    
    
    public static long fibonacci(int n) {
        if (n == 0) {
            return 0;
        }
        if (n <= 2) {
            return 1;
        }
        
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    
}
