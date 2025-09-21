package com.studentconnect.gouni.platform.notifications.domain.model.aggregates;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.User;
import com.studentconnect.gouni.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Notification extends AuditableAbstractAggregateRoot<Notification> {
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @NotNull
    private String title;

    @NotNull
    private String message;

    public Notification(User user, String title, String message) {
        this.user = user;
        this.title = title;
        this.message = message;
    }

    public Notification() {}
}
