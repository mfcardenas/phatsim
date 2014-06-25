/*
 * Copyright (C) 2014 Pablo Campillo-Sanchez <pabcampi@ucm.es>
 *
 * This software has been developed as part of the 
 * SociAAL project directed by Jorge J. Gomez Sanz
 * (http://grasia.fdi.ucm.es/sociaal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package phat.mason.agents.automaton;

import phat.mason.PHATSimState;
import phat.mason.agents.ActorCollisionListener;
import phat.mason.agents.Agent;
import phat.mason.agents.PhysicsActor;
import phat.mason.space.PhysicsObject;
import phat.mason.space.RigidPhysicsObject;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double3D;

/**
 *
 * @author pablo
 */
public class MoveTo extends SimpleState implements ActorCollisionListener {
    private Double3D destiny;
    private float distance;
    private Double3D lastLocation;
    private boolean initMoving = false;
    
    public MoveTo( Agent agent, int priority, int duration, String name, Double3D destiny) {
        this(agent, priority, duration, name, destiny, 1f);
    }
    
    public MoveTo( Agent agent, int priority, int duration, String name, Double3D destiny, float distance) {
        super(agent, priority, duration, name);
        this.destiny = destiny;
        this.distance = distance;
    }
    
    @Override
    public void nextState(SimState state) {
        if(!initMoving) {
            PHATSimState simState = (PHATSimState)state;
            agent.getPhysicsActor().moveTo(destiny, distance);
            lastLocation = agent.getPhysicsActor().getLocation();
            simState.register(this); // register ActorCollisionListener
            initMoving = true;
        } else {
            /*lastLocation = agent.getPhysicsActor().getLocation();
            if(detectObstacle() != null) {
                agent.setAutomaton(new TripOver(agent, 10, 0, "TripOver"));
            }*/
        }
    }
    
    private PhysicsObject detectObstacle() {
        Bag bag = agent.getPhysicsActor().world().getObjectsWithinDistance(lastLocation, 1f);
        System.out.println("MoveTo "+bag.numObjs);
        for(int i = 0; i < bag.numObjs; i++) {
            Object object = bag.get(i);
            if(object instanceof RigidPhysicsObject) {
                RigidPhysicsObject rpo = (RigidPhysicsObject) object;
                if(rpo.getName().equals("Obstacle")) {
                    return rpo;
                }
            }
        }
        return null;
    }
    
    private double distanceToDestiny() {
        Double3D actualLoc = agent.getPhysicsActor().getLocation();
        return actualLoc.distanceSq(destiny);
    }
    
    @Override
    public boolean isFinished(SimState state) {
        double dist = distanceToDestiny();
        System.out.println("isFinished() => "+dist+" < 0.1 == "+(dist < distance));
        return dist < distance;
    }

    @Override
    public void collision(PhysicsActor pa, PhysicsObject object) {
        if(object.getName().equals("Obstacle")) {
            pa.tripOver();
        }
    }
}