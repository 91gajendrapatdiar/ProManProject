package com.proman.TaskAllocation.controller;

import com.proman.TaskAllocation.model.PlanType;
import com.proman.TaskAllocation.model.Subscription;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.repository.SubscriptionRepository;
import com.proman.TaskAllocation.repository.SubscriptionService;
import com.proman.TaskAllocation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<Subscription>  getUserSubscription(@RequestHeader("Authorization") String jwt)
        throws  Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Subscription subscription = subscriptionService.getUsersSubscription(user.getId());
        return  new ResponseEntity<>(subscription, HttpStatus.OK);

    }

    @PatchMapping("/upgrade")
    public ResponseEntity<Subscription>  upgradeSubscription(@RequestHeader("Authorization") String jwt,
                                                             @RequestParam PlanType planType)
            throws  Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Subscription subscription = subscriptionService.upgardeSubscription(user.getId(), planType);
        return  new ResponseEntity<>(subscription, HttpStatus.OK);

    }


}
