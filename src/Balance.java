public final class Balance {
    private final long amountInKopecks;
    private final Currency currency;

    public Balance(long amountInKopecks, Currency currency) {
        if (currency == null) throw new IllegalArgumentException("Валюта пустая");
        if (amountInKopecks < 0) throw new IllegalArgumentException("Баланс не может быть отрицательным");
        this.amountInKopecks = amountInKopecks;
        this.currency = currency;
    }

    public long getAmountInKopecks() { return amountInKopecks; }
    public Currency getCurrency() { return currency; }

    //прибавить сумму
    public Balance add(long amountInKopecks) {
        if (amountInKopecks <= 0) throw new IllegalArgumentException("Сумма меньше нуля");
        return new Balance(Math.addExact(this.amountInKopecks, amountInKopecks), this.currency);
    }

    //вычесть сумму
    public Balance subtract(long amountInKopecks) {
        if (amountInKopecks <= 0) throw new IllegalArgumentException("Сумма меньше нуля");
        if (amountInKopecks > this.amountInKopecks) throw new IllegalStateException("Недостаточно средств на счёте");
        return new Balance(Math.subtractExact(this.amountInKopecks, amountInKopecks), this.currency);
    }

    //конвертация в другую валюту(отправляем в Currency)
    public Balance convertTo(Currency targetCurrency) {
        if (targetCurrency == null) throw new IllegalArgumentException("Целевая валюта пустая");
        long converted = this.currency.convertAmount(this.amountInKopecks, targetCurrency);
        return new Balance(converted, targetCurrency);
    }

    @Override
    public String toString() {
        long rubles = amountInKopecks / 100;
        long kopecks = amountInKopecks % 100;
        return String.format("%d.%02d %s", rubles, kopecks, currency.getSymbol());
    }
}