package main.jsonfile;

import java.util.List;

public final class InvestmentAccountJson {

    private String nameInvestmentAccount;
    private List<AssetJson> assets;

    public void addAsset(final AssetJson asset) {
        this.assets.add(asset);
    }

    public String getNameInvestmentAccount() {
        return nameInvestmentAccount;
    }

    public void setNameInvestmentAccount(final String nameInvestmentAccount) {
        this.nameInvestmentAccount = nameInvestmentAccount;
    }

    public List<AssetJson> getAssets() {
        return assets;
    }

    public void setAssets(final List<AssetJson> assets) {
        this.assets = assets;
    }

}
