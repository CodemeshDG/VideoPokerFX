package com.dommyg.videopoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * This class allows for creating a deck of playing cards. It also has functions to determine which outcome the player arrived at during the game.
 */

class Deck {

    // These are possible hand outcomes with hierarchical values.
    static final int ROYAL_FLUSH = 9;
    static final int STRAIGHT_FLUSH = 8;
    static final int FOUR_OF_A_KIND = 7;
    static final int FULL_HOUSE = 6;
    static final int FLUSH = 5;
    static final int STRAIGHT = 4;
    static final int THREE_OF_A_KIND = 3;
    static final int TWO_PAIR = 2;
    static final int JACKS_OR_BETTER = 1;
    static final int NOTHING = 0;

    // The master deck are all 52 cards, which is used to reset the deck at the end of the game.
    static final private ArrayList<Card> MASTER_DECK = initializeMasterDeck();

    // Adding all 52 cards to the MASTER_DECK.
    private static ArrayList<Card> initializeMasterDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        //TODO: Use constants instead of string literals.
        deck.add(new Card("Hearts", "Two"));
        deck.add(new Card("Hearts", "Three"));
        deck.add(new Card("Hearts", "Four"));
        deck.add(new Card("Hearts", "Five"));
        deck.add(new Card("Hearts", "Six"));
        deck.add(new Card("Hearts", "Seven"));
        deck.add(new Card("Hearts", "Eight"));
        deck.add(new Card("Hearts", "Nine"));
        deck.add(new Card("Hearts", "Ten"));
        deck.add(new Card("Hearts", "Jack"));
        deck.add(new Card("Hearts", "Queen"));
        deck.add(new Card("Hearts", "King"));
        deck.add(new Card("Hearts", "Ace"));

        deck.add(new Card("Diamonds", "Two"));
        deck.add(new Card("Diamonds", "Three"));
        deck.add(new Card("Diamonds", "Four"));
        deck.add(new Card("Diamonds", "Five"));
        deck.add(new Card("Diamonds", "Six"));
        deck.add(new Card("Diamonds", "Seven"));
        deck.add(new Card("Diamonds", "Eight"));
        deck.add(new Card("Diamonds", "Nine"));
        deck.add(new Card("Diamonds", "Ten"));
        deck.add(new Card("Diamonds", "Jack"));
        deck.add(new Card("Diamonds", "Queen"));
        deck.add(new Card("Diamonds", "King"));
        deck.add(new Card("Diamonds", "Ace"));

        deck.add(new Card("Spades", "Two"));
        deck.add(new Card("Spades", "Three"));
        deck.add(new Card("Spades", "Four"));
        deck.add(new Card("Spades", "Five"));
        deck.add(new Card("Spades", "Six"));
        deck.add(new Card("Spades", "Seven"));
        deck.add(new Card("Spades", "Eight"));
        deck.add(new Card("Spades", "Nine"));
        deck.add(new Card("Spades", "Ten"));
        deck.add(new Card("Spades", "Jack"));
        deck.add(new Card("Spades", "Queen"));
        deck.add(new Card("Spades", "King"));
        deck.add(new Card("Spades", "Ace"));

