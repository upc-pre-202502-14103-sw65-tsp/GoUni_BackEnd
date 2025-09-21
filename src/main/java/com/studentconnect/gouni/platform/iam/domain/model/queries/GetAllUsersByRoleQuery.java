package com.studentconnect.gouni.platform.iam.domain.model.queries;

import com.studentconnect.gouni.platform.iam.domain.model.valueobjects.Roles;

public record GetAllUsersByRoleQuery(Roles role) {
}
