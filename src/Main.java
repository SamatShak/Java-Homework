public class Main {
    public static void main(String[] args) {
        Balance initialRub = new Balance(50_000, Currency.RUB);
        Balance initialUsd = new Balance(10_000, Currency.USD);

        BankAccount acc1 = new BankAccount("1234-5678", "Иван Иванов", initialRub, 100_000);
        BankAccount acc2 = new BankAccount("8765-4321", "Пётр Петров", initialUsd, 50_000);

        System.out.println("1.Начальное состояние:");
        System.out.println(acc1);
        System.out.println(acc2 + "\n");

        System.out.println("2.Пополняем первый счёт на 250руб:");
        acc1.deposit(25_000);
        System.out.println(acc1 + "\n");

        System.out.println("3.Снимаем 100 руб с первого счёта:");
        acc1.withdraw(10_000);
        System.out.println(acc1 + "\n");

        System.out.println("4.Переводим 50 руб со второго счёта на первый:");
        acc2.convertBalance(Currency.RUB); // сначала конвертируем для удобства
        System.out.println("Второй счёт после конвертации: " + acc2);
        acc2.transferTo(acc1, 5_000);
        System.out.println("Первый счёт после перевода: " + acc1);
        System.out.println("Второй счёт после перевода: " + acc2 + "\n");

        System.out.println("5.Проверка дневного лимита(пытаемся снять 950руб, лимит 1000,уже снято 100):");
        try {
            acc1.withdraw(95_000);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage() + "\n");
        }

        System.out.println("6.История операций первого счёта:");
        for (Transaction t : acc1.getTransactionHistory()) {
            System.out.println("  " + t);
        }
        System.out.println();

        System.out.println("7.Попытка пополнить на отрицательную сумму:");
        try {
            acc1.deposit(-100);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage() + "\n");
        }

        System.out.println("8.Попытка снять больше, чем есть:");
        try {
            acc1.withdraw(1_000_000);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage() + "\n");
        }

        System.out.println("9.Попытка перевести на свой же счёт:");
        try {
            acc1.transferTo(acc1, 1000);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage() + "\n");
        }

        System.out.println("10.Конвертация первого счёта в доллары:");
        acc1.convertBalance(Currency.USD);
        System.out.println(acc1 + "\n");

    }
}