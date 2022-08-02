package main.control.investment;

import java.util.List;

/**
 * This interface models every interaction from GUI of investmentPage to Logic.
 *
 */
public interface InvestmentViewObserver {

    /**
     * Get all symbols in all holding Accounts.
     * @return list of String of symbols.
     */
    List<String> getAllHoldingSymbols();

    /**
     * get all prices of corresponding to each symbol all holding Accounts.
     * This method needs to access third party's API, so it takes time to process,
     * You may need to wait for several seconds to ultimately get the results.
     * @param symbols the ordered symbols
     * @return list of double of prices
     */
    List<Double> getAllHoldingInPrices(List<String> symbols);

    /**
     * get all value corresponding to each symbol of all holding Accounts.
     * @param prices ordered prices.
     * @param shares ordered shares.
     * @return list of all value.
     */
    List<Double> getAllHoldingInValue(List<Double> prices, List<Double> shares);

    /**
     * get all holding share numbers of all holding accounts.
     * @param symbols the ordered symbol
     * @return a list of shares in double
     */
    List<Double> getAllHoldingShares(List<String> symbols);

}
