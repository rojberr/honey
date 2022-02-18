package com.jaybee.honey.order.web;

import com.jaybee.honey.order.application.port.PlaceOrderUseCase;
import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.domain.Order;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final QueryOrderUseCase query;
    private final PlaceOrderUseCase placeOrder;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getOrders() {
        return query.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Order> getById() {
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addOrder() {
        return null;
    }
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<Void> addHoney(@Valid @RequestBody CatalogController.RestHoneyCommand command) {
//        Honey honey = catalog.addHoney(command.toCreateCommand());
//        URI uri = createdHoneyURI(honey);
//        return ResponseEntity.created(uri).build();
//    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrder() {
        return;
    }
//    @PutMapping("/{id}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void updateHoney(@PathVariable Long id,
//                            @RequestBody CatalogController.RestHoneyCommand command) {
//        CatalogUseCase.UpdateHoneyResponse response = catalog.updateHoney(command.toUpdateCommand(id));
//        if (!response.isSuccess()) {
//            String message = String.join(", ", response.getErrors());
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
//        }
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleete() {
        return;
    }
//    @DeleteMapping("/{id}/cover")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void removeHoneyCover(@PathVariable Long id) {
//        catalog.removeHoneyCover(id);
//    }
}
