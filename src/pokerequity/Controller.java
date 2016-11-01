package pokerequity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import javafx.event.ActionEvent;
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
    @FXML
    ArrayList<ToggleButton> cardContainers;
    @FXML
    Label tieresult;
    @FXML
    Label tielabel;

    float p1WinPercentage;
    float p2WinPercentage;
    float tiePercentage;
    String p1HoleCard = "";
    String p2HoleCard = "";
    String board = "";
    HashMap<String, String> selectedCards;
    ArrayList<ToggleButton> clickedCardButtons;
    ToggleButton activeCardContainer = null;
    String[] p1Results = new String[9];
    String[] p2Results = new String[9];

    public Controller() {

        init();
    }

    public void evaluate() {
        if (!buildHand()) {
            raiseErrorMsg();
        } else {
            Dealer d = null;
            if (p2HoleCard.length() > 0) {
                d = new Dealer(p1HoleCard, p2HoleCard, board);

            } else {
                d = new Dealer(p1HoleCard, board);
            }           
            parseResult(d);
            displayResult(d);

        }
    }
    /**
     * converts the elements of selectedCards array into Strings that can be passed by.
     * @return true in case of a successfull handbuilding
     */
    private boolean buildHand() {
        if (!isSelectionValid()) {
            return false;
        }
        p1HoleCard = selectedCards.get("p1h1") + selectedCards.get("p1h2");
        if (selectedCards.get("p2h1").length() > 0 && selectedCards.get("p2h2").length() > 0) {
            p2HoleCard = selectedCards.get("p2h1") + selectedCards.get("p2h2");
        } else {
            p2HoleCard = "";
        }

        board = selectedCards.get("f1") + selectedCards.get("f2") + selectedCards.get("f3")
                + selectedCards.get("t1") + selectedCards.get("r1");
        return true;
    }
    /**
     * 
     * @return true if both flop and player1 hand exists
     */
    private boolean isSelectionValid() {
        if (selectedCards.get("p1h1") == "" || selectedCards.get("p1h2") == "") {
            return false;
        }
        if (selectedCards.get("f1") == "" || selectedCards.get("f2") == "" || selectedCards.get("f3") == "") {
            return false;
        }
        if (selectedCards.get("p2h1") == "" ^ selectedCards.get("p2h2") == "") {
            
        }
        return true;
    }

    /**
     * creates or deletes assignments of the given card button and
     * modifies the associated cardcontainer accordingly
     * @param event - 
     */
    public void setHand(ActionEvent event) {
        Object source = event.getSource();
        ToggleButton clicked = (ToggleButton) source;
        if (clicked.isSelected()) {
            if (activeCardContainer != null) {

                activeCardContainer.setText(clicked.getText());
                activeCardContainer.setTextFill(clicked.getTextFill());
                selectedCards.put(activeCardContainer.getId(), clicked.getId().substring(1));
                clickedCardButtons.add(clicked);
            } else {
                clicked.setSelected(false);
            }

        } else {
            String containerId = getContainerId(clicked);
            ToggleButton containerCard = null;
            for (int i = 0; i < cardContainers.size(); i++) {
                if (cardContainers.get(i).getId().equalsIgnoreCase(containerId)) {
                    containerCard = cardContainers.get(i);
                }
            }
            if (containerCard != null) {
                containerCard.setText("");
            }
            selectedCards.put(containerId, "");
            clickedCardButtons.remove(clicked);
        }

    }

    /**
     * returns the clicked button's id
     * @param clicked - the container that is clicked
     * @return a String that represent the clicked button's id.
     */
    private String getContainerId(ToggleButton clicked) {
        String id = null;
        for (Entry<String, String> entry : selectedCards.entrySet()) {
            if (entry.getValue().equals(clicked.getId().substring(1))) {
                return entry.getKey();
            }
        }
        return null;
    }

     /**
     * bind or unbind the clicked button to the active card container.
     * put or deletes the text(that represents the card) from it.
     * @param event - 
     */
    public void setActiveCardContainer(ActionEvent event) {
        Object source = event.getSource();
        ToggleButton clicked = (ToggleButton) source;
        if (clicked.isSelected()) {
            this.activeCardContainer = clicked;
        } else {
            clicked.setText("");
            String selectedCardId = "x" + selectedCards.get(clicked.getId());
            selectedCards.put(clicked.getId(), "");
            for (ToggleButton card : clickedCardButtons) {
                if (card.getId().equalsIgnoreCase(selectedCardId)) {
                    card.setSelected(false);
                }
            }
            this.activeCardContainer = null;
        }
    }

    /**
     * Converts the Dealer Object's results to consumable form by the Gui
     * @param d - A Dealer object of which the displayable elements will be created.
     */
    private void parseResult(Dealer d) {
        int runTime = d.getRunTime();
        int[] resultArray = d.getPvpResult();
        this.p1WinPercentage = ((float) resultArray[0] / (runTime / 100));
        this.p2WinPercentage = ((float) resultArray[1] / (runTime / 100));
        this.tiePercentage = ((float) resultArray[2] / (runTime / 100));

        HashMap<String, Integer> p1 = d.getPlayer1ResultMap();

        p1Results[0] = Float.toString((float) p1.get("highcard") / (runTime / 100));
        p1Results[1] = Float.toString((float) p1.get("pair") / (runTime / 100));
        p1Results[2] = Float.toString((float) p1.get("twopair") / (runTime / 100));
        p1Results[3] = Float.toString((float) p1.get("threeofakind") / (runTime / 100));
        p1Results[4] = Float.toString((float) p1.get("straight") / (runTime / 100));
        p1Results[5] = Float.toString((float) p1.get("flush") / (runTime / 100));
        p1Results[6] = Float.toString((float) p1.get("fullhouse") / (runTime / 100));
        p1Results[7] = Float.toString((float) p1.get("fourofakind") / (runTime / 100));
        p1Results[8] = Float.toString((float) p1.get("straightflush") / (runTime / 100));
        if (d.getPlayer2ResultMap() != null) {
            HashMap<String, Integer> p2 = d.getPlayer2ResultMap();
            p2Results[0] = Float.toString((float) p2.get("highcard") / (runTime / 100));
            p2Results[1] = Float.toString((float) p2.get("pair") / (runTime / 100));
            p2Results[2] = Float.toString((float) p2.get("twopair") / (runTime / 100));
            p2Results[3] = Float.toString((float) p2.get("threeofakind") / (runTime / 100));
            p2Results[4] = Float.toString((float) p2.get("straight") / (runTime / 100));
            p2Results[5] = Float.toString((float) p2.get("flush") / (runTime / 100));
            p2Results[6] = Float.toString((float) p2.get("fullhouse") / (runTime / 100));
            p2Results[7] = Float.toString((float) p2.get("fourofakind") / (runTime / 100));
            p2Results[8] = Float.toString((float) p2.get("straightflush") / (runTime / 100));
        }
    }

    /**
     * binding the results to the corresponding part of the Gui
     * @param d 
     */
    private void displayResult(Dealer d) {
        if (d.getPlayer2ResultMap() != null) {

            p1result.setText(String.format("%.2f", this.p1WinPercentage));
            p2result.setText(String.format("%.2f", this.p2WinPercentage));
            tielabel.setText("tie");
            tielabel.setTextFill(Color.BROWN);
            tieresult.setText(String.format("%.2f", this.tiePercentage));
            tieresult.setTextFill(Color.BROWN);
            if (p1WinPercentage > p2WinPercentage) {
                p1result.setTextFill(Color.GREEN);
                p2result.setTextFill(Color.RED);
            } else {
                p1result.setTextFill(Color.RED);
                p2result.setTextFill(Color.GREEN);
            }
            for (int i = 0; i < this.p2ResultList.size(); i++) {
                this.p2ResultList.get(i).setText(p2Results[i]);
            }
        }

        for (int i = 0; i < this.p1ResultList.size(); i++) {

            this.p1ResultList.get(i).setText(p1Results[i]);
        }
        errorLabel.setText("");
    }

    
    private void raiseErrorMsg() {
        this.errorLabel.setText("choose more cards!");
        this.errorLabel.setTextFill(Color.RED);
    }

    private void init() {
        selectedCards = new HashMap<String, String>();
        clickedCardButtons = new ArrayList<ToggleButton>();
        selectedCards.put("p1h1", "");
        selectedCards.put("p1h2", "");
        selectedCards.put("p2h1", "");
        selectedCards.put("p2h2", "");
        selectedCards.put("f1", "");
        selectedCards.put("f2", "");
        selectedCards.put("f3", "");
        selectedCards.put("t1", "");
        selectedCards.put("r1", "");

    }

    /**
     * clears the Gui
     */
    public void clear() {
        p1result.setText("");
        p2result.setText("");
        tieresult.setText("");
        for (int i = 0; i < this.p1ResultList.size(); i++) {
            this.p1ResultList.get(i).setText("");
            this.p2ResultList.get(i).setText("");
        }
        for (int i = 0; i < cardContainers.size(); i++) {
            this.cardContainers.get(i).setText("");
            this.cardContainers.get(i).setSelected(false);
        }
        for (int i = 0; i < clickedCardButtons.size(); i++) {
            clickedCardButtons.get(i).setDisable(false);
            clickedCardButtons.get(i).setSelected(false);
        }
        selectedCards.clear();
        this.errorLabel.setText("");
        init();
    }
}
