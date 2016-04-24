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
package phat.agents.events.actuators;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import phat.agents.AgentsAppState;
import phat.agents.events.PHATEvent;
import phat.devices.DevicesAppState;
import phat.devices.actuators.Actuator;
import phat.devices.actuators.ActuatorListener;
import phat.devices.actuators.VibratorActuator;
import phat.util.SpatialFactory;
import phat.util.SpatialUtils;

public class EventLauncher implements ActuatorListener {
    DevicesAppState devicesAppState;
    AgentsAppState agentsAppState;
    Map<String, VibratorEventSource> eventMapping = new HashMap<String, VibratorEventSource>();
    
    public EventLauncher(AgentsAppState agentsAppState, DevicesAppState devicesAppState) {
        this.agentsAppState = agentsAppState;
        this.devicesAppState = devicesAppState;
        
        init();
    }
    
    private void init() {
        List<Spatial> devices = SpatialUtils.getSpatialsByRole(SpatialFactory.getRootNode(), "AndroidDevice");
        
        System.out.println("\n\n");
        System.out.println("Getting devices and their actuators...");
        
        for(String id: devicesAppState.getDeviceIds()) {
            System.out.println("Device = "+id);
            Node device = devicesAppState.getDevice(id);
            VibratorActuator va = device.getControl(VibratorActuator.class);
            if(va != null) {
                System.out.println("\t"+va.getClass().getSimpleName()+":"+va.getId());
                eventMapping.put(va.getId(), new VibratorEventSource(va, device));
                va.add(this);
            }
        }
    }

    @Override
    public void stateChanged(Actuator actuator) {
        System.out.println("stateChanged = "+actuator);
        if(actuator instanceof VibratorActuator) {
            VibratorEventSource ve = (VibratorEventSource) eventMapping.get(actuator.getId());
            System.out.println("VibratorEventSource = "+ve);
            agentsAppState.add(new PHATVibratorEvent(actuator.getId()+"-"+actuator.getState(), ve));
        }
    }
}
