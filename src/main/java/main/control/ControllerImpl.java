package main.control;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.google.common.base.Optional;

import javafx.concurrent.Task;
import main.control.investment.InvestmentViewObserver;
import main.control.investment.InvestmentViewObserverimpl;
import main.json.OperationJSONUtente;
import main.model.account.InvestmentAccount;
import main.model.account.InvestmentAccountTypeFactory;
import main.model.account.InvestmentAccountTypeFactoryImpl;
import main.model.account.NotEnoughFundsException;
import main.model.account.NotEnoughSharesException;
import main.model.market.Equity;
import main.model.market.EquityPool;
import main.model.market.EquityPoolBasic;
import main.model.market.HoldingAccount;
import main.model.market.HoldingAccountImpl;
import main.model.market.Market;
import main.model.market.MarketImpl;
import main.model.market.Order;
import main.model.market.OrderImpl;
import main.model.profile.PasswordChangeByEmail;
import main.model.profile.PasswordChangeByFC;
import main.model.profile.PasswordChangeByOldPassword;
import main.model.profile.PasswordChanger;
import main.model.profile.ProfileCredentials;
import main.model.profile.ProfileEconomy;
import main.model.profile.ProfileEconomyImpl;
import main.model.profile.SimplePassword;
import main.view.PageState;
import main.view.SubscriptionPlans;
import main.view.View;
import main.view.profile.AddAccountView;
import main.view.profile.PasswordChangeView;

public class ControllerImpl implements Controller {

    private ProfileEconomy profile;

    private ProfileCredentials profileCred; // these two cannot be final if we want to change profile

    private List<View> views;
    private Market market;
    private EquityPool ep;
    private InvestmentViewObserver ivo;
    private static final int refleshRate = 1000;
    private static final int NUMTHREADS = 10;
    // when we access database to retain some symbols that may takes a lot of times,
    // If an user keeps spamming for the same task, by keeping create new threads,
    // may
    // cause thread throttling, so with an excecutor, only N threads initialized
    // will be used
    private final ExecutorService executor;

