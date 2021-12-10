package org.csc133.a5.Commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a5.GameWorld;

public class  DecelerateCommand extends Command {
    private final GameWorld gw;

    public DecelerateCommand(GameWorld gw) {
        super("Decelerate");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {gw.Decelerate();}

}