package ma.laayouni.comptecqrses.commands.aggregates;

import ma.laayouni.comptecqrses.commonapi.commands.CreateAccountCommand;
import ma.laayouni.comptecqrses.commonapi.commands.CreditAccountCommand;
import ma.laayouni.comptecqrses.commonapi.commands.DebitAccountCommand;
import ma.laayouni.comptecqrses.commonapi.enums.AccountStatus;
import ma.laayouni.comptecqrses.commonapi.events.AccountActivatedEvent;
import ma.laayouni.comptecqrses.commonapi.events.AccountCreatedEvent;
import ma.laayouni.comptecqrses.commonapi.events.AccountCreditedEvent;
import ma.laayouni.comptecqrses.commonapi.events.AccountDebitedEvent;
import ma.laayouni.comptecqrses.commonapi.exceptions.AmountNegativeException;
import ma.laayouni.comptecqrses.commonapi.exceptions.BalanceNotSufficientException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        //Required by Axon
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if (createAccountCommand.getInitialBalance() < 0) throw new RuntimeException("balance negative");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getId();
        this.currency = event.getCurrency();
        this.balance = event.getInitialBalance();
        this.status = AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand creditAccountCommand) {
        if (creditAccountCommand.getAmount() < 0) throw new AmountNegativeException("Amount should not be negative");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                creditAccountCommand.getId(),
                creditAccountCommand.getCurrency(),
                creditAccountCommand.getAmount()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance += event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand comand) {
        if (comand.getAmount() < 0) throw new AmountNegativeException("Amount should not be negative");
        if (this.balance< comand.getAmount()) throw new BalanceNotSufficientException("Balance Not sufficient ");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                comand.getId(),
                comand.getAmount(),
                comand.getCurrency()
                ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance -= event.getAmount();
    }
}
