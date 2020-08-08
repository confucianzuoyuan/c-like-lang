package BackEnd;

import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalEnvironment {
    private Map<String, Object> map = new LinkedHashMap<>();

    public Environment globals = new Environment(null);

    public void defineObject(String name, Object node) {
        map.put(name, node);
    }

    public Object resolveObject(String name) {
        return map.get(name);
    }
}
