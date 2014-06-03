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
package phat.body.commands;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import java.util.logging.Level;

import phat.body.BodiesAppState;
import phat.body.control.navigation.AutonomousControlListener;
import phat.body.control.navigation.navmesh.NavMeshMovementControl;
import phat.commands.PHATCommand;
import phat.commands.PHATCommandListener;
import phat.commands.PHATCommand.State;
import phat.structures.houses.House;
import phat.structures.houses.HouseAppState;

/**
 *
 * @author pablo
 */
public class GoToSpaceCommand extends PHATCommand implements AutonomousControlListener {

    private String bodyId;
    private String spaceId;

    public GoToSpaceCommand(String bodyId, String spaceId, PHATCommandListener listener) {
        super(listener);
        this.bodyId = bodyId;
        this.spaceId = spaceId;
        logger.log(Level.INFO, "New Command: {0}", new Object[]{this});
    }

    public GoToSpaceCommand(String bodyId, String spaceId) {
        this(bodyId, spaceId, null);
    }

    @Override
    public void runCommand(Application app) {
        BodiesAppState bodiesAppState = app.getStateManager().getState(BodiesAppState.class);
        HouseAppState houseAppState = app.getStateManager().getState(HouseAppState.class);

        Node body = bodiesAppState.getAvailableBodies().get(bodyId);

        System.out.println("BodyId == "+bodyId);
        if (body != null && body.getParent() != null) {
            House house = houseAppState.getHouse(body);
            System.out.println("HOUSE ====> "+house);
            if (house != null) {
                Vector3f loc = house.getCoordenatesOfSpaceById(spaceId);
                System.out.println("LOC => "+loc);
                if (loc != null) {
                    NavMeshMovementControl nmmc = body.getControl(NavMeshMovementControl.class);
                    if (nmmc != null) {
                        System.out.println("setMinDistance => 0.5");
                        nmmc.setMinDistance(0.5f);
                        boolean reachable = nmmc.moveTo(loc);
                        if (reachable) {
                            System.out.println("REACHABLE!!!!!");
                            nmmc.setListener(this);
                            return;
                        }
                    }
                }
            }
        }
        setState(State.Fail);
    }

    @Override
    public void interruptCommand(Application app) {
        BodiesAppState bodiesAppState = app.getStateManager().getState(
                BodiesAppState.class);

        Node body = bodiesAppState.getAvailableBodies().get(bodyId);

        if (body != null && body.getParent() != null) {
            NavMeshMovementControl nmmc = body
                    .getControl(NavMeshMovementControl.class);
            nmmc.moveTo(null);
            setState(State.Interrupted);
            return;
        }
        setState(State.Fail);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + bodyId + ", " + spaceId + ")";
    }

    @Override
    public void destinationReached(Vector3f destination) {
        setState(State.Success);
    }
}
