package ru.netology.web.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.SupplementPage;
import ru.netology.web.page.VerificationPage;

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

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою первую карту с главной страницы,")
    public void transferFromSecondCardToFirst(int addSum) {
        DataHelper.CardInfo cardinfo1 = DataHelper.getFirstCardInfo();
        DataHelper.CardInfo cardinfo2 = DataHelper.getSecondCardInfo();
        supplementPage = dashboardPage.supplemention(cardinfo1);
        dashboardPage = supplementPage.succesCardSupplementation(cardinfo2, String.valueOf(addSum));
        dashboardPage.reloadDashboardPage();
    }

    @Тогда("баланс его первой карты из списка на главной странице должен стать {int} рублей.")
    public void firstCardBalanceChanged(int expectedBalance1) {
        DataHelper.CardInfo cardinfo1 = DataHelper.getFirstCardInfo();
        assertAll(
                () -> dashboardPage.checkBalance(cardinfo1, expectedBalance1));
    }
}
