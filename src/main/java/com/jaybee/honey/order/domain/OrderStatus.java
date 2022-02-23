package com.jaybee.honey.order.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {

    NEW {
        @Override
        public OrderStatus updateStatus(OrderStatus status) {
            return switch (status) {
                case PAID -> PAID;
                case CANCELLED -> CANCELLED;
                case ABANDONED -> ABANDONED;
                case SHIPPED -> SHIPPED;
                default -> super.updateStatus(status);
            };
        }
    },
    CANCELLED,
    ABANDONED,
    PAID {
        @Override
        public OrderStatus updateStatus(OrderStatus status) {
            if (status == SHIPPED) {
                return SHIPPED;
            } else {
                super.updateStatus(status);
                return status;
            }
        };
    },
    SHIPPED;

    public static Optional<OrderStatus> parseString(String value) {
        return Arrays.stream(values())
                .filter(it -> StringUtils.equalsIgnoreCase(it.name(), value))
                .findFirst();
    }

    public OrderStatus updateStatus(OrderStatus status) {
        throw new IllegalArgumentException("Unable to mark " + this.name() + " order as " + status.name());
    }
}
