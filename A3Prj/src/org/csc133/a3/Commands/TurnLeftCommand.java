package org.csc133.a3.Commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.GameWorld;

public class  TurnLeftCommand extends Command {
    private final GameWorld gw;

    public TurnLeftCommand(GameWorld gw) {
        super("Turn Left");
        this.gw = gw;
    }


    @Override
    public void actionPerformed(ActionEvent evt) {gw.turnLeft();}

}