package ma.laayouni.comptecqrses.commonapi.events;

import lombok.Getter;

public class AccountDebitedEvent extends BaseEvent<String>{
    @Getter private double amount;
    @Getter private String currency;

    public AccountDebitedEvent(String id, double initialeBalance, String currency) {
        super(id);
        this.amount = initialeBalance;
        this.currency = currency;
    }
}
