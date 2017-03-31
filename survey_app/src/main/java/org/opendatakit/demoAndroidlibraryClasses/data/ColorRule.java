package org.opendatakit.demoAndroidlibraryClasses.data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.TreeMap;
import java.util.UUID;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.demoAndroidlibraryClasses.database.data.Row;

public class ColorRule {
    public static final String TAG = "ColColorRule";
    private String mId;
    private String mElementKey;
    private RuleType mOperator;
    private String mValue;
    private int mForeground;
    private int mBackground;

    private ColorRule() {
    }

    public ColorRule(String colElementKey,RuleType compType, String value, int foreground, int background) {
        this(UUID.randomUUID().toString(), colElementKey, compType, value, foreground, background);
    }

    public ColorRule(String id, String colName, RuleType compType, String value, int foreground, int background) {
        this.mId = id;
        this.mElementKey = colName;
        this.mOperator = compType;
        this.mValue = value;
        this.mForeground = foreground;
        this.mBackground = background;
    }

    public TreeMap<String, Object> getJsonRepresentation() {
        TreeMap map = new TreeMap();
        map.put("mValue", this.mValue);
        map.put("mElementKey", this.mElementKey);
        map.put("mOperator", this.mOperator.name());
        map.put("mId", this.mId);
        map.put("mForeground", Integer.valueOf(this.mForeground));
        map.put("mBackground", Integer.valueOf(this.mBackground));
        return map;
    }

    @JsonIgnore
    public String getRuleId() {
        return this.mId;
    }

    @JsonIgnore
    public String getColumnElementKey() {
        return this.mElementKey;
    }

    @JsonIgnore
    public String getVal() {
        return this.mValue;
    }

    public void setVal(String newVal) {
        this.mValue = newVal;
    }

    @JsonIgnore
    public int getForeground() {
        return this.mForeground;
    }

    public String toString() {
        return "[id=" + this.getRuleId() + ", elementKey=" + this.getColumnElementKey() + ", operator=" + this.getOperator() + ", value=" + this.getVal() + ", background=" + this.getBackground() + ", foreground=" + this.getForeground() + "]";
    }

    public boolean equals(Object o) {
        if(!(o instanceof ColorRule)) {
            return false;
        } else {
         ColorRule other = (ColorRule)o;
            boolean sameId = this.mId == null?other.mId == null:this.mId.equals(other.mId);
            return sameId && this.equalsWithoutId(other);
        }
    }

    public boolean equalsWithoutId(ColorRule other) {
        if(this.mBackground != other.mBackground) {
            return false;
        } else if(this.mForeground != other.mForeground) {
            return false;
        } else {
            label44: {
                if(this.mOperator == null) {
                    if(other.mOperator == null) {
                        break label44;
                    }
                } else if(this.mOperator == other.mOperator) {
                    break label44;
                }

                return false;
            }

            if(this.mElementKey == null) {
                if(other.mElementKey != null) {
                    return false;
                }
            } else if(!this.mElementKey.equals(other.mElementKey)) {
                return false;
            }

            if(this.mValue == null) {
                if(other.mValue != null) {
                    return false;
                }
            } else if(!this.mValue.equals(other.mValue)) {
                return false;
            }

            return true;
        }
    }

    public void setForeground(int newForeground) {
        this.mForeground = newForeground;
    }

    @JsonIgnore
    public int getBackground() {
        return this.mBackground;
    }

    public void setBackground(int newBackground) {
        this.mBackground = newBackground;
    }

    @JsonIgnore
    public RuleType getOperator() {
        return this.mOperator;
    }

    public void setOperator(RuleType newOperator) {
        this.mOperator = newOperator;
    }

    public void setColumnElementKey(String elementKey) {
        this.mElementKey = elementKey;
    }

    public boolean checkMatch(ElementDataType type, Row row) {
        try {
            String e = row.getDataByKey(this.mElementKey);
            if(e == null) {
                return false;
            } else {
                int compVal;
                if(type != ElementDataType.number && type != ElementDataType.integer) {
                    compVal = e.compareTo(this.mValue);
                } else {
                    double doubleValue = Double.parseDouble(e);
                    double doubleRule = Double.parseDouble(this.mValue);
                    compVal = Double.valueOf(doubleValue).compareTo(Double.valueOf(doubleRule));
                }

                switch(mOperator.ordinal()) {
                    case 1:
                        return compVal < 0;
                    case 2:
                        return compVal <= 0;
                    case 3:
                        return compVal == 0;
                    case 4:
                        return compVal >= 0;
                    case 5:
                        return compVal > 0;
                    default:
                        throw new IllegalArgumentException("unrecognized op passed to checkMatch: " + this.mOperator);
                }
            }
        } catch (NumberFormatException var9) {
            var9.printStackTrace();
            throw new IllegalArgumentException("error parsing value as number, removing the offending rule");
        }
    }

    public static enum RuleType {
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL("<="),
        EQUAL("="),
        GREATER_THAN_OR_EQUAL(">="),
        GREATER_THAN(">"),
        NO_OP("operation value");

        private static final String STR_NULL = "null";
        private static final String STR_LESS_THAN = "<";
        private static final String STR_LESS_OR_EQUAL = "<=";
        private static final String STR_EQUAL = "=";
        private static final String STR_GREATER_OR_EQUAL = ">=";
        private static final String STR_GREATER_THAN = ">";
        private static final int NUM_VALUES_FOR_SPINNER = 5;
        private String symbol;

        private RuleType(String symbol) {
            this.symbol = symbol;
        }

        public static CharSequence[] getValues() {
            CharSequence[] result = new CharSequence[]{"<", "<=", "=", ">=", ">"};
            return result;
        }

        public String getSymbol() {
            return this.symbol == null?"null":this.symbol;
        }

        public static RuleType getEnumFromString(String inputString) {
            if(inputString.equals(LESS_THAN.symbol)) {
                return LESS_THAN;
            } else if(inputString.equals(LESS_THAN_OR_EQUAL.symbol)) {
                return LESS_THAN_OR_EQUAL;
            } else if(inputString.equals(EQUAL.symbol)) {
                return EQUAL;
            } else if(inputString.equals(GREATER_THAN_OR_EQUAL.symbol)) {
                return GREATER_THAN_OR_EQUAL;
            } else if(inputString.equals(GREATER_THAN.symbol)) {
                return GREATER_THAN;
            } else if(!inputString.equals("") && !inputString.equals(" ")) {
                throw new IllegalArgumentException("unrecognized rule operator: " + inputString);
            } else {
                return NO_OP;
            }
        }
    }
}
