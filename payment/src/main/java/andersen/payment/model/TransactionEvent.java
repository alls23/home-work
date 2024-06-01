package andersen.payment.model;

import andersen.payment.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private static final String EVENT = "Transaction";

    private Integer orderId;
    private TransactionStatus status;

    public TransactionEvent orderId(Integer orderId) {
        this.orderId = orderId;
        return this;
    }

    public TransactionEvent status(TransactionStatus status) {
        this.status = status;
        return this;
    }


    public String getEvent() {
        return EVENT;
    }
}
