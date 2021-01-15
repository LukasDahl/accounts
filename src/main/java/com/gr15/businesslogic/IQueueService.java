/**
 * @author August
 */

package com.gr15.businesslogic;

import com.gr15.businesslogic.exceptions.QueueException;

public interface IQueueService {
    void validateAccount(String accountId) throws QueueException;
    void accountExists(String accountId) throws QueueException;
    void publishDeleteAccountEvent(String accountId) throws QueueException;
}
