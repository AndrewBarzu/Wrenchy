package com.example.xghos.loginregister;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

class MCrypt {

	static char[] HEX_CHARS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	private  String SECRET_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMkxXz9YW0aaRUTSaqdeTf76SGsmbzxJchc3hOlKk5HdFWNrOLcYaCaI34teGUcyqTLyqz/0ig11wLqJQ92knLL3ut9lemqgcRhEStKgW+6PgwJmVlUBmiCs2mFcX3zuxdkWqLeYQfXrTiduVEKzk25eXe4C39nPQZXoKtIuQ1ZBAgMBAAECgYEAh/EeRHT1J51xlHEAs5oa5WBDsnLD0l5mAp325pMHZxWVMBnwtnZ9WRw+v7KTLfJjNROW9p3K8hFQajn5azeg0HG1owdycRI3cFUViLSOhr6n7w8rTcc0qABFxNLJeWZShxRUQVbvt3+ypfGKcNSvdypOqUWBo2rTJQGJTnB9N2kCQQDjMR0+ooo+CiiXQLY/owbmm2tKOFH/FqaxuLvCICD1BHmRQIbdFDhGGfWfeUV2IpHKBlzCLQ0Zq+yXqM8FDTs3AkEA4rRNofAqcstf2Zbzs4YphezFlXAcIbS/dAca6+vsyFO+RNXrCd2E8ogL0DexekGzNgSlMlgrV/8WSOvUjgBmRwJAEzR/qF/NF9l7/O03esGpP607sJZ6N/oONU/Mb2iP4KZ0MFcoD2A6MZLYNJbnzEi30kAfbzqntNfXtCpM9bxcmQJASQ7R9d3v0A66VOTQpIoX15D2G7KFXldhW4LuqhjdtbFHH+fQVt2MepxdMjzAdSW3GzFkC1Veon0SMW3OkfSPAQJAdX5B1yYpkEgGKMU3wZa1qYbzBFm9POTPgw6gORo6xuXh1AO8jnu64w8PwALGo7fSg4bs/Whxbtd3uqcJQQ0juQ==";

	private IvParameterSpec ivspec;
	private SecretKeySpec keyspec;
	private Cipher cipher;

	private static MCrypt INSTANCE;

	static MCrypt getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new MCrypt();
		}
		return INSTANCE;
	}

	private String getSkInMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5sum = md.digest(input.getBytes());
			String output = String.format("%032X", new BigInteger(1, md5sum));
			return output.toLowerCase();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	private MCrypt() {


		final String KEY =  getSkInMD5(SECRET_KEY);
		final String iv = KEY.substring(4, 20);

		ivspec = new IvParameterSpec(iv.getBytes());
		keyspec = new SecretKeySpec(KEY.getBytes(), "AES");

		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public byte[] encrypt(String text) {
		if(text == null || text.length() == 0)
			return new byte[0];

		byte[] encrypted = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			//String paddedText = padString(text);
			encrypted = cipher.doFinal(text.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encrypted;
	}

	public byte[] decrypt(String code) {
		if(code == null || code.length() == 0)
			return new byte[0];

		byte[] decrypted = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			decrypted = cipher.doFinal(hexToBytes(code));
			//Remove trailing zeroes
			if( decrypted.length > 0) {
				int trim = 0;
				for( int i = decrypted.length - 1; i >= 0; i-- ) if( decrypted[i] == 0 ) trim++;

				if( trim > 0 ) {
					byte[] newArray = new byte[decrypted.length - trim];
					System.arraycopy(decrypted, 0, newArray, 0, decrypted.length - trim);
					decrypted = newArray;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypted;
	}

	private String bytesToHex(byte[] buf) {
		char[] chars = new char[2 * buf.length];
		for (int i = 0; i < buf.length; ++i) {
			chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
			chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
		}
		return new String(chars);
	}

	private byte[] hexToBytes(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	String encryptHex(String code) {
		byte[] encrypted = encrypt(code);
		if(encrypted == null) {
			return "";
		}
		return bytesToHex(encrypted);
	}

	String decryptHex(String code) {
		return new String(decrypt(code));
	}

	String getEncryptedSecretKey() {
		return SECRET_KEY;
	}

}
