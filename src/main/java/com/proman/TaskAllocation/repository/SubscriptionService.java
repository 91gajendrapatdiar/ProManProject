package com.proman.TaskAllocation.repository;

import com.proman.TaskAllocation.model.PlanType;
import com.proman.TaskAllocation.model.Subscription;
import com.proman.TaskAllocation.model.User;

public interface SubscriptionService {

    Subscription createSubscription(User user);

    Subscription getUsersSubscription(Long userId)throws  Exception;

    Subscription upgardeSubscription(Long userId, PlanType planType);

    boolean inValid(Subscription subscription);
}
