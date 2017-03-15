/*
 * Copyright 2015 Anton Straka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sk.antons.tostringer;

import java.lang.reflect.Method;

/**
 * Reads property from public method
 * @author antons
 */
public class MethodClassProperty extends ClassProperty {
    private Method method = null;
    
    /**
     * Constructs instance using method.
     * @param field Method used to resolve property value.
     */
    public MethodClassProperty(Method method) {
        super(method2name(method));
        this.method = method;
    }

    /**
     * Constructs instance using name and field.
     * @param name - name of the property
     * @param field Field used to resolve property value.
     */
    public MethodClassProperty(String name, Method method) {
        super(name);
        this.method = method;
    }

    private static String method2name(Method m) {
        String name = m.getName();
        if((name.length() > 3) && name.startsWith("get")) name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
        else if((name.length() > 2) && name.startsWith("is") && ((m.getReturnType() == Boolean.class) || (m.getReturnType() == boolean.class))) name = Character.toLowerCase(name.charAt(2)) + name.substring(3);
        return name;
    }
    
    private static Class[] noparams = new Class[]{};

    @Override
    public Object valueFrom(Object object) {
        Object rv = null;
        try {
            rv = method.invoke(object, noparams);
        } catch (Exception e) {
            rv = "Unable to resolve property " + e;
        }
        return rv;
    }
    
}
