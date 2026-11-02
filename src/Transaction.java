import java.time.Instant;

//доп. класс для хранения истории операций
public final class Transaction {
    private final String type;
    private final long amountInKopecks;
    private final Currency currency;
    private final String description;
    private final long timestamp;

    public Transaction(String type, long amountInKopecks, Currency currency, String description) {
        if (type == null || description == null) throw new IllegalArgumentException("Тип или описание пустые");
        this.type = type;
        this.amountInKopecks = amountInKopecks;
        this.currency = currency;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %d %s - %s",
                type,
                Instant.ofEpochMilli(timestamp).toString(),
                amountInKopecks,
                currency.getCode(),
                description
        );
    }
}