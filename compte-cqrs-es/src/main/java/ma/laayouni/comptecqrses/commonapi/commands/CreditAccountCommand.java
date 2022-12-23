package ma.laayouni.comptecqrses.commonapi.commands;

import lombok.Getter;

public class CreditAccountCommand extends BaseCommand<String>{

    @Getter private double amount;
    @Getter private String currency;

    public CreditAccountCommand(String id, double initialBalance, String currency) {
        super(id);
        this.amount = initialBalance;
        this.currency = currency;
    }
}
