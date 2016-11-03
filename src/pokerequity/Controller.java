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
    String[] p1Results = new String[9];
    String[] p2Results = new String[9];
    String p1HoleCard = "";
    String p2HoleCard = "";
    String board = "";
    HashMap<String, String> selectedCards;
    ArrayList<ToggleButton> clickedCardButtons;
    ToggleButton activeCardContainer = null;
    Dealer d;
    View view = new View(this);

    public Controller() {
        init();
    }

    public void evaluate() {
        if (!buildHand()) {
            view.raiseErrorMsg();
        } else {

            if (p2HoleCard.length() > 0) {
                d = new Dealer(p1HoleCard, p2HoleCard, board);

            } else {
                d = new Dealer(p1HoleCard, board);
            }
            view.parseResult(d);
            view.displayResult(d);
        }
    }

    /**
     * converts the elements of selectedCards array into Strings that can be
     * passed by.
     *
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
            return false;
        }
        return true;
    }

    /**
     * creates or deletes assignments of the given card button and modifies the
     * associated cardcontainer accordingly
     *
     * @param event -
     */
    public void setHand(ActionEvent event) {
        Object source = event.getSource();
        ToggleButton clicked = (ToggleButton) source;

        if (clicked.isSelected()) {
            if (activeCardContainer != null) {
                
                /*revert the selection of a card ,
                that will be no longer binded to a certain container*/
                ToggleButton prevCard = getRevokedCard();
                if (prevCard!=null) {
                    System.out.println(prevCard.getId());
                    prevCard.setSelected(false);
                }
                
                /*passes the new cardbutton to the current
                selection and updates container attributes*/
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
     * Search clickedCardButton for the specified bounded card
     * @return a ToggleButton that is currently binded to a container
     */
    private ToggleButton getRevokedCard() {
        String prevCardId = "x"+selectedCards.get(activeCardContainer.getId());
        ToggleButton prevCard = null;
        for (ToggleButton b : clickedCardButtons) {
            if (b.getId().equalsIgnoreCase(prevCardId)) {
                prevCard = b;
            }
        }
        return prevCard;
    }


    /**
     * returns the clicked button's id
     *
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
     * bind or unbind the clicked button to the active card container. put or
     * deletes the text(that represents the card) from it.
     *
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
     * clears the temporary storage lists and invokes View's clear on Gui
     */
    public void clear() {
        view.clear();
        for (int i = 0; i < clickedCardButtons.size(); i++) {
            clickedCardButtons.get(i).setDisable(false);
            clickedCardButtons.get(i).setSelected(false);
        }
        selectedCards.clear();
        init();
    }

}
