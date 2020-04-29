package dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public final class TruckStatusDTO {
    Long totalTrucksNumber;
    Long totalBrokenNumber;
    Long totalRestNumber;
}
