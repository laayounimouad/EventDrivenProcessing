package ma.laayouni.comptecqrses.commonapi.dtos;

import lombok.Data;
import lombok.Getter;

@Data
public class CreditAccountRequestDTO {
    private String accountId;
    private double amount;
    private String currency;
}
