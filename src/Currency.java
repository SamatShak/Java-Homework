public enum Currency {
    RUB("RUB", "₽", 100),
    USD("USD", "$", 9_600),
    EUR("EUR", "€", 10_300),
    CNY("CNY", "¥", 1_320);

    private final String code;
    private final String symbol;
    private final long priceInRublesKopecks;

    Currency(String code, String symbol, long priceInRublesKopecks) {
        this.code = code;
        this.symbol = symbol;
        this.priceInRublesKopecks = priceInRublesKopecks;
    }

    public String getCode() { return code; }
    public String getSymbol() { return symbol; }
    public long getPriceInRublesKopecks() { return priceInRublesKopecks; }

    //конвертация суммы из этой валюты в целевую
    public long convertAmount(long amountInKopecks, Currency target) {
        if (target == null) throw new IllegalArgumentException("Целевая валюта пустая");
        if (amountInKopecks < 0) throw new IllegalArgumentException("Сумма не может быть отрицательной");
        if (this == target) return amountInKopecks;

        long inBase = Math.multiplyExact(amountInKopecks, this.priceInRublesKopecks);
        return inBase / target.priceInRublesKopecks;
    }

    @Override
    public String toString() {
        return code + " (" + symbol + ")";
    }
}