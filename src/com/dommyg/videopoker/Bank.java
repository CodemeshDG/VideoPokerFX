package com.dommyg.videopoker;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class holds the player's money.
 */

class Bank {

    private BigDecimal bankroll;

    Bank() {
        this.bankroll = BigDecimal.valueOf(200.00);
    }

    BigDecimal getBankroll() {
        return bankroll.setScale(2,RoundingMode.HALF_EVEN);
    }

    void setBankroll(BigDecimal bankroll) {
        this.bankroll = bankroll;
    }
}

