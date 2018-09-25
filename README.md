
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

## ToStringerFactory

The class produces commonly used ToStringer instamces. 

```java
public class PrettyPrinter {
    private static ToStringer tostringer = 
		ToStringerFactory.simpleField("org.foo.client", "org.foo.server");
    
    public static String toString(Object object) {
        return tostringer.toString(object);
    }    
}
```

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
## ToStackTracer

The class produces shorter stack traces. It is usefull whne you want to see 
only own packages in stack trace.

```java
public class PrettyPrinter {
    private static ToStackTracer tostacktracer = 
		ToStackTracer.instance("org.foo");
    
    public static String st(Throwable t) {
        return tostacktracer.ts(t);
    }    
}
```
it produces stack traces like this where are visible org.foo packages and two line context
```
18:54:28 ERR - Unable to call get request to http://localhost:8080/mocka - /client_exist/6608271230 because of org.springframework.web.client.HttpClientErrorException: 404 null
	at org.springframework.web.client.DefaultResponseErrorHandler.handleError(DefaultResponseErrorHandler.java:94)
	at org.springframework.web.client.DefaultResponseErrorHandler.handleError(DefaultResponseErrorHandler.java:79)
	at org.springframework.web.client.ResponseErrorHandler.handleError(ResponseErrorHandler.java:63)
	at ...
	at org.springframework.web.client.RestTemplate.execute(RestTemplate.java:680)
	at org.springframework.web.client.RestTemplate.exchange(RestTemplate.java:600)
	at org.foo.integration.common.rest.JsonRestClient.getRequest(JsonRestClient.java:48)
	at org.foo.integration.proj.client.impl.RestProjClient.clientExist(RestProjClient.java:53)
	at org.foo.integration.proj.controller.ProjController.sendApplication(ProjController.java:106)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at ...
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.foo.integration.common.web.RRDumpFilter.doFilterInternal(RRDumpFilter.java:69)
	at org.foo.integration.common.web.RRDumpFilter.doFilterInternal(RRDumpFilter.java:54)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at ... 
18:54:28 WAR - Unable to call proj sendApplication java.lang.IllegalStateException: org.springframework.web.client.HttpClientErrorException: 404 null
	at org.foo.integration.common.rest.JsonRestClient.getRequest(JsonRestClient.java:68)
	at org.foo.integration.proj.client.impl.RestProjClient.clientExist(RestProjClient.java:53)
	at org.foo.integration.proj.controller.ProjController.sendApplication(ProjController.java:106)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at ...
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.foo.integration.common.web.RRDumpFilter.doFilterInternal(RRDumpFilter.java:69)
	at org.foo.integration.common.web.RRDumpFilter.doFilterInternal(RRDumpFilter.java:54)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at ...
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



