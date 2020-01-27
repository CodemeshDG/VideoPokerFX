package com.dommyg.videopoker;

import java.math.BigDecimal;

/**
 * This class holds a deck and bank. It contains payout amounts for player wins and determines wagers and payouts based upon the player's bet and denomination selected.
 */

class Machine {

    // These are possible hand outcomes with prize values.
    static final private int ROYAL_FLUSH_PRIZE = 800;
    static final private int STRAIGHT_FLUSH_PRIZE = 50;
    static final private int FOUR_OF_A_KIND_PRIZE = 25;
    static final private int FULL_HOUSE_PRIZE = 9;
    static final private int FLUSH_PRIZE = 6;
    static final private int STRAIGHT_PRIZE = 4;
    static final private int THREE_OF_A_KIND_PRIZE = 3;
    static final private int TWO_PAIR_PRIZE = 2;
    static final private int JACKS_OR_BETTER_PRIZE = 1;
    static final private int NOTHING_PRIZE = 0;

    // The machine needs a deck to work with, a bank to hold player's money, and holds the bet amount, denomination, and win amounts of play.
    private Deck deck;
    private Bank bank;
    private BigDecimal betDenomination;
    private int bet;
    private BigDecimal winAmount;

    Machine(Deck deck, Bank bank) {
        this.deck = deck;
        this.bank = bank;
        this.betDenomination = BigDecimal.valueOf(.25);
        this.bet = 1;
        this.winAmount = BigDecimal.valueOf(0);
    }

    void setBetDenomination(BigDecimal betDenomination) {
        this.betDenomination = betDenomination;
    }

    BigDecimal getBetDenomination() {
        return betDenomination;
    }

    void setBet(int bet) {
        this.bet = bet;
    }

    int getBet() {
        return bet;
    }

    Deck getDeck() {
        return deck;
    }

    BigDecimal getWinAmount() {
        return winAmount;
    }

    void setWinAmount(BigDecimal winAmount) {
        this.winAmount = winAmount;
    }

    // Determines which amount to payout to the player based upon their handStatus.
    void determinePayout() {
        switch (deck.getHandStatus()) {
            case Deck.ROYAL_FLUSH:
                processPayout(ROYAL_FLUSH_PRIZE);
                break;

            case Deck.STRAIGHT_FLUSH:
                processPayout(STRAIGHT_FLUSH_PRIZE);
                break;

            case Deck.FOUR_OF_A_KIND:
                processPayout(FOUR_OF_A_KIND_PRIZE);
                break;

            case Deck.FULL_HOUSE:
                processPayout(FULL_HOUSE_PRIZE);
                break;

            case Deck.FLUSH:
                processPayout(FLUSH_PRIZE);
                break;

            case Deck.STRAIGHT:
                processPayout(STRAIGHT_PRIZE);
                break;

            case Deck.THREE_OF_A_KIND:
                processPayout(THREE_OF_A_KIND_PRIZE);
                break;

            case Deck.TWO_PAIR:
                processPayout(TWO_PAIR_PRIZE);
                break;

            case Deck.JACKS_OR_BETTER:
                processPayout(JACKS_OR_BETTER_PRIZE);
                break;

            case Deck.NOTHING:
                processPayout(NOTHING_PRIZE);
                break;
        }
    }

    // Adds any winnings to the bankroll at the end of a game.
    private void processPayout(int prize) {
        bank.setBankroll(bank.getBankroll().add(calculatePayout(prize)));
        this.winAmount = calculatePayout(prize);
    }

    // Removes the wager from the bankroll when the player starts a new game.
    void processWager() {
        bank.setBankroll(bank.getBankroll().subtract(calculateWager()));
    }

    // Calculates the payout based upon the denomination selected by the player.
    private BigDecimal calculatePayout(int prize) {
        return betDenomination.multiply(BigDecimal.valueOf(bet * prize));
    }

    // Calculates the wager based upon the denomination selected by the player.
    private BigDecimal calculateWager() {
        return betDenomination.multiply(BigDecimal.valueOf(bet));
    }
}
