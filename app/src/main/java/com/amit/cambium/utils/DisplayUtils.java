package com.amit.cambium.utils;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Utility class for any formatting on strings
 *
 * Created by Amit Barjatya on 10/29/17.
 */

public class DisplayUtils {

    /**
     * Provides a string representation of amount appended with the currency symbol
     * @param currency the code for the currency to use
     * @param totalAmount amount
     * @return
     */
    public static String currencyFormat(String currency,
                                        double totalAmount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(
                Locale.getDefault());
        format.setCurrency(Currency.getInstance(currency));
        return format.format(totalAmount);
    }
}
