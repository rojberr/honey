package com.jaybee.honey.order.web;

import com.jaybee.honey.order.application.RichOrder;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.domain.OrderStatus;
import com.jaybee.honey.web.CreatedURI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.jaybee.honey.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;
import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
class OrderController {
    private final ManipulateOrderUseCase manipulateOrder;
    private final QueryOrderUseCase queryOrder;

    // Available only for ADMIN
    @GetMapping
    public List<RichOrder> getOrders() {
        return queryOrder.findAll();
    }

    // Available only for OWNER or ADMIN
    @GetMapping("/{id}")
    public ResponseEntity<RichOrder> getOrderById(@PathVariable Long id) {
        return queryOrder.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<Object> createOrder(@RequestBody PlaceOrderCommand command) {
        return manipulateOrder
                .placeOrder(command)
                .handle(
                        orderId -> ResponseEntity.created(orderUri(orderId)).build(),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    URI orderUri(Long orderId) {
        return new CreatedURI("/" + orderId).uri();
    }

    // Every status change for ADMIN
    // Cancelling only for OWNER
    @PatchMapping("/{id}/status")
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<Object> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        OrderStatus orderStatus = OrderStatus
                .parseString(status)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Unknown status: " + status));
        UpdateStatusCommand command = new UpdateStatusCommand(id, orderStatus, null);
        return manipulateOrder
                .updateOrderStatus(command)
                .handle(
                        success -> ResponseEntity.status(ACCEPTED).body(success),
                        error -> ResponseEntity.status(BAD_REQUEST).body(error)
                );
    }

    // Available only for ADMIN
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        manipulateOrder.deleteOrderById(id);
    }
}
