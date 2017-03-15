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

import java.util.Collection;
import java.util.Map;

/**
 * Generate string representation for given objects. It uses list of class 
 * property resolvers to obtain properties of given objects.
 * @author antons
 */
public class ToStringer {
    private int listLimit = 0; 
    private ClassPropertyResolver[] resolvers = null;

    /**
     * Create instance using list of resolvers.
     * @param resolvers List of class property resolvers.
     */
    public ToStringer(ClassPropertyResolver... resolvers) {
        this.resolvers = resolvers;
    }

    /**
     * List properties limit. Limits ouput for list like properties 
     * @return 0 - unlimited, >0 limits output of list like properties
     */
    public int getListLimit() {
        return listLimit;
    }

    /**
     * Sets limit for list like properties.
     * @param listLimit new value for limit (0 is unlimited)
     */
    public void setListLimit(int listLimit) {
        this.listLimit = listLimit;
    }


    /**
     * Finds class property resolver for given class.
     * @param clazz
     * @return class property resolver or null
     */
    protected ClassPropertyResolver getResolver(Class clazz) {
        if(clazz == null) return null;
        if(resolvers == null) return null;
        for(ClassPropertyResolver resolver : resolvers) {
            if(resolver.isSupportedClass(clazz)) return resolver;
        }
        return null;
    } 

    /**
     * Add object representation to string buffer using specified prefix. 
     * @param sb - string holder
     * @param prefix - prefix for given object 
     * @param object - object to be converted to string
     */
    protected void toString(StringBuilder sb, String prefix, Object object) {
        if(object == null) {
            //sb.append(prefix).append("=null");
            return;
        }
        
        if(object instanceof Object[]) {
            Object[] objects = (Object[])object;
            sb.append(prefix).append(".size=").append(objects.length);
            int num=0;
            for(Object object1 : objects) {
                if((listLimit > 0) && (num >= listLimit)) {
                    sb.append(prefix).append('[').append(num).append(']').append("=...more values");
                    break;
                }
                toString(sb, prefix + "[" + (num++) + "]", object1);
            }
            return;
        }

        if(object instanceof Collection) {
            Collection col = (Collection)object;
            sb.append(prefix).append(".size=").append(col.size());
            int num=0;
            for(Object object1 : col) {
                if((listLimit > 0) && (num >= listLimit)) {
                    sb.append(prefix).append('[').append(num).append(']').append("=...more values");
                    break;
                }
                toString(sb, prefix + "[" + (num++) + "]", object1);
            }
            return;
        }
        
        if(object instanceof Map) {
            Map map = (Map)object;
            sb.append(prefix).append(".size=").append(map.size());
            int num=0;
            for(Object o : map.entrySet()) {
                Map.Entry entry = (Map.Entry)o;
                if((listLimit > 0) && (num >= listLimit)) {
                    sb.append(prefix).append('[').append(entry.getKey()).append(']').append("=...more values");
                    break;
                }
                toString(sb, prefix + "[" + entry.getKey() + "]", entry.getValue());
                num++;
            }
            return;
        }
        
        
        ClassPropertyResolver resolver = getResolver(object.getClass());
        
        if(resolver == null) {
            sb.append(prefix).append("=").append(object);
            return;
        }
        
        ClassProperty[] properties = resolver.properties(object.getClass());
        
        if(properties == null) return;

        for(ClassProperty ai : properties) {
            toString(sb, prefix + "." + ai.getName(), ai.valueFrom(object));
        }
    }
    
    /**
     * Converts gigen object to String 
     * @param object - to be converted
     * @return string representation of object
     */
    public String toString(Object object) {
        if(object == null) return null;
        StringBuilder sb = new StringBuilder(); 
        sb.append(object.getClass().getSimpleName()).append(':').append(object.hashCode()).append(" (");
        toString(sb, "\n ", object);
        sb.append("\n)");
        return sb.toString();
    }

}
