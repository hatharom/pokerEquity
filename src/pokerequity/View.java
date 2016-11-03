/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerequity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import pokereval.Dealer;

public class View {

    Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    public void raiseErrorMsg() {
        controller.errorLabel.setText("choose more cards!");
        controller.errorLabel.setTextFill(Color.RED);
    }

    /**
     * Converts the Dealer Object's results to consumable form by the Gui
     *
     * @param d - A Dealer object of which the displayable elements will be
     * created.
     */
    public void parseResult(Dealer d) {
        int runTime = d.getRunTime();
        int[] resultArray = d.getPvpResult();
        controller.p1WinPercentage = ((float) resultArray[0] / (runTime / 100));
        controller.p2WinPercentage = ((float) resultArray[1] / (runTime / 100));
        controller.tiePercentage = ((float) resultArray[2] / (runTime / 100));

        LinkedHashMap<String, Integer> p1 = d.getPlayer1ResultMap();
        int index = 0;
        for (Map.Entry<String, Integer> entry : p1.entrySet()) {
            controller.p1Results[index] = String.format("%.2f", (float) entry.getValue() / (runTime / 100)) + "%";
            index++;
        }

        if (d.getPlayer2ResultMap() != null) {
            LinkedHashMap<String, Integer> p2 = d.getPlayer2ResultMap();
            index = 0;
            for (Map.Entry<String, Integer> entry : p2.entrySet()) {
                controller.p2Results[index] = String.format("%.2f", (float) entry.getValue() / (runTime / 100)) + "%";
                index++;
            }
        }
    }

    /**
     * binding the results to the corresponding part of the Gui
     *
     * @param d
     */
    public void displayResult(Dealer d) {
        if (d.getPlayer2ResultMap() != null) {

            controller.p1result.setText(String.format("%.2f", controller.p1WinPercentage) + "%");
            controller.p2result.setText(String.format("%.2f", controller.p2WinPercentage) + "%");
            controller.tielabel.setText("tie");
            controller.tielabel.setTextFill(Color.BROWN);
            controller.tieresult.setText(String.format("%.2f", controller.tiePercentage));
            controller.tieresult.setTextFill(Color.BROWN);
            if (controller.p1WinPercentage > controller.p2WinPercentage) {
                controller.p1result.setTextFill(Color.GREEN);
                controller.p2result.setTextFill(Color.RED);
            } else {
                controller.p1result.setTextFill(Color.RED);
                controller.p2result.setTextFill(Color.GREEN);
            }
            for (int i = 0; i < controller.p2ResultList.size(); i++) {
                controller.p2ResultList.get(i).setText(controller.p2Results[i]);
            }
        } else {
            for (int i = 0; i < controller.p2ResultList.size(); i++) {
                controller.p2ResultList.get(i).setText("");
            }
            controller.p2result.setText("");
            controller.tielabel.setText("");
            controller.tieresult.setText("");
        }

        for (int i = 0; i < controller.p1ResultList.size(); i++) {

            controller.p1ResultList.get(i).setText(controller.p1Results[i]);
        }
        controller.errorLabel.setText("");
    }

    /**
     * clears the Gui
     */
    public void clear() {
        controller.p1result.setText("");
        controller.p2result.setText("");
        controller.tieresult.setText("");

        for (int i = 0; i < controller.p1ResultList.size(); i++) {
            controller.p1ResultList.get(i).setText("");
            controller.p2ResultList.get(i).setText("");
        }
        for (int i = 0; i < controller.cardContainers.size(); i++) {
            controller.cardContainers.get(i).setText("");
            controller.cardContainers.get(i).setSelected(false);
        }

        controller.errorLabel.setText("");

    }

}
