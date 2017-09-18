package org.restbucks.tdd.domain.ordering;

import static lombok.AccessLevel.PRIVATE;
import static org.restbucks.tdd.domain.ordering.Order.Status.PAYMENT_EXPECTED;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Getter
@NoArgsConstructor(access = PRIVATE)
public class Order {

    private Identity id;

    private Location location;

    private Status status = PAYMENT_EXPECTED;

    private Order(Identity id) {
        this.id = id;
    }

    public void with(Location location) {
        this.location = location;
    }

    public static Order newOrder() {
        return new Order(Identity.next());
    }

    @Value
    public static class Identity {

        private String value;

        public static Identity of(String value) {
            return new Identity(value);
        }

        public static Identity next() {
            return of(UUID.randomUUID().toString());
        }
    }

    /**
     * Enumeration for all the statuses an {@link Order} can be in.
     */
    public enum Status {

        /**
         * Placed, but not payed yet. Still changeable.
         */
        PAYMENT_EXPECTED,

        /**
         * {@link Order} was payed. No changes allowed to it anymore.
         */
        PAID,

        /**
         * The {@link Order} is currently processed.
         */
        PREPARING,

        /**
         * The {@link Order} is ready to be picked up by the customer.
         */
        READY,

        /**
         * The {@link Order} was completed.
         */
        TAKEN;
    }
}
