package com.jaybee.honey.order.application.port;

import com.jaybee.honey.commons.Either;
import com.jaybee.honey.order.domain.OrderStatus;
import com.jaybee.honey.order.domain.Recipient;
import lombok.*;

import java.util.List;

public interface ManipulateOrderUseCase {
    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    void deleteOrderById(Long id);

    UpdateStatusResponse updateOrderStatus(UpdateStatusCommand command);

    @Builder
    @Value
    @AllArgsConstructor
    class PlaceOrderCommand {
        @Singular
        List<OrderItemCommand> items;
        Recipient recipient;
    }

    @Value
    class OrderItemCommand {
        Long honeyId;
        int quantity;

        @SneakyThrows
        public OrderItemCommand(Long honeyId, int quantity) {
            this.honeyId = honeyId;
            if (quantity <= 0) {
                throw new Exception("The ordered quantity cannot be equal or less than zero");
            }
            this.quantity = quantity;
        }
    }

    @Value
    class UpdateStatusCommand {
        Long orderId;
        OrderStatus status;
        String email;
    }


    class PlaceOrderResponse extends Either<String, Long> {
        public PlaceOrderResponse(boolean success, String left, Long right) {
            super(success, left, right);
        }

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, null, orderId);
        }

        public static PlaceOrderResponse failure(String error) {
            return new PlaceOrderResponse(false, error, null);
        }
    }

    class UpdateStatusResponse extends Either<String, OrderStatus> {
        public UpdateStatusResponse(boolean success, String left, OrderStatus right) {
            super(success, left, right);
        }

        public static UpdateStatusResponse success(OrderStatus status) {
            return new UpdateStatusResponse(true, null, status);
        }

        public static UpdateStatusResponse failure(String error) {
            return new UpdateStatusResponse(false, error, null);
        }
    }
}