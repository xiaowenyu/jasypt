/*
 * =============================================================================
 * 
 *   Copyright (c) 2007-2010, The JASYPT team (http://www.jasypt.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.jasypt.properties;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.util.text.TextEncryptor;

/**
 * <p>
 * Utility class to encrypt/decrypt values in properties files which could be
 * encrypted.
 * </p>
 * <p>
 * A value is considered "encrypted" when it appears surrounded by 
 * <tt>ENC(...)</tt>, like:
 * </p>
 * <p>
 *   <center>
 *     <tt>my.value=ENC(!"DGAS24FaIO$)</tt>
 *   </center>
 * </p>
 * <p>
 *   <b>This class is meant for internal Jasypt use only.</b>
 * </p>
 * 
 * @since 1.4
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 */
public final class PropertyValueEncryptionUtils {

    private static final String ENCRYPTED_VALUE_PREFIX = "ENC(";
    private static final String ENCRYPTED_VALUE_SUFFIX = ")";

    
    public static boolean isEncryptedValue(final String value) {
        if (value == null) {
            return false;
        }
        final String trimmedValue = value.trim();
        return (trimmedValue.startsWith(ENCRYPTED_VALUE_PREFIX) && 
                trimmedValue.endsWith(ENCRYPTED_VALUE_SUFFIX));
    }

    // ??ENC()???
    private static String getInnerEncryptedValue(final String value) {
        return value.substring(
                ENCRYPTED_VALUE_PREFIX.length(),
                (value.length() - ENCRYPTED_VALUE_SUFFIX.length()));
    }

    // ??
    public static String decrypt(
            final String encodedValue, final StringEncryptor encryptor) {
        // ??
        return encryptor.decrypt(getInnerEncryptedValue(encodedValue.trim()));
    }

    
    public static String decrypt(
            final String encodedValue, final TextEncryptor encryptor) {
        return encryptor.decrypt(getInnerEncryptedValue(encodedValue.trim()));
    }

    
    public static String encrypt(
            final String decodedValue, final StringEncryptor encryptor) {
        return 
            ENCRYPTED_VALUE_PREFIX + 
            encryptor.encrypt(decodedValue) +
            ENCRYPTED_VALUE_SUFFIX;
    }

    
    public static String encrypt(
            final String decodedValue, final TextEncryptor encryptor) {
        return 
            ENCRYPTED_VALUE_PREFIX + 
            encryptor.encrypt(decodedValue) +
            ENCRYPTED_VALUE_SUFFIX;
    }
    
    
    private PropertyValueEncryptionUtils() {
        super();
    }

    
}
