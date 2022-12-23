package ma.laayouni.comptecqrses.query.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.laayouni.comptecqrses.commonapi.events.AccountCreatedEvent;
import ma.laayouni.comptecqrses.query.entities.Account;
import ma.laayouni.comptecqrses.query.repositories.AccountRepository;
import ma.laayouni.comptecqrses.query.repositories.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent event){
        log.info("******************************");
        log.info("AccountCreatedEvent received");
        log.info("******************************");
        Account account=new Account();
        account.setBalance(event.getInitialBalance());
        account.setCurrency(event.getCurrency());
        account.setId(event.getId());
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }
}
