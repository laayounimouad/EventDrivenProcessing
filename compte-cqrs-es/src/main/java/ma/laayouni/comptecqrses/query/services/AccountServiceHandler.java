package ma.laayouni.comptecqrses.query.services;

import jdk.jfr.TransitionTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.laayouni.comptecqrses.commonapi.enums.OperationType;
import ma.laayouni.comptecqrses.commonapi.events.AccountActivatedEvent;
import ma.laayouni.comptecqrses.commonapi.events.AccountCreatedEvent;
import ma.laayouni.comptecqrses.commonapi.events.AccountCreditedEvent;
import ma.laayouni.comptecqrses.commonapi.events.AccountDebitedEvent;
import ma.laayouni.comptecqrses.commonapi.queries.GetAccountByIdQuery;
import ma.laayouni.comptecqrses.commonapi.queries.GetAllAccountsQuery;
import ma.laayouni.comptecqrses.query.entities.Account;
import ma.laayouni.comptecqrses.query.entities.Operation;
import ma.laayouni.comptecqrses.query.repositories.AccountRepository;
import ma.laayouni.comptecqrses.query.repositories.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
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

    @EventHandler
    public void on(AccountActivatedEvent event){
        log.info("******************************");
        log.info("AccountActivatedEvent received");
        log.info("******************************");
        Account account=accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event){
        log.info("******************************");
        log.info("AccountDebitedEvent received");
        log.info("******************************");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation=new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());// a ne pas faire - la date doit etre creer au moment de l'event non pas dans le handler
        operation.setType(OperationType.DEBIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event){
        log.info("******************************");
        log.info("AccountCreditedEvent received");
        log.info("******************************");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation=new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());// a ne pas faire - la date doit etre creer au moment de l'event non pas dans le handler
        operation.setType(OperationType.CREDIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query){
        return accountRepository.findAll();
    }

    @QueryHandler
    public Account on(GetAccountByIdQuery query){
        return accountRepository.findById(query.getId()).get();
    }
}
