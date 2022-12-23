package ma.laayouni.comptecqrses.commonapi.events;

import lombok.Getter;
import ma.laayouni.comptecqrses.commonapi.enums.AccountStatus;

public class AccountCreatedEvent extends BaseEvent<String>{
    @Getter private double initialeBalance;
    @Getter private String currency;
    @Getter private AccountStatus status;

    public AccountCreatedEvent(String id, double initialeBalance, String currency, AccountStatus status) {
        super(id);
        this.initialeBalance = initialeBalance;
        this.currency = currency;
        this.status = status;
    }
}
