package org.csc133.a3.Commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.GameWorld;

public class StartOrStopEngineCommand extends Command {

    GameWorld gw;

    public StartOrStopEngineCommand(GameWorld gw) {
        super("Start or Stop");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {gw.StartOrStopEngine();}
}
