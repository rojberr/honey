package dev.drzymala.jaybeehoneyapi.order.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {

    NEW {
        @Override
        public UpdateStatusResult updateStatus(OrderStatus status) {
            return switch (status) {
                case PAID -> UpdateStatusResult.ok(PAID);
                case CANCELLED -> UpdateStatusResult.revoked(CANCELLED);
                case ABANDONED -> UpdateStatusResult.revoked(ABANDONED);
                case SHIPPED -> UpdateStatusResult.ok(SHIPPED);
                default -> super.updateStatus(status);
            };
        }
    },
    CANCELLED,
    ABANDONED,
    PAID {
        @Override
        public UpdateStatusResult updateStatus(OrderStatus status) {
            if (status == SHIPPED) {
                return UpdateStatusResult.ok(SHIPPED);
            } else {
                return super.updateStatus(status);
            }
        }
    },
    SHIPPED;

    public static Optional<OrderStatus> parseString(String value) {
        return Arrays.stream(values())
                .filter(it -> StringUtils.equalsIgnoreCase(it.name(), value))
                .findFirst();
    }

    public UpdateStatusResult updateStatus(OrderStatus status) {
        throw new IllegalArgumentException("Unable to mark " + this.name() + " order as " + status.name());
    }
}
