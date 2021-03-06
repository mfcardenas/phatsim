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
package phat.agents.automaton;

import phat.PHATInterface;
import phat.agents.Agent;
import phat.body.commands.PlayBodyAnimationCommand;
import phat.commands.PHATCommand;
import phat.commands.PHATCommandListener;

/**
 *
 * @author pablo
 */
public class PlayAnimation extends SimpleState implements PHATCommandListener {

    PlayBodyAnimationCommand playBodyAnimationCommand;
    boolean animFinished = false;
    String animationName;

    public PlayAnimation(Agent agent, String name, String animationName) {
        this(agent, 0, name, animationName);
    }
    
    public PlayAnimation(Agent agent, int priority, String name, String animationName) {
        super(agent, priority, name);
        this.animationName = animationName;
    }

    @Override
    public boolean isFinished(PHATInterface phatInterface) {
        return ((super.getFinishCondition() != null && super.isFinished(phatInterface)) || super.getFinishCondition() == null) && animFinished;
    }

    @Override
    public void commandStateChanged(PHATCommand command) {
        if (command == playBodyAnimationCommand
                && command.getState().equals(PHATCommand.State.Success)) {
            animFinished = true;
        }
    }

    @Override
    public void simpleNextState(PHATInterface phatInterface) {
        if(!super.isFinished(phatInterface) && animFinished) {
            playAnim();
        }
    }

    @Override
    public void initState(PHATInterface phatInterface) {
        playAnim();
    }
    
    private void playAnim() {
        playBodyAnimationCommand = new PlayBodyAnimationCommand(agent.getId(), animationName, this);
        agent.runCommand(playBodyAnimationCommand);
        animFinished = false;
    }
}
