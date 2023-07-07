package screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LottoEvent implements ItemListener, ActionListener,Runnable {

    LottoMadness gui;
    Thread playing ;


    public LottoEvent(LottoMadness in){
        gui = in;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("Play")){
            startPlaying();
        }
        if (command.equals("Stop")){
            stopPlaying();
        }
        if (command.equals("Reset")){
            clearAllFiles();
        }
    }

    private void clearAllFiles() {
    }

    private void stopPlaying() {
    }


    private void startPlaying() {
        playing = new Thread(this);
        playing.start();
        gui.play.setEnabled(false);
        gui.stop.setEnabled(true);
        gui.reset.setEnabled(false);
        gui.quickPick.setEnabled(false);
        gui.personal.setEnabled(false);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

    @Override
    public void run() {

    }
}
