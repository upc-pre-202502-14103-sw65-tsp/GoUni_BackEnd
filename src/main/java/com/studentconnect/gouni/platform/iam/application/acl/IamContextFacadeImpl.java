package com.studentconnect.gouni.platform.iam.application.acl;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.User;
import com.studentconnect.gouni.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.studentconnect.gouni.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.studentconnect.gouni.platform.iam.domain.services.UserCommandService;
import com.studentconnect.gouni.platform.iam.domain.services.UserQueryService;
import com.studentconnect.gouni.platform.iam.interfaces.acl.IamContextFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IamContextFacadeImpl implements IamContextFacade {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Override
    public Optional<User> fetchUserById(UUID userId) {
        return userQueryService.handle(new GetUserByIdQuery(userId));
    }

    @Override
    public UUID fetchUserIdByEmail(String email) {
        var getUserByEmailQuery = new GetUserByEmailQuery(email);
        var result = userQueryService.handle(getUserByEmailQuery);
        if (result.isEmpty())
            return null;
        return result.get().getId();
    }
}
