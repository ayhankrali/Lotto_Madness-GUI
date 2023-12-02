package screen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

public class LottoEvent implements ItemListener, ActionListener, Runnable {

    private static final int MAX_BALL_NUMBER = 50;
    private static final int MIN_BALL_NUMBER = 1;
    private static final int DRAWING_INTERVAL = 100;

    private final LottoMadness gui;
    private volatile Thread playing;

    public LottoEvent(LottoMadness in) {
        gui = in;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("Play")) {
            startPlaying();
        }
        if (command.equals("Stop")) {
            stopPlaying();
        }
        if (command.equals("Reset")) {
            clearAllFields();
        }
    }

    private void clearAllFields() {
        JTextField[] numbers = gui.getNumbers(); // Access the numbers array using the getter
        for (JTextField number : numbers) {
            number.setText(null);
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
        playing = null;
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
        if (item == gui.quickPick) {
            for (int i = 0; i < gui.numbers.length; i++) {
                int pick;
                do {
                    pick = (int) Math.floor(Math.random() * (MAX_BALL_NUMBER - MIN_BALL_NUMBER + 1) + MIN_BALL_NUMBER);
                } while (numberGone(pick, gui.numbers, i));
                gui.numbers[i].setText(Integer.toString(pick));
            }
        } else {
            clearAllFields();
        }
    }

    private void addOneToField(JTextField field) {
        int num = Integer.parseInt(Objects.requireNonNullElse(field.getText(), "0"));
        num++;
        field.setText(Integer.toString(num));
    }

    private boolean numberGone(int num, JTextField[] pastNums, int count) {
        for (int i = 0; i < count; i++) {
            if (Integer.parseInt(pastNums[i].getText()) == num) {
                return true;
            }
        }
        return false;
    }

    private boolean matchedOne(JTextField win, JTextField[] allPicks) {
        for (JTextField textField : allPicks) {
            if (win.getText().equals(textField.getText())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        while (playing == thisThread) {
            addOneToField(gui.drawings);
            int draw = Integer.parseInt(Objects.requireNonNullElse(gui.drawings.getText(), "0"));
            float numYears = (float) draw / 104;
            gui.years.setText(Float.toString(numYears));

            int matches = 0;
            for (int i = 0; i < gui.winners.length; i++) {
                int ball;

                do {
                    ball = (int) Math.floor(Math.random() * (MAX_BALL_NUMBER - MIN_BALL_NUMBER + 1) + MIN_BALL_NUMBER);
                } while (numberGone(ball, gui.winners, i));
                gui.winners[i].setText(Integer.toString(ball));

                if (matchedOne(gui.winners[i], gui.numbers)) {
                    matches++;
                }
            }

            switch (matches) {
                case 3 -> addOneToField(gui.got3);
                case 4 -> addOneToField(gui.got4);
                case 5 -> addOneToField(gui.got5);
                case 6 -> {
                    addOneToField(gui.got6);
                    gui.stop.setEnabled(false);
                    gui.play.setEnabled(true);
                    playing = null;
                }
            }

            try {
                Thread.sleep(DRAWING_INTERVAL);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
