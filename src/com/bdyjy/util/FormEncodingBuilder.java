/**
 * FormEncodingBuilder.java[v 1.0.0]
 * class:com.bdyjy.util,FormEncodingBuilder
 * 周航 create at 2016-4-25 下午3:16:46
 */
package com.bdyjy.util;

import okio.Buffer;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

/**
 * com.bdyjy.util.FormEncodingBuilder
 * @author 周航<br/> 
 * create at 2016-4-25 下午3:16:46
 */
public final class FormEncodingBuilder {
	 private static final char[] HEX_DIGITS =
	      { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	 
	  static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
	  static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
	  static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
	  static final String QUERY_ENCODE_SET = " \"'<>#";
	  static final String QUERY_COMPONENT_ENCODE_SET = " \"'<>#&=";
	  static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
	  static final String FRAGMENT_ENCODE_SET = "";

	
	
	  private static final MediaType CONTENT_TYPE =
	      MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

	  private final Buffer content = new Buffer();

	  /** Add new key-value pair. */
	  public FormEncodingBuilder add(String name, String value) {
	    if (content.size() > 0) {
	      content.writeByte('&');
	    }
	    canonicalize(content, name, 0, name.length(),
	        FORM_ENCODE_SET, false, true);
	    content.writeByte('=');
	    canonicalize(content, value, 0, value.length(),
	        FORM_ENCODE_SET, false, true);
	    return this;
	  }

	  /** Add new key-value pair. */
	  public FormEncodingBuilder addEncoded(String name, String value) {
	    if (content.size() > 0) {
	      content.writeByte('&');
	    }
	    canonicalize(content, name, 0, name.length(),
	        FORM_ENCODE_SET, true, true);
	    content.writeByte('=');
	    canonicalize(content, value, 0, value.length(),
	        FORM_ENCODE_SET, true, true);
	    return this;
	  }

	  public RequestBody build() {
	    return RequestBody.create(CONTENT_TYPE, content.snapshot());
	  }
	  
	  static String canonicalize(String input, int pos, int limit, String encodeSet,
		      boolean alreadyEncoded, boolean query) {
		    int codePoint;
		    for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
		      codePoint = input.codePointAt(i);
		      if (codePoint < 0x20
		          || codePoint >= 0x7f
		          || encodeSet.indexOf(codePoint) != -1
		          || (codePoint == '%' && !alreadyEncoded)
		          || (query && codePoint == '+')) {
		        // Slow path: the character at i requires encoding!
		        Buffer out = new Buffer();
		        out.writeUtf8(input, pos, i);
		        canonicalize(out, input, i, limit, encodeSet, alreadyEncoded, query);
		        return out.readUtf8();
		      }
		    }

		    // Fast path: no characters in [pos..limit) required encoding.
		    return input.substring(pos, limit);
		  }

		  static void canonicalize(Buffer out, String input, int pos, int limit,
		      String encodeSet, boolean alreadyEncoded, boolean query) {
		    Buffer utf8Buffer = null; // Lazily allocated.
		    int codePoint;
		    for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
		      codePoint = input.codePointAt(i);
		      if (alreadyEncoded
		          && (codePoint == '\t' || codePoint == '\n' || codePoint == '\f' || codePoint == '\r')) {
		        // Skip this character.
		      } else if (query && codePoint == '+') {
		        // HTML permits space to be encoded as '+'. We use '%20' to avoid special cases.
		        out.writeUtf8(alreadyEncoded ? "%20" : "%2B");
		      } else if (codePoint < 0x20
		          || codePoint >= 0x7f
		          || encodeSet.indexOf(codePoint) != -1
		          || (codePoint == '%' && !alreadyEncoded)) {
		        // Percent encode this character.
		        if (utf8Buffer == null) {
		          utf8Buffer = new Buffer();
		        }
		        utf8Buffer.writeUtf8CodePoint(codePoint);
		        while (!utf8Buffer.exhausted()) {
		          int b = utf8Buffer.readByte() & 0xff;
		          out.writeByte('%');
		          out.writeByte(HEX_DIGITS[(b >> 4) & 0xf]);
		          out.writeByte(HEX_DIGITS[b & 0xf]);
		        }
		      } else {
		        // This character doesn't need encoding. Just copy it over.
		        out.writeUtf8CodePoint(codePoint);
		      }
		    }
		  }

	}