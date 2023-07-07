package screen;

import javax.swing.*;
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
        for (int i = 0; i < 6 ; i++) {
            gui.numbers[i].setText(null);
            gui.winners[i].setText(null);
        }
        gui.got3.setText("0");
        gui.got4.setText("0");
        gui.got5.setText("0");
        gui.got6.setText("0");
        gui.drawings.setText("0");
        gui.years.setText("0");
    }

    private void stopPlaying() {
        gui.stop.setEnabled(false);
        gui.play.setEnabled(true);
        gui.quickPick.setEnabled(true);
        gui.personal.setEnabled(true);
        playing = null ;
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
    public void itemStateChanged(ItemEvent event) {
        Object item = event.getItem();
        if (item == gui.quickPick){
            for (int i = 0; i < 6; i++) {
                int pick ;
                do {
                    pick = (int) Math.floor(Math.random() * 50 + 1 );
                }while (numberGone(pick,gui.numbers,i));
                gui.numbers[i].setText("" + pick);
            }

        }else {
            for (int i = 0; i < 6; i++) {
                gui.numbers[i].setText(null);
            }


        }

    }


    private boolean numberGone(int pick, JTextField[] numbers, int i) {
        return false;
    }

    @Override
    public void run() {

    }
}
