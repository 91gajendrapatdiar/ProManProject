package com.proman.TaskAllocation.repository;

import com.proman.TaskAllocation.model.PlanType;
import com.proman.TaskAllocation.model.Subscription;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;



@Service
public class SubscriptionServiceImpl  implements  SubscriptionService{

    @Autowired
    private UserService userService;
    @Autowired
    private  SubscriptionRepository subscriptionRepository;
    @Override
    public Subscription createSubscription(User user) {
        Subscription subscription=  new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlanType(PlanType.FREE);

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getUsersSubscription(Long userId) throws Exception {

        Subscription subscription =  subscriptionRepository.findByUserId(userId);
        if(!inValid(subscription)){
            subscription.setPlanType((PlanType.FREE));
            subscription.setSubscriptionStartDate(LocalDate.now().plusMonths(12));
            subscription.setSubscriptionStartDate(LocalDate.now());

        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription upgardeSubscription(Long userId, PlanType planType) {
        Subscription subscription= subscriptionRepository.findByUserId(userId);
        subscription.setPlanType(planType);
        subscription.setSubscriptionStartDate(LocalDate.now());
        if(planType.equals(planType.ANNUALLY)){
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        }else {
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(1));
        }

        return  subscriptionRepository.save(subscription);
    }

    @Override
    public boolean inValid(Subscription subscription) {
        if(subscription.getPlanType().equals(PlanType.FREE))
            return  true;

        LocalDate endDate =  subscription.getSubscriptionEndDate();
        LocalDate currentDate = LocalDate.now();

        return endDate.isAfter(currentDate) || endDate.isEqual(currentDate);
    }
}
