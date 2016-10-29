package pokerequity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import pokereval.*;

public class Controller {

    @FXML
    Label errorLabel;
    @FXML
    Label p1result;
    @FXML
    Label p2result;
    @FXML
    ArrayList<Label> p1ResultList;
    @FXML
    ArrayList<Label> p2ResultList;
    float p1WinPercentage;
    float p2WinPercentage;

    String[] p1Results = new String[9];
    String[] p2Results = new String[9];

    public void evaluate() {
        //checkinput
        Dealer d = new Dealer("AcAs", "KcQs", "TdJs3c");
        parseResult(d);
        displayResult(d);

    }

    private void parseResult(Dealer d) {
        int runTime = d.getRunTime();
        int[] resultArray = d.getPvpResult();
        System.out.println(Arrays.toString(resultArray));
        this.p1WinPercentage = ((float) resultArray[0] / (runTime / 100));
        this.p2WinPercentage = ((float) resultArray[1] / (runTime / 100));

        HashMap<String, Integer> p1 = d.getPlayer1ResultMap();
        HashMap<String, Integer> p2 = d.getPlayer2ResultMap();

        p1Results[0] = Float.toString((float) p1.get("highcard") / (runTime / 100));
        p1Results[1] = Float.toString((float) p1.get("pair") / (runTime / 100));
        p1Results[2] = Float.toString((float) p1.get("twopair") / (runTime / 100));
        p1Results[3] = Float.toString((float) p1.get("threeofakind") / (runTime / 100));
        p1Results[4] = Float.toString((float) p1.get("straight") / (runTime / 100));
        p1Results[5] = Float.toString((float) p1.get("flush") / (runTime / 100));
        p1Results[6] = Float.toString((float) p1.get("fullhouse") / (runTime / 100));
        p1Results[7] = Float.toString((float) p1.get("fourofakind") / (runTime / 100));
        p1Results[8] = Float.toString((float) p1.get("straightflush") / (runTime / 100));

        p2Results[0] = Float.toString((float) p2.get("highcard") / (runTime / 100));
        p2Results[1] = Float.toString((float) p2.get("pair") / (runTime / 100));
        p2Results[2] = Float.toString((float) p2.get("twopair") / (runTime / 100));
        p2Results[3] = Float.toString((float) p2.get("threeofakind") / (runTime / 100));
        p2Results[4] = Float.toString((float) p2.get("straight") / (runTime / 100));
        p2Results[5] = Float.toString((float) p2.get("flush") / (runTime / 100));
        p2Results[6] = Float.toString((float) p2.get("fullhouse") / (runTime / 100));
        p2Results[7] = Float.toString((float) p2.get("fourofakind") / (runTime / 100));
        p2Results[0] = Float.toString((float) p2.get("straightflush") / (runTime / 100));

    }

    private void displayResult(Dealer d) {
        p1result.setText(Float.toString(this.p1WinPercentage).substring(0, 5));
        p2result.setText(Float.toString(this.p2WinPercentage).substring(0, 5));
        if (p1WinPercentage>p2WinPercentage) {
            p1result.setTextFill(Color.GREEN);
            p2result.setTextFill(Color.RED);
        } else{
            p1result.setTextFill(Color.RED);
            p2result.setTextFill(Color.GREEN);
        }
        
        for (int i = 0; i < this.p1ResultList.size(); i++) {
            this.p1ResultList.get(i).setText(p1Results[i]);
            this.p2ResultList.get(i).setText(p2Results[i]);
        }
    }

    public void clear() {
        p1result.setText("");
        p2result.setText("");
        for (int i = 0; i < this.p1ResultList.size(); i++) {
            this.p1ResultList.get(i).setText("");
            this.p2ResultList.get(i).setText("");
        }

    }
}
