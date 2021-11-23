package org.csc133.a4.Commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a4.GameWorld;

public class  TurnRightCommand extends Command {
    private final GameWorld gw;

    public TurnRightCommand(GameWorld gw) {
        super("Turn Right");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        gw.turnRight();
    }

}