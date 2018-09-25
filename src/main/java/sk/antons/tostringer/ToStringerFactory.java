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
 * Factory class for common ToStringer instances.
 * @author antons
 */
public class ToStringerFactory {

    /**
     * Create instance of ToStringer with field scanner 
     * and classes to be scanned are recognized using fqdn name prefixes.
     * @param prefixes List of fqdn prefixes of classes to be scanned.
     */
    public static ToStringer simpleField(String... prefixes) {

        ToStringer rv = new ToStringer(
            new ClassPropertyResolver[] {
                new FieldScanClassPropertyResolver(
                    prefixes
                )
            }
        );
        return rv;
    }


}
