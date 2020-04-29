package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public final class DriverStatusDTO {
    Long totalDrivers;
    Long driversOnRest;
}
