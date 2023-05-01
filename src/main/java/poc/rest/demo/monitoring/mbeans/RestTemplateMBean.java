package poc.rest.demo.monitoring.mbeans;

import javax.management.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestTemplateMBean implements DynamicMBean {
    public static final String REST_TEMPLATE_INSECURE_OBJ="restTemplateInsecureObj";
    public static final String REST_TEMPLATE_SECURE_OBJ="restTemplateSecureObj";
    public static final String REST_TEMPLATE_INSECURE_MBEAN_NAME = "RestTemplateInsec:type=Rest,name=\"insecurerestTemplate\"";
    private final Map<String, Attribute> attributes;


    public RestTemplateMBean() {
        attributes=new HashMap<>();
    }

    public Attribute addAttribute(String attributeName, Object attributeObj) throws ReflectionException, AttributeNotFoundException, MBeanException, InvalidAttributeValueException {
        Attribute attribute=new Attribute(attributeName,attributeObj);
        setAttribute(attribute);
        return attribute;
    }
    @Override
    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        return attributes.get(attribute);
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        String name = attribute.getName();
        attributes.put(name,attribute);
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        return new AttributeList(new ArrayList<>(this.attributes.values()));
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        attributes.asList().forEach(a-> this.attributes.put(a.getName(),a));
        return attributes;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        MBeanInfo mBeanInfo=new MBeanInfo(this.getClass().getCanonicalName(),
                null,null,null,
                null,null);
        return mBeanInfo;
    }
}
