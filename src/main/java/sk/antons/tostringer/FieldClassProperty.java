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

/**
 * Reads property from public field
 * @author antons
 */
public class FieldClassProperty extends ClassProperty {
    private Field field = null;
    
    /**
     * Constructs instance using field.
     * @param field Field used to resolve property value.
     */
    public FieldClassProperty(Field field) {
        super(field.getName());
        this.field = field;
    }
    
    /**
     * Constructs instance using name and field.
     * @param name - name of the property
     * @param field Field used to resolve property value.
     */
    public FieldClassProperty(String name, Field field) {
        super(name);
        this.field = field;
    }

    @Override
    public Object valueFrom(Object object) {
        Object rv = null;
        try {
            rv = field.get(object);
        } catch (Exception e) {
            rv = "Unable to resolve property " + e;
        }
        return rv;
    }
    
}
