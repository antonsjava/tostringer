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
 * Resolves property resolvers from given class.
 * @author antons
 */
public interface ClassPropertyResolver {
    
    /**
     * Returns true if class properties can be resolved for this class.
     * @param clazz - class to be checked
     * @return true or false
     */
    boolean isSupportedClass(Class clazz);     
    
    /**
     * Reads class properties for given class
     * @param clazz
     * @return class properties or null
     */
    ClassProperty[] properties(Class clazz);     
}
