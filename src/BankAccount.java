import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BankAccount {
    private final String accountNumber;
    private final String ownerName;
    private Balance balance;
    private final long dailyWithdrawLimit;
    private long withdrawnToday;
    private final List<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String ownerName, Balance initialBalance, long dailyWithdrawLimit) {
        if (accountNumber == null || ownerName == null) throw new IllegalArgumentException("Номер счёта или имя пустые");
        if (accountNumber.isBlank() || ownerName.isBlank()) throw new IllegalArgumentException("Номер счёта или имя просто пробелы");
        if (dailyWithdrawLimit <= 0) throw new IllegalArgumentException("Дневной лимит меньше нуля");
        if (initialBalance == null) throw new IllegalArgumentException("Начальный баланс пустой");

        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.dailyWithdrawLimit = dailyWithdrawLimit;
        this.withdrawnToday = 0;
        this.transactionHistory = new ArrayList<>();

        addTransaction("CREATE", initialBalance.getAmountInKopecks(), initialBalance.getCurrency(), "Счёт открыт");
    }

    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public Balance getBalance() { return balance; }
    public long getDailyWithdrawLimit() { return dailyWithdrawLimit; }
    public long getWithdrawnToday() { return withdrawnToday; }

    //вернуть копию истории
    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    //пополнение счёта
    public void deposit(long amountInKopecks) {
        if (amountInKopecks <= 0) throw new IllegalArgumentException("Сумма пополнения должна быть больше нуля");
        this.balance = balance.add(amountInKopecks);
        addTransaction("DEPOSIT", amountInKopecks, balance.getCurrency(), "Пополнение");
    }

    //снятие со счёта с проверкой лимита
    public void withdraw(long amountInKopecks) {
        if (amountInKopecks <= 0) throw new IllegalArgumentException("Сумма снятия должна быть больше нуля");
        if (withdrawnToday + amountInKopecks > dailyWithdrawLimit) {
            throw new IllegalStateException("Превышен дневной лимит вывода");
        }
        this.balance = balance.subtract(amountInKopecks);
        this.withdrawnToday += amountInKopecks;
        addTransaction("WITHDRAW", amountInKopecks, balance.getCurrency(), "Снятие");
    }

    // перевод на другой счёт
    public void transferTo(BankAccount target, long amountInKopecks) {
        if (target == null) throw new IllegalArgumentException("Целевой счёт пустой");
        if (target == this) throw new IllegalArgumentException("Нельзя перевести на свой же счёт");
        if (amountInKopecks <= 0) throw new IllegalArgumentException("Сумма перевода должна быть больше нуля");
        if (withdrawnToday + amountInKopecks > dailyWithdrawLimit) {
            throw new IllegalStateException("Превышен дневной лимит вывода");
        }

        this.balance = balance.subtract(amountInKopecks);
        this.withdrawnToday += amountInKopecks;
        target.balance = target.balance.add(amountInKopecks);

        String descOut = "Перевод на счёт " + target.accountNumber;
        String descIn = "Перевод со счёта " + this.accountNumber;
        addTransaction("TRANSFER_OUT", amountInKopecks, balance.getCurrency(), descOut);
        target.addTransaction("TRANSFER_IN", amountInKopecks, target.balance.getCurrency(), descIn);
    }

    // конвертация баланса(отправляем в Balance)
    public void convertBalance(Currency targetCurrency) {
        this.balance = balance.convertTo(targetCurrency);
        addTransaction("CONVERT", balance.getAmountInKopecks(), targetCurrency, "Конвертация в " + targetCurrency.getCode());
    }

    // сброс дневного счётчика(новый ден)
    public void resetDailyWithdrawal() {
        this.withdrawnToday = 0;
    }

    // для добавления записи в историю
    private void addTransaction(String type, long amountInKopecks, Currency currency, String description) {
        transactionHistory.add(new Transaction(type, amountInKopecks, currency, description));
    }

    @Override
    public String toString() {
        return String.format("Счёт[%s, %s, баланс=%s, лимит=%d, снято сегодня=%d]",
                accountNumber, ownerName, balance, dailyWithdrawLimit, withdrawnToday);
    }
}