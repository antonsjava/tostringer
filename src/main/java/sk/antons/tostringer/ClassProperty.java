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

/**
 * Abstract class for property accessor. Class keeps name of the property and 
 * defines method for accessing the property.
 * @author antons
 */
public abstract class ClassProperty implements Comparable<ClassProperty>{
    private String name = null;
    
    /**
     * Constructs instace using name of the property
     * @param name - name of the property
     */
    public ClassProperty(String name) {
        this.name = name;
    }

    /**
     * Redsa property value from fiven object.
     * @param object - object from there property should be resolved.
     * @return - value of property
     */
    public abstract Object valueFrom(Object object);

    /**
     * Property name getter
     * @return name of the property
     */
    public String getName() {
        return name;
    }

    /**
     * Compares instances usink name value.
     * @param o other instance to compare
     * @return 
     */
    public int compareTo(ClassProperty o) {
        String name1 = getName();
        String name2 = null;
        if(o != null) name2 = o.getName();
        if(name1 == null) name1 = "";
        if(name2 == null) name2 = "";
        return name1.compareTo(name2);
    }


        
}
