package com.studentconnect.gouni.platform.notifications.application.internal.outboundservices.acl;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.User;
import com.studentconnect.gouni.platform.iam.interfaces.acl.IamContextFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    public Optional<User> fetchUserById(UUID userId) {
        return iamContextFacade.fetchUserById(userId);
    }

    public UUID fetchUserIdByEmail(String email) {
        return iamContextFacade.fetchUserIdByEmail(email);
    }
}