        deck.add(new Card("Clubs", "Two"));
        deck.add(new Card("Clubs", "Three"));
        deck.add(new Card("Clubs", "Four"));
        deck.add(new Card("Clubs", "Five"));
        deck.add(new Card("Clubs", "Six"));
        deck.add(new Card("Clubs", "Seven"));
        deck.add(new Card("Clubs", "Eight"));
        deck.add(new Card("Clubs", "Nine"));
        deck.add(new Card("Clubs", "Ten"));
        deck.add(new Card("Clubs", "Jack"));
        deck.add(new Card("Clubs", "Queen"));
        deck.add(new Card("Clubs", "King"));
        deck.add(new Card("Clubs", "Ace"));
        return deck;
    }

    //TODO: You might want to use /** */ style comments for intellisense to work when looking at these fields.

    // The dynamic deck where cards are removed from and added to the player's hand.
    private ArrayList<Card> deck;
    // The player's hand in an ArrayList, which has the functionality of shifting items towards the top of the array when items are removed.
    // This is exploited in the determineHandStatus function, which needs to remove and shift cards to calculate wins and losses.
    // The contents of this ArrayList are never displayed in the UI.
    private ArrayList<Card> hand;
    // The player's hand in an array, which has the functionality of keeping items in the same position when items are removed.
    // This is exploited in functions involving displaying the player's hand in the UI.
    // On video poker machines, when a card is removed from the player's hand, the card is replaced in the same position; no cards are shifted.
    // This array allows the displaying of the player's hand to function in a way faithful to a real video poker machine.
    private Card[] handDisplay;
    // The hierarchical value of the hand's outcome, which is never displayed on the UI.
    private int handStatus;
    // The written version of the handStatus's hierarchical value, which is displayed on the UI.
    private String handStatusDisplay;

    Deck() {
        this.deck = new ArrayList<>();
        this.deck.addAll(MASTER_DECK);
        this.hand = new ArrayList<>();
        this.handDisplay = new Card[5];
        this.handStatus = 0;
        this.handStatusDisplay = "";
    }

    // The first cycle happens when the player starts a new game by pressing the "DEAL" button.
    void firstCycle() {
        deal(deck, handDisplay);
        // Creating a new ArrayList which is used to determine hand status and report it to the player in the UI.
        // The determineHandStatus includes functions which might remove some or all the cards, which is why this duplicate ArrayList is created and used instead of the hand itself.
        ArrayList<Card> handCheck = new ArrayList<>();
        handCheck.addAll(hand);
        determineHandStatus(handCheck);
    }

    // The second cycle happens when the player presses "DEAL" after the opportunity to hold cards.
    void secondCycle() {
        deal(deck, handDisplay);
        determineHandStatus(hand);
    }

    // The final cycle happens at the end of the game.
    void finalCycle() {
        resetDeck();
        resetHandDisplay();
    }

    // Deals cards from the deck into the player's hand until it has five total.
    // TODO: use more descriptive variable names for your arguments.
    private void deal(ArrayList<Card> array1, Card[] array2) {
        // Checking if there are empty positions in the array.
        // All positions would be empty at the start of the game.
        // Some positions might be empty when a player holds some cards and deals new ones.
        for (int i = 0; i < 5; i++) {
            if (array2[i] == null) {
                // If the position is empty, pick a random card from the deck.
                int randomSelection = getRandom(array1);
                // Place the selected card into the null position in the array.
                array2[i] = array1.get(randomSelection);
                // Remove the selected card from the deck.
                array1.remove(randomSelection);
            }
        }
        // TODO: Figure out what these things are...
        if (hand.size() > 0) {
            this.hand.subList(0, hand.size()).clear();
        }
        Collections.addAll(hand, handDisplay);
    }

    // Processes holds once the player presses "DEAL." If a card is not selected to be held by the player, it will be discarded, and vice versa.
    void hold(boolean card1, boolean card2, boolean card3, boolean card4, boolean card5) {
        if (!card1) {
            discard(1);
        }
        if (!card2) {
            discard(2);
        }
        if (!card3) {
            discard(3);
        }
        if (!card4) {
            discard(4);
        }
        if (!card5) {
            discard(5);
        }
    }

    // Discards a selected card from the handDisplay.
    private void discard(int choice) {
        this.handDisplay[(choice-1)] = null;
    }

    // Clears all cards in the deck and resets it by copying over the MASTER_DECK.
    // This is used when the game is over.
    private void resetDeck() {
        deck.subList(0, deck.size()).clear();
        deck.addAll(MASTER_DECK);
    }

    private void resetHandDisplay() {
        for (int i = 0; i < 5; i++) {
            this.handDisplay[i] = null;
        }
    }

    private static int getRandom(ArrayList<Card> array) {
        return new Random().nextInt(array.size());
    }

    private static int[] sortHand(ArrayList<Card> cards) {
        // Changing the Card objects into an int from the numberValue.
        // TODO: Use a constant for 5 (HAND_SIZE?)
        int[] sortedCards = new int[5];
        for (int i = 0; i < 5; i++) {
            sortedCards[i] = cards.get(i).getNumberValue();
        }
        // Sorting the cards.
        Arrays.sort(sortedCards);
        return sortedCards;
    }

    private static void printArray(ArrayList<Card> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.print(array.get(i));
            if (i < array.size()-1) {
                System.out.print(", ");
            }
        }
        System.out.println(".");
    }

    private void determineHandStatus(ArrayList<Card> cards) {
        if (checkRoyalFlush(cards)) {
            reportStatus(ROYAL_FLUSH);
        }
        // Required to handle other possibilities.
        int currentStatus = NOTHING;

        if (checkStraight(cards)) {
            currentStatus = STRAIGHT;
        }
        if (checkFlush(cards)) {
            if (currentStatus == STRAIGHT) {
                reportStatus(STRAIGHT_FLUSH);
            } else {
                currentStatus = FLUSH;
            }
        }
        // Required along with currentStatus to handle the final possibilities.
        int newStatus = checkPair(cards);

        if (newStatus > currentStatus) {
            reportStatus(newStatus);
        } else {
            reportStatus(currentStatus);
        }
    }

    int getHandStatus() {
        return handStatus;
    }

    private void reportStatus(int status) {
        switch (status) {
            case ROYAL_FLUSH:
                this.handStatus = ROYAL_FLUSH;
                this.handStatusDisplay = "ROYAL FLUSH";
                System.out.println("ROYAL FLUSH!!!");
                break;

            case STRAIGHT_FLUSH:
                this.handStatus = STRAIGHT_FLUSH;
                this.handStatusDisplay = "STRAIGHT FLUSH";
                System.out.println("Straight flush!!!");
                break;

            case FOUR_OF_A_KIND:
                this.handStatus = FOUR_OF_A_KIND;
                this.handStatusDisplay = "FOUR OF A KIND";
                System.out.println("Four of a kind!!!");
                break;

            case FULL_HOUSE:
                this.handStatus = FULL_HOUSE;
                this.handStatusDisplay = "FULL HOUSE";
                System.out.println("Full house!!!");
                break;

            case FLUSH:
                this.handStatus = FLUSH;
                this.handStatusDisplay = "FLUSH";
                System.out.println("Flush!!!");
                break;

            case STRAIGHT:
                this.handStatus = STRAIGHT;
                this.handStatusDisplay = "STRAIGHT";
                System.out.println("Straight!");
                break;

            case THREE_OF_A_KIND:
                this.handStatus = THREE_OF_A_KIND;
                this.handStatusDisplay = "THREE OF A KIND";
                System.out.println("Three of a kind!");
                break;

            case TWO_PAIR:
                this.handStatus = TWO_PAIR;
                this.handStatusDisplay = "TWO PAIRS";
                System.out.println("Two pairs!");
                break;

            case JACKS_OR_BETTER:
                this.handStatus = JACKS_OR_BETTER;
                this.handStatusDisplay = "JACKS OR BETTER";
                System.out.println("Jacks or better!");
                break;

            case NOTHING:
                this.handStatus = NOTHING;
                this.handStatusDisplay = "";
                System.out.println("Your hand is not a winner.");
                break;
        }
    }

    private static boolean checkFlush(ArrayList<Card> cards) {
        // Checking to see if cards all have the same suit.
        for (int i = 1; i < 5; i++) {
            if (!cards.get(0).getSuit().equals(cards.get(i).getSuit())) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkStraight(ArrayList<Card> cards) {
        // Transitioning the hand into int array.
        int[] sortedCards = sortHand(cards);
        // Checking to see if the final card is an ace.
        if (sortedCards[4] == 14) {
            // Checking to see if the first card is either a 2 or a 10.
            // The ace can start an Ace-2-3-4-5 straight or can be the end of a 10-Jack-Queen-King-Ace straight.
            if (sortedCards[0] == 2 || sortedCards[0] == 10) {
                // Checking for a straight.
                for (int i = 0; i < 3; i++) {
                    if (sortedCards[i+1] != sortedCards[i]+1) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            // Checking for a straight.
        } else {
            for (int i = 0; i < 4; i++) {
                if (sortedCards[i+1] != sortedCards[i]+1) {
                    return false;
                }
            }
        } return true;
    }

    private static boolean checkRoyalFlush(ArrayList<Card> cards) {
        // Checking to see if the cards all have the same suit.
        if (!checkFlush(cards)) {
            return false;
        }
        // Checking to see if the cards' values are all 10 and above.
        for (int i = 0; i < 5; i++) {
            if (cards.get(i).getNumberValue() < 10) {
                return false;
            }
        }
        // Checking to see if the cards are all different (meaning a 10, Jack, Queen, King, and Ace).
        for (int i = 0; i < 4; i++) {
            for (int x = 1 + i; x < 5; x++) {
                if (cards.get(i).getValue().equals(cards.get(x).getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int checkPair(ArrayList<Card> cards) {
        // These arrays are to store pairs.
        // There could be a maximum of two pairs with a five card hand.
        // Outcomes are TWO PAIRS, THREE OF A KIND, FOUR OF A KIND, and FULL HOUSE (a pair and three of a kind).
        ArrayList<Card> firstSet = new ArrayList<>();
        ArrayList<Card> secondSet = new ArrayList<>();

        while(!cards.isEmpty()) {
            // Making a copy of the first card in the array for reference.
            Card source = new Card(cards.get(0));
            // Checking to see if there are matching values.
            if (firstSet.size() != 0) {
                // Checking to see if the reference card matches the value of this set.
                if (source.getValue().equals(firstSet.get(0).getValue())) {
                    firstSet.add(cards.get(0));
                    cards.remove(0);
                    // Reference card matched and has been removed.
                    // Returning to start of loop to get a new reference card.
                    continue;
                }
            }
            // Checking to see if there are matching values in the next set (if the first set is populated but the reference card did not match their value).
            if (secondSet.size() != 0) {
                // Checking to see if the reference card matches the value of this set.
                if (source.getValue().equals(secondSet.get(0).getValue())) {
                    secondSet.add(cards.get(0));
                    cards.remove(0);
                    // Reference card matched and has been removed.
                    // Returning to start of loop to get a new reference card.
                    continue;
                }
            }
            // Checking to see if there are at least two cards left in the hand to examine.
            if (cards.size() >= 2) {
                // A value to indicate if a match was found during the for loop.
                boolean match = false;
                for (int i = 1; i < cards.size(); i++) {
                    if (source.getValue().equals(cards.get(i).getValue())) {
                        // Flagging a match.
                        match = true;
                        // Checking to see if there are cards in the first set of matches.
                        // If so, these matched cards should be added into the second set, which logically would be empty.
                        // Matched cards discovered here would not match the values in the first set (this would have been caught at the start of the while loop.
                        if (firstSet.size() == 0) {
                            firstSet.add(cards.get(0));
                            firstSet.add(cards.get(i));
                            cards.remove(i);
                            cards.remove(0);
                            // Reference card matched another card and both have been removed.
                            // Returning to start of while loop to get a new reference card.
                            break;
                        } else {
                            secondSet.add(cards.get(0));
                            secondSet.add(cards.get(i));
                            cards.remove(i);
                            cards.remove(0);
                            // Reference card matched another card and both have been removed.
                            // Returning to start of while loop to get a new reference card.
                            break;
                        }
                    }
                }
                // If no match was found, throw out the reference card; it matches none of the cards.
                if (!match) {
                    cards.remove(0);
                }
                // This is the final card in the hand and matches nothing.
                // Discard it; the hand is now empty and the while loop will terminate.
            } else {
                cards.remove(0);
            }
        }
        // Print out a result for the player.
        if (firstSet.isEmpty()) {
            return NOTHING;
        } else {
            if (firstSet.size() == 4) {
                return FOUR_OF_A_KIND;
            } else if (firstSet.size() == 3) {
                if (!secondSet.isEmpty()) {
                    return FULL_HOUSE;
                } else {
                    return THREE_OF_A_KIND;
                }
            } else if (secondSet.isEmpty()) {
                if (firstSet.get(0).getNumberValue() >= 11){
                    return JACKS_OR_BETTER;
                } else {
                    return NOTHING;
                }
            } else if (secondSet.size() == 2) {
                return TWO_PAIR;
            } else {
                return FULL_HOUSE;
            }
        }

    }

    Card getHandDisplay(int item) {
        return handDisplay[item];
    }

    String getHandStatusDisplay() {
        return handStatusDisplay;
    }
}
