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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Resolves class properties from class. It scans fields defined in class and 
 * tries to find public 'get' accessors for this attributes.
 * 
 * It uses set of FQN name prefixes to identify supported classes.
 * @author antons
 */
public class FieldScanClassPropertyResolver implements ClassPropertyResolver {
    String[] prefixes = null;
    
    /**
     * Create instance using list of prefixes
     * @param prefixes list of prefixes
     */
    public FieldScanClassPropertyResolver(String... prefixes) {
        this.prefixes = prefixes;
    }
       
    private static class ClassInfo {
        Class clazz = null;
        ClassProperty[] properties = null;
    }



    private boolean isInternalClass(String clName) {
        if(clName == null) return false;
        if(prefixes == null) return false;
        for(String classe : prefixes) {
            if(clName.startsWith(classe)) return true;
        }
        return false;
    }

    private Map<String, ClassInfo> classInfos = new HashMap<String, ClassInfo>();
    private Set<String> badClasses = new HashSet<String>();

    /**
     * Returns true if given class name is prefixed by one of the prefix.
     * @param clazz - class to be checked
     * @return true if class is prefixed
     */
    public boolean isSupportedClass(Class clazz) {
        String cname = clazz.getName();
        if(classInfos.containsKey(cname)) return true;
        if(badClasses.contains(cname)) return false;
        boolean rv = false;
        if(prefixes != null) {
            for(String classe : prefixes) {
                if(cname.startsWith(classe)) {
                    rv = true;
                    break;
                }
            }
        }
        if(!rv) badClasses.add(cname);
        return rv;
    }

    /**
     * It returns class properties for supported class. 
     * @param clazz - given class
     * @return 
     */
    public ClassProperty[] properties(Class clazz) {
        ClassInfo cinfo = classInfo(clazz);
        if(cinfo == null) return null;
        return cinfo.properties;
    }


    private ClassInfo classInfo(Class clazz) {
        if(clazz == null) return null;
        String cname = clazz.getName();
        ClassInfo cinfo = classInfos.get(cname);
        if(cinfo == null) {
            if(isInternalClass(cname)) {
                cinfo = new ClassInfo();
                cinfo.clazz = clazz;
                List<ClassProperty> list = new ArrayList<ClassProperty>();
                Field[] fields = clazz.getDeclaredFields();
                if(fields != null) {
                    Class[] params = new Class[]{}; 
                    for(Field field : fields) {
                        if(field.isEnumConstant()) continue;
                        if(field.isSynthetic()) continue;
                        if((field.getModifiers() & Modifier.STATIC) != 0) continue;
                        if((field.getModifiers() & Modifier.PUBLIC) != 0) {
                            FieldClassProperty aa = new FieldClassProperty(field);
                            list.add(aa);
                        } else {
                            String prefix = "get";
                            String name = field.getName();
                            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                            Class rclazz = field.getType();
                            if(rclazz.equals(boolean.class) || rclazz.equals(Boolean.class)) prefix = "is";
                            Method m = null;
                            try {
                                m = clazz.getDeclaredMethod(prefix + name, params);
                            } catch(Exception e) {
                                //ignore
                            }
                            if(m != null) {
                                if((m.getModifiers() & Modifier.PUBLIC) != 0) {
                                    list.add(new MethodClassProperty(field.getName(), m));
                                }
                            } else {
                                try {
                                    m = clazz.getDeclaredMethod(field.getName(), params);
                                } catch(Exception e) {
                                    //ignore
                                }
                                if(m != null) {
                                    if((m.getModifiers() & Modifier.PUBLIC) != 0) {
                                        list.add(new MethodClassProperty(field.getName(), m));
                                    }
                                }
                            }
                        }
                    }
                }
                Collections.sort(list);
                ClassInfo pinfo = classInfo(clazz.getSuperclass());
                if(pinfo != null) {
                    ClassProperty[] parray = pinfo.properties;
                    if((parray != null) && (parray.length > 0)) {
                        List<ClassProperty> nl = new ArrayList<ClassProperty>();
                        for(ClassProperty cp : parray) {
                            nl.add(cp);
                        }
                        nl.addAll(list);
                        list = nl;
                    }
                    
                }
                cinfo.properties = list.toArray(new ClassProperty[]{});
                classInfos.put(cname, cinfo);
            }
        }
        return cinfo;
    }
}
