package BackEnd;

import AST.ASTNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> map = new LinkedHashMap<>();
    private final Environment enclosingEnv;

    public Environment(Environment enclosingScope) {
        this.enclosingEnv = enclosingScope;
    }

    public void define(String name, Object value) {
        map.put(name, value);
    }

    public Object getInfoCurrent(String name) {
        return map.get(name);
    }

    public Object getInfo(String name) {
        Object t = map.get(name);

        if (t != null) {
            return t;
        }

        // 到上一层作用域寻找变量
        if (enclosingEnv != null) {
            return enclosingEnv.getInfo(name);
        }

        return null;
    }

    public void setInfo(String name, Object value) {
        Object t = map.get(name);

        if (t != null) {
            map.put(name, value);
        }

        if (enclosingEnv != null) {
            enclosingEnv.setInfo(name, value);
        }
    }

    public Environment getEnclosingEnv() {
        return enclosingEnv;
    }

}