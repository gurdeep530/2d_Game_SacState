package org.csc133.a2.Commands;
import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a2.GameWorld;

public class  QuitCommand extends Command {
    private final GameWorld gw;

    public QuitCommand(GameWorld gw) {
        super("Quit");
        this.gw = gw;
    }


    @Override
    public void actionPerformed(ActionEvent evt) {
        gw.quit();
    }
}

