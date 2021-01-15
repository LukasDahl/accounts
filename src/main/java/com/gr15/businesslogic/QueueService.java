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

import java.util.concurrent.CompletableFuture;

public class QueueService implements IEventReceiver, IQueueService {

    private static final String QUEUE_TYPE = "topic";
    private static final String EXCHANGE_NAME = "accountsExchange";

    private static final String TOKEN_CMD_BASE = "token.cmds.";
    private static final String ACCOUNT_CMD_BASE = "account.cmds.";
    private static final String TRANSACTION_EVENT_BASE = "transaction.events.";

    private static final String VALIDATE_TOKEN_CMD = "validateToken";
    private static final String VALIDATE_ACCOUNT_CMD = "validateAccount";
    private static final String VALIDATE_ACCOUNTS_CMD = "validateAccounts";

    private static final String TOKEN_VALIDATED_EVENT = "tokenValidated";
    private static final String ACCOUNT_VALIDATED_EVENT = "accountValidated";
    // private static final String ACCOUNTS_VALIDATED_EVENT = "accountsValidated";
    private static final String TRANSACTION_CREATED_EVENT = "transactionCreated";

    private IEventSender eventSender;
    private CompletableFuture<Account> accountResult;
    private CompletableFuture<TokenInfo> tokenInfoResult;
    private AccountManager accountManager;

    public QueueService(IEventSender eventSender) {
        accountManager = AccountManager.getInstance();
        this.eventSender = eventSender;
    }

    @Override
    public Account validateAccount(String accountId) throws QueueException {

        Event event = new Event(VALIDATE_ACCOUNT_CMD, accountId);

        accountResult = new CompletableFuture<Account>();
        try {
            eventSender.sendEvent(event, EXCHANGE_NAME, QUEUE_TYPE, ACCOUNT_CMD_BASE + VALIDATE_ACCOUNT_CMD);
        } catch (Exception e) {
            throw new QueueException("Error while validating account");
        }

        return accountResult.join();
    }

    @Override
    public void receiveEvent(Event event) throws QueueException {

        System.out.println("Handling event: " + event);

        if (event.getEventType().equals(TOKEN_VALIDATED_EVENT)) {

            var tokenInfo = new Gson().fromJson(new Gson().toJson(event.getEventInfo()), TokenInfo.class);

            tokenInfoResult.complete(tokenInfo);

        } else if (event.getEventType().equals(VALIDATE_ACCOUNTS_CMD)) {
            String accountId = new Gson().fromJson(new Gson().toJson(event.getEventInfo()), String.class);

            //var account = new Gson().fromJson(new Gson().toJson(event.getEventInfo()), Account.class);

            //accountResult.complete(accountId);

        } else {
            System.out.println("event ignored: " + event);
        }
    }

}
