/**
 * @author Jonatan
 */

package com.gr15.businesslogic;

import com.google.gson.Gson;
import com.gr15.businesslogic.exceptions.QueueException;
import com.gr15.businesslogic.model.Account;
import com.gr15.businesslogic.model.TokenInfo;
import com.gr15.messaging.interfaces.IEventReceiver;
import com.gr15.messaging.interfaces.IEventSender;
import com.gr15.messaging.models.Event;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class QueueService implements IEventReceiver, IQueueService {

    private static final String QUEUE_TYPE = "topic";
    private static final String EXCHANGE_NAME = "accountsExchange";

    private static final String TOKEN_CMD_BASE = "token.cmds.";
    private static final String ACCOUNT_CMD_BASE = "account.cmds.";
    private static final String TRANSACTION_EVENT_BASE = "transaction.events.";
    private static final String ACCOUNT_EVENT_BASE = "account.events";

    private static final String VALIDATE_TOKEN_CMD = "validateToken";
    private static final String VALIDATE_ACCOUNT_CMD = "validateAccount";
    private static final String VALIDATE_ACCOUNTS_CMD = "validateAccounts";

    private static final String ACCOUNT_EXISTS_CMD = "accountExistsRequest";
    private static final String ACCOUNT_EXISTS_EVENT = "accountExistsResponse";

    private static final String TOKEN_VALIDATED_EVENT = "tokenValidated";
    private static final String ACCOUNT_VALIDATED_EVENT = "accountValidated";
    // private static final String ACCOUNTS_VALIDATED_EVENT = "accountsValidated";
    private static final String TRANSACTION_CREATED_EVENT = "transactionCreated";


    private static final String ACCOUNT_DELETE_EVENT = "accountDeleted";

    private IEventSender eventSender;
    private CompletableFuture<Account> accountResult;
    private CompletableFuture<TokenInfo> tokenInfoResult;
    private AccountManager accountManager;

    public QueueService(IEventSender eventSender) {
        this.eventSender = eventSender;
    }

    @Override
    public void receiveEvent(Event event) throws QueueException {

        System.out.println("Handling event: " + event);
        accountManager = AccountManager.getInstance();


        if (event.getEventType().equals(TOKEN_VALIDATED_EVENT)) {

            var tokenInfo = new Gson().fromJson(new Gson().toJson(event.getEventInfo()), TokenInfo.class);

            tokenInfoResult.complete(tokenInfo);

        } else if (event.getEventType().equals(VALIDATE_ACCOUNTS_CMD)) {
            String accountId = new Gson().fromJson(new Gson().toJson(event.getEventInfo()), String.class);

            validateAccount(accountId);

        } else if (event.getEventInfo().equals(ACCOUNT_EXISTS_CMD)){
            String accountId = new Gson().fromJson(new Gson().toJson(event.getEventInfo()), String.class);

            accountExists(accountId);

        } else {
            System.out.println("event ignored: " + event);
        }
    }

    private void validateAccount(String accountId) throws QueueException {
        Account account = accountManager.validateAccount(accountId);
        if (account == null)
            account = new Account(UUID.fromString(accountId));

        Event event = new Event(ACCOUNT_VALIDATED_EVENT, account);

        try{
            eventSender.sendEvent(event, EXCHANGE_NAME, QUEUE_TYPE, ACCOUNT_EVENT_BASE + ACCOUNT_VALIDATED_EVENT);
        } catch(Exception e){
            throw new QueueException("Error while publishing account validated event.");
        }
    }

    private void accountExists(String accountId) throws QueueException {
        String responseString = accountId + ",";

        if (accountManager.validateAccount(accountId) != null)
            responseString += "1";
        else
            responseString += "0";

        Event event = new Event(ACCOUNT_EXISTS_EVENT, responseString);

        try{
            eventSender.sendEvent(event, EXCHANGE_NAME, QUEUE_TYPE, ACCOUNT_EVENT_BASE + ACCOUNT_EXISTS_EVENT);
        } catch (Exception e){
            throw new QueueException("Error while poblishin account exists event.");
        }
    }


    @Override
    public void publishDeleteAccountEvent(String accountId) throws QueueException {
        Event event = new Event(ACCOUNT_DELETE_EVENT, accountId);
        try {
            eventSender.sendEvent(event, EXCHANGE_NAME, QUEUE_TYPE, ACCOUNT_EVENT_BASE + ACCOUNT_DELETE_EVENT);
        } catch (Exception e) {
            throw new QueueException("Error while publishing account delete event");
        }
    }
}
