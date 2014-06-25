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
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.logging.Level;
import phat.body.BodiesAppState;
import phat.body.control.navigation.navmesh.NavMeshMovementControl;
import phat.body.control.parkinson.HeadTremblingControl;
import phat.body.control.physics.PHATCharacterControl;
import phat.commands.PHATCommand;
import phat.commands.PHATCommandListener;
import phat.structures.houses.HouseAppState;

/**
 *
 * @author pablo
 */
public class TremblingHeadCommand extends PHATCommand {

    private String bodyId;
    private Boolean on;
    private Float minAngle;
    private Float maxAngle;
    private Float angular;

    public TremblingHeadCommand(String bodyId, Boolean on, PHATCommandListener listener) {
        super(listener);
        this.bodyId = bodyId;
        this.on = on;
        logger.log(Level.INFO, "New Command: {0}", new Object[]{this});
    }

    public TremblingHeadCommand(String bodyId, Boolean on) {
        this(bodyId, on, null);
    }

    @Override
    public void runCommand(Application app) {
        BodiesAppState bodiesAppState = app.getStateManager().getState(BodiesAppState.class);

        Node body = bodiesAppState.getAvailableBodies().get(bodyId);
        if (body != null) {
            if (on) {
                active(body);
            } else {
                desactive(body);
            }
        }
        setState(State.Success);
    }

    private void active(Node body) {
        HeadTremblingControl htc = body.getControl(HeadTremblingControl.class);
        if (htc == null) {
            htc = new HeadTremblingControl();
            body.addControl(htc);
        }
        if (minAngle != null) {
            htc.setMinAngle(minAngle);
        }
        if (maxAngle != null) {
            htc.setMaxAngle(maxAngle);
        }
        if (angular != null) {
            htc.setAngular(angular);
        }
    }

    private void desactive(Node body) {
        HeadTremblingControl htc = body.getControl(HeadTremblingControl.class);
        if (htc != null) {
            body.removeControl(htc);
        }
    }

    public Float getMinAngle() {
        return minAngle;
    }

    public void setMinAngle(Float minAngle) {
        this.minAngle = minAngle;
    }

    public Float getMaxAngle() {
        return maxAngle;
    }

    public void setMaxAngle(Float maxAngle) {
        this.maxAngle = maxAngle;
    }

    public Float getAngular() {
        return angular;
    }

    public void setAngular(Float angular) {
        this.angular = angular;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + bodyId + ", " + on + ")";
    }

    @Override
    public void interruptCommand(Application app) {
        setState(State.Fail);
    }
}