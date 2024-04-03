    package com.hgroupeight.interpreter.symboltable;

    import java.util.HashMap;
    import java.util.Map;

    public class SymbolTable {
        private Map<String, Object> variables;
        public SymbolTable() {
            variables = new HashMap<>();
        }

        public void put(String name, Object value) {
            variables.put(name, value);
        }

        public Object get(String name) throws Exception {
            if (!variables.containsKey(name)) {
                throw new Exception("Undeclared variable: " + name);
            }
            return variables.get(name);
        }

        public void addVariable(String name, Object value) {
            variables.put(name, value);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("SymbolTable:\n");
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            return sb.toString();
        }
    }
