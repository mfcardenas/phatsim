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
package phat.agents.automaton.conditions;

import phat.agents.Agent;
import phat.world.PHATCalendar;

public class TimerFinishedCondition implements AutomatonCondition {
	long seconds;
        PHATCalendar initialTime;
	boolean init = false;
        
	public TimerFinishedCondition(int hours, int minutes, int seconds) {
		super();
		this.seconds = hours*3600 + minutes*60 + seconds;
	}

	@Override
	public boolean evaluate(Agent agent) {
            if(!init) {
                initialTime = (PHATCalendar) agent.getTime().clone();
                init = true;
            }
            long secs = initialTime.spentTimeTo(agent.getTime());
            return secs >= seconds;
	}
        
        public long getSeconds() {
            return seconds;
        }
        
        public void setSeconds(long seconds) {
            this.seconds = seconds;
        }

}
