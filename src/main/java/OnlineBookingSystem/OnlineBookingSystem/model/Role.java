package OnlineBookingSystem.OnlineBookingSystem.model;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;

@Setter
@Getter
@Builder
@ToString
@Entity(name = "role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

  @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public Role(RoleType roleType) {
        this.roleType = roleType;

    }


    @Override
    public String toString() {
        return "Role{" +
                "roleStatus=" + roleType +
                '}';
    }



}
