package main.model.market;

import javafx.util.Pair;

public class OrderImpl implements Order{
	
	private final Pair<Equity, Double> order;
	//private Optional<String> code;
	
	public OrderImpl(final Pair<Equity, Double> order) {
		super();
		this.order = order;
	}
	
	public OrderImpl(final Equity equity, final double shares) {
		this(new Pair<>(equity, shares));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Equity getEquity() {
		return order.getKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getShares() {
		return order.getValue();
	}

}
