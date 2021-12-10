package org.csc133.a5.Commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a5.GameWorld;

public class  AccelerateCommand extends Command
{
    GameWorld gw;

    public AccelerateCommand(GameWorld gw) {
        super("Accelerate");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {gw.Accelerate();}
}
