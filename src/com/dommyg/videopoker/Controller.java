package com.dommyg.videopoker;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Controller {

    private Deck deck = new Deck();
    private Bank bank = new Bank();
    private Machine jacksOrBetter = new Machine(deck, bank);

    // Buttons for holding cards.
    @FXML
    private ToggleButton hold1;
    @FXML
    private ToggleButton hold2;
    @FXML
    private ToggleButton hold3;
    @FXML
    private ToggleButton hold4;
    @FXML
    private ToggleButton hold5;

    // Values that track if a card is meant to be held or not.
    private boolean card1Held;
    private boolean card2Held;
    private boolean card3Held;
    private boolean card4Held;
    private boolean card5Held;

    // Various displays and buttons that influence them.
    @FXML
    private Label denominationDisplay;
    @FXML
    private Button changeDenominationButton;
    @FXML
    private Label handStatusDisplay;
    @FXML
    private Label bankrollDisplay;
    @FXML
    private Label betDisplay;
    @FXML
    private Button changeBetButton;
    @FXML
    private Label winAmountDisplay;

    // Textual displays of the card.
    @FXML
    private Label card1;
    @FXML
    private Label card2;
    @FXML
    private Label card3;
    @FXML
    private Label card4;
    @FXML
    private Label card5;

    // Tracks if the player is starting a new game, or mid-game (after holding cards and requesting new ones).
    private boolean newHand = true;

    // The main game loop.
    // This function is activated when the player presses the "DEAL" button.
    // The newHand flag tracks if the player is starting a new game or mid-game (after holding cards and requesting new ones) and performs the appropriate cycle.
    @FXML
    private void performCycle() {
        if (newHand) {
            // If there is a win amount displayed from a previous hand, it is not cleared from a machine until a new game is initiated with the "DEAL" button.
            // It stays on screen after the game is over in case a player leaves the machine, so that potential gamblers can see someone won at the machine, and how much they won.
            // Although that is not the context this application would be played, this is meant to be a faithful reproduction of those machines.
            resetWinAmountDisplay();
            // Toggle buttons so that they are set to game mode.
            handleToggles();
            // Remove the money bet by the player on the game.
            jacksOrBetter.processWager();
            // Perform the first cycle, update the displays on the UI, and change the newHand flag to indicate game-in-progress.
            deck.firstCycle();
            setBankrollDisplay();
            setCardsDisplay();
            setHandStatusDisplay();
            newHand = false;
        } else {
            // Process holds.
            deck.hold(card1Held, card2Held, card3Held, card4Held, card5Held);
            // Perform the second cycle and update the cards and hand status displays on the UI.
            deck.secondCycle();
            setCardsDisplay();
            setHandStatusDisplay();
            // Perform the final cycle, handle payout calculations, and update the displays for bankroll and winnings on the UI.
            deck.finalCycle();
            jacksOrBetter.determinePayout();
            setWinAmountDisplay();
            setBankrollDisplay();
            // Reset the hold buttons in preparation of the next game.
            removeHolds();
            // Toggle buttons so that they are set to out-of-game mode and change the newHand flag to indicate game over.
            handleToggles();
            newHand = true;
        }
    }

    // Called when the player presses one of the hold buttons.
    // processHold checks if the hold button for a card is selected by the player and returns the value.
    // All five "HOLD" buttons are checked each time the player presses one of them.
    // Their selected/unselected values are passed on to each respective card's card1/2/3/4/5Held value.
    // These values are used during the second cycle with the Deck's hold function to determine which cards to discard.
    @FXML
    private void hold() {
        card1Held = processHold(hold1);
        card2Held = processHold(hold2);
        card3Held = processHold(hold3);
        card4Held = processHold(hold4);
        card5Held = processHold(hold5);
    }

    // Removes all holds on cards.
    // Called when the game is over.
    @FXML
    private void removeHolds() {
        hold1.setSelected(false);
        hold2.setSelected(false);
        hold3.setSelected(false);
        hold4.setSelected(false);
        hold5.setSelected(false);
        hold();
    }

    // Sets the ability for the player to press "HOLD" buttons.
    // The player can only hold cards when the game begins and the first five cards are dealt.
    // This is determined by the newHand variable.
    // When the game concludes, the player cannot hold cards and the buttons are greyed out.
    @FXML
    private void toggleHolds() {
        if (newHand) {
            hold1.setDisable(false);
            hold2.setDisable(false);
            hold3.setDisable(false);
            hold4.setDisable(false);
            hold5.setDisable(false);
        } else {
            hold1.setDisable(true);
            hold2.setDisable(true);
            hold3.setDisable(true);
            hold4.setDisable(true);
            hold5.setDisable(true);
        }
    }

    // Checks if the hold button for a card is selected by the player and returns the value.
    private static boolean processHold(ToggleButton button) {
        return button.isSelected();
    }

    // Processes changes when the player presses the "DENOM" button.
    // Changes the textual value reported to the player on the UI and passes the value to the Machine to set the betDenomination variable.
    @FXML
    private void setDenominationDisplay() {
        if (jacksOrBetter.getBetDenomination().equals(BigDecimal.valueOf(.25))) {
            processChangeDenomination(.50);
            denominationDisplay.setText("¢50");
        } else if (jacksOrBetter.getBetDenomination().equals(BigDecimal.valueOf(.50))) {
            processChangeDenomination(1.00);
            denominationDisplay.setText("$1");
        } else {
            processChangeDenomination(.25);
            denominationDisplay.setText("¢25");
        }
    }

    // Sets the betDenomination variable in the Machine.
    private void processChangeDenomination(double amount) {
        jacksOrBetter.setBetDenomination(BigDecimal.valueOf(amount));
    }

    // Sets the ability for the player to press "DENOM" button.
    // When the player is in a game, they cannot change the denomination of their bet and the button is greyed out.
    // When the game concludes, the player may change the used denomination for the next game.
    @FXML
    private void toggleChangeDenomination() {
        if (newHand) {
            changeDenominationButton.setDisable(true);
        } else {
            changeDenominationButton.setDisable(false);
        }
    }

    // Sets the textual values of the five drawn cards on the UI.
    @FXML
    private void setCardsDisplay() {
        card1.setText(jacksOrBetter.getDeck().getHandDisplay(0).toString());
        card2.setText(jacksOrBetter.getDeck().getHandDisplay(1).toString());
        card3.setText(jacksOrBetter.getDeck().getHandDisplay(2).toString());
        card4.setText(jacksOrBetter.getDeck().getHandDisplay(3).toString());
        card5.setText(jacksOrBetter.getDeck().getHandDisplay(4).toString());
    }

    // Sets the text of the hand status reported on the UI by calling the Machine's Deck's handStatusDisplay.
    @FXML
    private void setHandStatusDisplay() {
        handStatusDisplay.setText(jacksOrBetter.getDeck().getHandStatusDisplay());
    }

    // Sets the text of the dollar amount, which reports the player's Bank's bankroll value, on the UI.
    @FXML
    private void setBankrollDisplay() {
        bankrollDisplay.setText("$" +bank.getBankroll().toString());
    }

    // Processes changes when the player presses the "BET" button.
    // Changes the textual value reported to the player on the UI and passes the value to the Machine to set the bet variable.
    @FXML
    private void setBetDisplay() {
        if (jacksOrBetter.getBet() == 1) {
            processChangeBet(2);
            betDisplay.setText("BET 2");
        } else if (jacksOrBetter.getBet() == 2) {
            processChangeBet(3);
            betDisplay.setText("BET 3");
        } else if (jacksOrBetter.getBet() == 3) {
            processChangeBet(4);
            betDisplay.setText("BET 4");
        } else if (jacksOrBetter.getBet() == 4) {
            processChangeBet(5);
            betDisplay.setText("BET 5");
        } else {
            processChangeBet(1);
            betDisplay.setText("BET 1");
        }
    }

    // Sets the bet variable in the Machine.
    private void processChangeBet(int amount) {
        jacksOrBetter.setBet(amount);
    }

    // Sets the ability for the player to press "BET" button.
    // When the player is in a game, they cannot change their bet and the button is greyed out.
    // When the game concludes, the player may change their bet for the next game.
    @FXML
    private void toggleChangeBet() {
        if (newHand) {
            changeBetButton.setDisable(true);
        } else {
            changeBetButton.setDisable(false);
        }
    }

    // Sets the textual "WIN" display.
    // If the player does not win (value of zero), display nothing.
    // Else, display the win amount on the UI.
    @FXML
    private void setWinAmountDisplay() {
        if (jacksOrBetter.getWinAmount().equals(BigDecimal.valueOf(0))) {
            winAmountDisplay.setText("");
        } else {
            winAmountDisplay.setText("WIN $" +jacksOrBetter.getWinAmount().setScale(2, RoundingMode.HALF_EVEN));
        }
    }

    // Clears the textual "WIN" display (so it will disappear off the UI) by setting the win amount to zero and processing through setWinAmountDisplay.
    // Called when the player starts a new game and deals their first set of cards.
    private void resetWinAmountDisplay() {
        jacksOrBetter.setWinAmount(BigDecimal.valueOf(0));
        setWinAmountDisplay();
    }

    // Runs toggle functions.
    private void handleToggles() {
        toggleChangeDenomination();
        toggleChangeBet();
        toggleHolds();
    }

}
