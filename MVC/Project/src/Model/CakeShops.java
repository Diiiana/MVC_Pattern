package Model;

import java.util.*;

public class CakeShops {
    private List<CakeShop> cakeShopList = new ArrayList<CakeShop>();

    public CakeShops() {

    }

    public CakeShops(List<CakeShop> cakeShopsList) {
        this.cakeShopList = cakeShopsList;
    }

    public CakeShops(CakeShops cakeShops) {
        this.cakeShopList = cakeShops.cakeShopList;
    }

    public List<CakeShop> getCakeShopList() {
        return cakeShopList;
    }

    public void setCakeShopList(List<CakeShop> cakeShopList) {
        this.cakeShopList = cakeShopList;
    }

    public String toString() {
        String result = "";
        for (CakeShop cakeShop : cakeShopList) {
            result += cakeShop + "\n";
        }
        return result;
    }
}
