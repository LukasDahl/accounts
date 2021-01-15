/**
 * @author Wassim
 */

package com.gr15.businesslogic;


import com.gr15.businesslogic.exceptions.QueueException;
import com.gr15.businesslogic.model.Account;

public interface IQueueService {
    Account validateAccount(String accountId) throws QueueException;
}
