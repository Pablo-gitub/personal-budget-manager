package main.control.investment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import main.model.market.EquityPool;
import main.model.market.EquityPoolStock;
import main.model.profile.ProfileEconomy;

public final class InvestmentViewObserverimpl implements InvestmentViewObserver {

    private final ProfileEconomy profile;

    public InvestmentViewObserverimpl(final ProfileEconomy profile) {
        super();
        this.profile = profile;
    }

    @Override
    public List<String> getAllHoldingSymbols() {
        return profile.getHoldingAccounts().stream().map(x -> x.getHoldingSymbols()).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    // for the sake of performance and less redundancy, let's get the symbols from
    // parameters.
    @Override
    public List<Double> getAllHoldingInPrices(final List<String> symbols) {
        final EquityPool ep = new EquityPoolStock();
        final List<Double> prices = new ArrayList<>();
        // I am using thread here because it may takes time
        // to query all prices from api.
        new Thread(() -> {
        }).start();
        symbols.forEach(x -> prices.add(ep.getEquityPrice(x).get()));
       
        return prices;
    }

    @Override
    public List<Double> getAllHoldingInValue(final List<Double> prices, final List<Double> shares) {
        if (prices.size() != shares.size()) {
            return Collections.emptyList();
        }
        return IntStream.iterate(0, i -> i + 1).limit(prices.size()).mapToObj(i -> i)
                .map(i -> prices.get(i) * shares.get(i)).collect(Collectors.toList());
    }

    @Override
    public List<Double> getAllHoldingShares(final List<String> symbols) {
        // maybe later i will change it to tree map, so the ticker can be
        // ordered in a way defined by a comparator.
        final Map<String, Double> map = new LinkedHashMap<>();
        symbols.forEach(x -> map.put(x, 0.0));
        profile.getHoldingAccounts()
                .forEach(x -> symbols.forEach(s -> map.computeIfPresent(s, (k, v) -> v + x.howManyShares(s))));
        return map.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList());
    }

}
