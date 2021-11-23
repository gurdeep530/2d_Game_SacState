package org.csc133.a4.Commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a4.GameWorld;

public class  FightCommand extends Command{
    private final GameWorld gw;

    public FightCommand(GameWorld gw) {
        super("Fight");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {gw.Fight();}

}