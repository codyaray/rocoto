/*
 *    Copyright 2009-2010 The Rocoto Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.rocoto.converters;

import java.util.Locale;

import com.google.inject.TypeLiteral;

/**
 * 
 * @author Simone Tripodi
 * @version $Id$
 */
@Converts({ Locale.class, Locale[].class })
public final class LocaleConverter extends AbstractConverter {

    @Override
    protected Object simpleConvert(String value, TypeLiteral<?> toType) {
        int separator = value.indexOf('-');
        if (separator != -1) {
            String language = value.substring(0, separator);
            String country = value.substring(separator + 1);
            return new Locale(language, country);
        }
        return new Locale(value);
    }

}