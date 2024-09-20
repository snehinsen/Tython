package ca.tlcp.compiler.memory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Variable {

    private String label;
    private String dataType;

    public Variable(String label, String dataType) {
        this.label = label;
        this.dataType = dataType;
    }

    public String getLabel() {
        return label;
    }

    public String getDataType() {
        return dataType;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
