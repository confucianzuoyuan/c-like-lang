package IR;

import java.util.LinkedHashMap;
import java.util.Map;

public class IRRoot {
    public Map<String, Function> functions = new LinkedHashMap<>();

    public void accept(IIRVisitor visitor) {
        visitor.visit(this);
    }
}
