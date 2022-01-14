package core.beans;
import java.util.ArrayList;
import java.util.List;

public class MutablePropertyValues {
    private final List<PropertyValue> propertyValueList;

    public MutablePropertyValues() {
        this.propertyValueList = new ArrayList<>();
    }

    public List getPropertyValueList() {
        return this.propertyValueList;
    }

    public void addPropertyValue(String propertyName, Object propertyValue) {
        addPropertyValue(new PropertyValue(propertyName, propertyValue));
    }

    public MutablePropertyValues addPropertyValue(PropertyValue pv) {
        for (int i = 0; i < this.propertyValueList.size(); i++) {
            PropertyValue currentPv = (PropertyValue) this.propertyValueList.get(i);
            if (currentPv.getName().equals(pv.getName())) {
                return this;
            }
        }
        this.propertyValueList.add(pv);
        return this;
    }

    public void setPropertyValueAt(PropertyValue pv, int i) {
        this.propertyValueList.set(i, pv);
    }

    public void removePropertyValue(PropertyValue pv) {
        this.propertyValueList.remove(pv);
    }
    public PropertyValue getPropertyValue(String propertyName) {
        for (int i = 0; i < this.propertyValueList.size(); i++) {
            PropertyValue pv = (PropertyValue) this.propertyValueList.get(i);
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    public void removePropertyValue(String propertyName) {
        removePropertyValue(getPropertyValue(propertyName));
    }

    public int size() {
        return this.propertyValueList.size();
    }

}
