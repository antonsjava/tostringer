
# ToStringer

ToStringer is helper class for generating structured toString representations
for classes you don't have in control. It is possible to use ToStringer also for 
your own classes. If you want to have simple toStrings and only wants to have 
structured output only for some debug purposes. 

## ClassProperty

This class represents class porperty and ability to resolve the property from 
given object.

There are provided two implementations MethodClassProperty and FieldClassProperty
which resolves properties using public members of java class.

## ClassPropertyResolver

Resolves list of class properties for given class. There is provided implementation 
FieldScanClassPropertyResolver which scan class for public properties or public getters 
for thyis properties.

Class implementation limits this scanning only for classes which names start by 
specified list of prefixes.

## Example

This example shows how to create class PrettyPrint which uses ToStringer for classes 
from tvo packages and for Element class.

```java

public class PrettyPrinter {
    private static ToStringer tostringer = new ToStringer(
        new ClassPropertyResolver[] {
            new FieldScanClassPropertyResolver(
                "org.foo.client.message"
                , "org.foo.client.responses"
            )
            , new ElementClassPropertyResolver()
        }
    );
    
    public static String toString(Object object) {
        return tostringer.toString(object);
    }    

    public static class ElementClassPropertyResolver implements ClassPropertyResolver {

        public boolean isSupportedClass(Class type) {
            if(type == null) return false;
            return Element.class.isAssignableFrom(type);
        }
        
        private static ClassProperty[] props = new ClassProperty[]{
            new ElementClassProperty("element")
            , new ElementClassProperty("attrs")
            , new ElementClassProperty("value")
            , new ElementClassProperty("subelement")
        };

        public ClassProperty[] properties(Class type) {
            return props;
        }
    }
    
    public static class ElementClassProperty extends ClassProperty {
        public ElementClassProperty(String name) {
            super(name);
        }

        @Override
        public Object valueFrom(Object o) {
            if(o == null) return null;
            if(!(o instanceof Element)) return null;
            Element elem = (Element)o;
            String name = getName();
            if("element".equals(name)) {
                return elem.getLocalName();
            } else if("attrs".equals(name)) {
                NamedNodeMap nnmap = elem.getAttributes();
                ...
            } else if("subelement".equals(name)) {
                NodeList nl = elem.getChildNodes();
                ...
            } else if("value".equals(name)) {
                NodeList nl = elem.getChildNodes();
                ...
												} else {
                return null;
            }
        }

    }
}
```

## Maven usage

```
   <dependency>
      <groupId>com.github.antonsjava</groupId>
      <artifactId>tostringer</artifactId>
      <version>1.0</version>
   </dependency>
```

## OSGI usage (Karaf)

```
   bundle:install mvn:com.github.antonsjava/tostringer/1.0
   bundle:start com.github.antonsjava.tostringer/1.0.0
```



