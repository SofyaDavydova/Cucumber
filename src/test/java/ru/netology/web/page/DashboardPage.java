package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final String cardNumberStart = "5559 0000 0000 ";
    private SelenideElement reloadButton = $("[data-test-id=action-reload]");


    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var text = getCardElement(cardInfo).text();
        return extractBalance(text);
    }


    private SelenideElement getCardElement(DataHelper.CardInfo cardInfo){
        return cards.findBy(Condition.attribute("data-test-id", cardInfo.getCardID()));
    }

    private SelenideElement getCardElementByCardNumber(String cardNumber){
        var value = cardNumber.substring(cardNumberStart.length(), cardNumberStart.length() + 4);
        return cards.findBy(Condition.text(value));
    }

    public int getCardBalanceByCardNumber(String cardNumber) {
        var text = getCardElementByCardNumber(cardNumber).text();
        return extractBalance(text);
    }

    private SelenideElement getCardElementByIndex(int index){
        return cards.get(index - 1);
    }

    public int getCardBalanceByIndex(int index) {
        var text = getCardElementByIndex(index).text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public void checkBalance(DataHelper.CardInfo cardInfo, int expectedBalance){
        getCardElement(cardInfo).should(visible).should(text(balanceStart + expectedBalance + balanceFinish));
    }

    public void reloadDashboardPage(){
        reloadButton.click();
        heading.shouldBe(visible);
    }


    public SupplementPage supplemention(DataHelper.CardInfo cardInfo) {
        getCardElement(cardInfo).$("[data-test-id=action-deposit]").click();
        return new SupplementPage();
    }

    public SupplementPage supplementionByIndex(int index) {
        getCardElementByIndex(index).$("[data-test-id=action-deposit]").click();
        return new SupplementPage();
    }

}
