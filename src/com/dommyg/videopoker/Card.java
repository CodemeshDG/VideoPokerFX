package com.dommyg.videopoker;

/**
 * This class holds information to create playing cards.
 */
public class Card {
    //TODO: Since suit and value belong to a limited set of values, use an Enumeration.
    private String suit;
    private String value;
    private transient int numberValue;

    /**
     * Main constructor that contains two Strings (suits and values) and a translated int based off the value String.
     */
    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
        this.numberValue = calculateValue();
    }

    // Alternate constructor used during the Deck's checkPair function, which does not need a numberValue to perform its duties.
    public Card(Card card) {
        this.suit = card.suit;
        this.value = card.value;
    }

    String getSuit() {
        return suit;
    }

    String getValue() {
        return value;
    }

    int getNumberValue() {
        return numberValue;
    }

    // Translates the Card's value, which is a String, into an int for use in various places during gameplay.
    private int calculateValue() {
        switch (this.value) {
            //TODO Use constants instead of String literals.
            case "Two":
                return 2;

            case "Three":
                return 3;

            case "Four":
                return 4;

            case "Five":
                return 5;

            case "Six":
                return 6;

            case "Seven":
                return 7;

            case "Eight":
                return 8;

            case "Nine":
                return 9;

            case "Ten":
                return 10;

            case "Jack":
                return 11;

            case "Queen":
                return 12;

            case "King":
                return 13;

            case "Ace":
                return 14;

                default:
                    System.out.println("ERROR: Invalid value.");
                    return 0;
        }
    }

    @Override
    public String toString() {
        return value +" of " +suit;
    }

}