    public ControllerImpl(final View... views) {
        super();

        executor = Executors.newFixedThreadPool(NUMTHREADS);

        // profile should be read from somewhere...If nobody does this work, i will do
        // it later: such as reading from the configuration file
        // based on the configuration, reads from various platform, be it locally, from
        // a database or create a new one.

        this.views = List.of(Arrays.copyOf(views, views.length));
        for (final var view : views) {
            view.setObserver(this);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void buyStocks(final String symbol, final double shares, final String accountID) {

        final Task<Object> task = new Task<>() {
            @Override
            protected Object call() throws Exception {
                final Optional<Equity> stock = ep.getEquity(symbol);
                if (stock.isPresent()) {
                    try {
                        market.buyAsset(getInvAccountById(accountID), getHolAccountById(accountID),
                                new OrderImpl(stock.get(), shares));
                        updateMarketInfo();
                    } catch (final NotEnoughFundsException e) {
                        views.forEach(x -> x.showMessage(e.toString()));
                    }
                } else {
                    views.forEach(x -> x.showMessage("This symbol doesn't exist! :("));
                }

                return null;
            }

        };
        executor.execute(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sellStocks(final String symbol, final double shares, final String accountID) {

        final Task<Object> task = new Task<>() {

            @Override
            protected Object call() throws Exception {
                try {
                    market.sellAsset(getInvAccountById(accountID), getHolAccountById(accountID),
                            new OrderImpl(ep.getEquity(symbol).get(), shares));
                    updateMarketInfo();
                } catch (final NotEnoughSharesException e) {
                    // not enough share
                    views.forEach(x -> x.showMessage(e.toString()));
                } catch (final IllegalStateException e) {
                    // no symbol specified
                    views.forEach(x -> x.showMessage("No symbol specified! :("));
                }
                return null;
            }

        };
        executor.execute(task);
    }

    private InvestmentAccount getInvAccountById(final String accountID) {
        return profile.getInvestmentAccounts().stream().filter(x -> x.getID().equals(accountID)).findFirst().get();
    }

    private HoldingAccount getHolAccountById(final String accountID) {
        return profile.getHoldingAccounts().stream().filter(x -> x.getID().equals(accountID)).findFirst().get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMarketInfo() {
        final Task<Queue<List<?>>> task = new Task<Queue<List<?>>>() {
            @Override
            public Queue<List<?>> call() {
                final Queue<List<?>> queue = new LinkedList<>();
                final List<String> symbols = ivo.getAllHoldingSymbols();
                final List<Double> shares = ivo.getAllHoldingShares(symbols);
                final List<Double> prices = ivo.getAllHoldingInPrices(symbols);
                queue.add(symbols);
                queue.add(prices);
                queue.add(shares);
                queue.add(ivo.getAllHoldingInValue(prices, shares));
                queue.add(ivo.getAllInvAccountIDs());
                return queue;
            }
        };

        task.setOnSucceeded(e -> {
            updateView(task, PageState.INVEST);
        });

        executor.execute(task);
    }

    private void updateView(final Task<Queue<List<?>>> task, final PageState pageState) {
        views.forEach(v -> v.updateView(Optional.fromNullable(task.getValue()), pageState));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminateApp() {
        executor.shutdown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showProfile() {

        final Task<Queue<List<?>>> task = new Task<Queue<List<?>>>() {
            @Override
            public Queue<List<?>> call() {
                final Queue<List<?>> q = new LinkedList<>();
                final List<String> invAccId = new LinkedList<>();
                final List<Double> invAccValues = new LinkedList<>();
                profile.getInvestmentAccounts().forEach(acc -> {
                    invAccId.add(acc.getID());
                    invAccValues.add(acc.getBalance());
                    invAccValues.add(acc.getInvestedBalance());
                });
                final List<String> holAccId = new LinkedList<>();
                final List<Double> holAccValues = new LinkedList<>();
                profile.getHoldingAccounts().forEach(acc -> {
                    holAccId.add(acc.getID());
                    holAccValues.add(acc.getTotalValue());
                });

                q.add(invAccId);
                q.add(invAccValues);
                q.add(holAccId);
                q.add(holAccValues);
                return q;
            }
        };
        task.setOnSucceeded(e -> {
            updateView(task, PageState.PROFILE);
        });
        executor.execute(task);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerProfile(final String name, final String surname, final String fc, final String eMail,
            final String password) {
        init(name, surname, fc, eMail, password);
    }

    private void init(String name, String surname, String fc, String eMail, String password) {
        this.profileCred = new ProfileCredentials(name, surname, fc, eMail, new SimplePassword(password));
        this.profile = new ProfileEconomyImpl();
        market = new MarketImpl();
        ep = new EquityPoolBasic();
        ivo = new InvestmentViewObserverimpl(profile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accessProfile(final String eMail, final String password) {
        // this is just a version to let the application run
        // this method needs to retrieve the user profile from json database
        createDemoAccount();

    }

    private void createDemoAccount() {
        init("Mario", "Rossi", "MRRSS10T99533K", "mario.rossi@studio.unibo.it", "SuperMario");

        InvestmentAccountTypeFactory f = new InvestmentAccountTypeFactoryImpl();
        InvestmentAccount invAcc = f.createForFree("Etoro");
        HoldingAccount hAcc = new HoldingAccountImpl(new EquityPoolBasic(), "Etoro");
        invAcc.deposit(10000);
        Order o = new OrderImpl(ep.getEquity("TSLA").get(), 0.7);
        market.buyAsset(invAcc, hAcc, o);
        o = new OrderImpl(ep.getEquity("AAPL").get(), 0.3);
        market.buyAsset(invAcc, hAcc, o);
        o = new OrderImpl(ep.getEquity("GME").get(), 3.0);
        market.buyAsset(invAcc, hAcc, o);
        o = new OrderImpl(ep.getEquity("JNJ").get(), 5.0);
        market.buyAsset(invAcc, hAcc, o);
        o = new OrderImpl(ep.getEquity("ETH-USD").get(), 1.0);
        market.buyAsset(invAcc, hAcc, o);
        profile.addHoldingAccount(hAcc);
        profile.addInvestmentAccount(invAcc);
        InvestmentAccount invAcc2 = f.createWithOperationFees(x -> x * 0.01, "Binance");
        o = new OrderImpl(ep.getEquity("BTC-USD").get(), 0.7);
        HoldingAccount hAcc2 = new HoldingAccountImpl(new EquityPoolBasic(), "Binance");
        invAcc2.deposit(1000000);
        market.buyAsset(invAcc2, hAcc2, o);
        o = new OrderImpl(ep.getEquity("AAL").get(), 17);
        market.buyAsset(invAcc2, hAcc2, o);
        profile.addHoldingAccount(hAcc2);
        profile.addInvestmentAccount(invAcc2);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showPasswordChangeView() {
        new PasswordChangeView(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProfileCredentials getUsrInfo() {
        return this.profileCred;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePword(final String strategy, final String newPword, final String confPword, final String id) {
        final PasswordChanger changer;
        if ("Email".equals(strategy)) {
            changer = new PasswordChanger(new PasswordChangeByEmail(this.profileCred));
            changer.changePassword(newPword, confPword, id);
        } else if ("Password Attuale".equals(strategy)) {
            changer = new PasswordChanger(new PasswordChangeByOldPassword(this.profileCred));
            changer.changePassword(newPword, confPword, id);
        } else {
            changer = new PasswordChanger(new PasswordChangeByFC(this.profileCred));
            changer.changePassword(newPword, confPword, id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProfileEconomy getUsrEconomy() {
        return this.profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showAddAccountView() {
        new AddAccountView(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createAcc(final String name, final double value, final SubscriptionPlans subPlan) {
        if (this.profile.getHoldingAccounts().stream().map(acc -> acc.getID()).collect(Collectors.toList())
                .contains(name)) {
            return false;
        } else {
            final InvestmentAccountTypeFactory f = new InvestmentAccountTypeFactoryImpl();
            final InvestmentAccount invAcc = f.createWithOperationFees(x -> x * subPlan.getFee(), name);
            final HoldingAccount hAcc = new HoldingAccountImpl(new EquityPoolBasic(), name);
            invAcc.deposit(value);
            this.profile.addHoldingAccount(hAcc);
            this.profile.addInvestmentAccount(invAcc);
            return true;
        }
    }
    @Override
    public void showExpenditure() {

        final Task<Queue<List<?>>> task = new Task<Queue<List<?>>>() {
            @Override
            public Queue<List<?>> call() {
                final Queue<List<?>> q = new LinkedList<>();
                final List<String> invAccId = new LinkedList<>();
                final List<Double> invAccValues = new LinkedList<>();
                profile.getInvestmentAccounts().forEach(acc -> {
                    invAccId.add(acc.getID());
                    invAccValues.add(acc.getBalance());
                    invAccValues.add(acc.getInvestedBalance());
                });
                final List<String> holAccId = new LinkedList<>();
                final List<Double> holAccValues = new LinkedList<>();
                profile.getHoldingAccounts().forEach(acc -> {
                    holAccId.add(acc.getID());
                    holAccValues.add(acc.getTotalValue());
                });

                q.add(invAccId);
                q.add(invAccValues);
                q.add(holAccId);
                q.add(holAccValues);
                return q;
            }
        };
        task.setOnSucceeded(e -> {
            updateView(task, PageState.EXPENSE);
        });
        executor.execute(task);

    }
}
