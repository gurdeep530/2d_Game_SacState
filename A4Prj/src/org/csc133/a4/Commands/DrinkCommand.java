package org.csc133.a4.Commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a4.GameWorld;

public class  DrinkCommand extends Command {

    private final GameWorld gw;

    public DrinkCommand(GameWorld gw){
        super("Drink");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
            gw.Drink();
        }

}
