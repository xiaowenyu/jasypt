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
package org.jasypt.spring4.properties;

import org.jasypt.commons.CommonUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * <p>
 * Subclass of
 * <tt>org.springframework.beans.factory.config.PropertyPlaceholderConfigurer</tt>
 * which can make use of a {@link org.jasypt.encryption.StringEncryptor} or
 * {@link org.jasypt.util.text.TextEncryptor} object to decrypt property values
 * if they are encrypted in the loaded resource locations.
 * </p>
 * <p>
 * A value is considered "encrypted" when it appears surrounded by
 * <tt>ENC(...)</tt>, like:
 * </p>
 * <p>
 * <center> <tt>my.value=ENC(!"DGAS24FaIO$)</tt> </center>
 * </p>
 * <p>
 * Encrypted and unencrypted objects can be combined in the same resources file.
 * </p>
 * 
 * @since 1.9.3
 * 
 * @author Chus Picos
 * 
 */
public final class EncryptablePropertyPlaceholderConfigurer 
        extends PropertyPlaceholderConfigurer {
	/*
	 * Only one of these instances will be initialized, the other one will be
	 * null.
	 */
	private final StringEncryptor stringEncryptor;
	private final TextEncryptor textEncryptor;

	/**
	 * <p>
	 * Creates an <tt>EncryptablePropertyPlaceholderConfigurer</tt> instance
	 * which will use the passed {@link StringEncryptor} object to decrypt
	 * encrypted values.
	 * </p>
	 * 
	 * @param stringEncryptor
	 *            the {@link StringEncryptor} to be used do decrypt values. It
	 *            can not be null.
	 */
	// 配置加密的封装方法
	public EncryptablePropertyPlaceholderConfigurer(
	        final StringEncryptor stringEncryptor) {
		super();
		CommonUtils.validateNotNull(stringEncryptor, "Encryptor cannot be null");
		this.stringEncryptor = stringEncryptor;
		this.textEncryptor = null;
	}

	/**
	 * <p>
	 * Creates an <tt>EncryptablePropertyPlaceholderConfigurer</tt> instance which will use the
	 * passed {@link TextEncryptor} object to decrypt encrypted values.
	 * </p>
	 * 
	 * @param textEncryptor
	 *            the {@link TextEncryptor} to be used do decrypt values. It can
	 *            not be null.
	 */
	public EncryptablePropertyPlaceholderConfigurer(final TextEncryptor textEncryptor) {
		super();
		CommonUtils.validateNotNull(textEncryptor, "Encryptor cannot be null");
		this.stringEncryptor = null;
		this.textEncryptor = textEncryptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.PropertyResourceConfigurer#convertPropertyValue(java.lang.String)
	 */
	// 解密配置的参数
    @Override
	protected String convertPropertyValue(final String originalValue) {
		if (!PropertyValueEncryptionUtils.isEncryptedValue(originalValue)) {
			return originalValue;
		}
		if (this.stringEncryptor != null) {
			return PropertyValueEncryptionUtils.decrypt(originalValue,
					this.stringEncryptor);

		}
		return PropertyValueEncryptionUtils.decrypt(originalValue, this.textEncryptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @since 1.8
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolveSystemProperty(java.lang.String)
	 */
	// spring PropertyPlaceholderConfigurer 的调用
    @Override
    protected String resolveSystemProperty(final String key) {
        return convertPropertyValue(super.resolveSystemProperty(key));
    }
    
}
