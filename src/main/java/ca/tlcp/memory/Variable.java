package ca.tlcp.memory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Variable {

    private String label;
    private String dataType;
    private boolean isArray;


}
