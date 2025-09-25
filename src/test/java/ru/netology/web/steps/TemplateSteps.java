package ru.netology.web.steps;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.SupplementPage;
import ru.netology.web.page.VerificationPage;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TemplateSteps {
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;
    private static SupplementPage supplementPage;

    @Пусть("пользователь залогинен с именем {string} и паролем {string},")
    public void succesLogin(String login, String password) {
        DataHelper.AuthInfo authInfo = new DataHelper.AuthInfo(login, password);
        loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        verificationPage = loginPage.validLogin(login, password);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы,")
    public void transferFromSecondCardToFirst(int addSum, String cardNumber, int cardIndex) {
        supplementPage = dashboardPage.supplementionByIndex(cardIndex);
        dashboardPage = supplementPage.succesCardSupplementationByCardNumber(cardNumber, addSum);
        dashboardPage.reloadDashboardPage();
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен стать {int} рублей.")
    public void firstCardBalanceChanged(int cardIndex, int expectedBalance) {
        int actualBalance = dashboardPage.getCardBalanceByIndex(cardIndex);
        Assertions.assertEquals(expectedBalance, actualBalance);
    }
}
