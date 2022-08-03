package dataSearch.framework.util;

import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;

public class MessageUtil {
	/**
     * MessageSourceAccessor
     */
    private static MessageSourceAccessor msAcc = null;
    
    public void setMessageSourceAccessor(MessageSourceAccessor msAcc) {
    	MessageUtil.msAcc = msAcc;
    }
    
    /**
     * KEY?�� ?��?��?��?�� 메세�? 반환
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        return msAcc.getMessage(key, Locale.getDefault());
    }
    
    /**
     * KEY?�� ?��?��?��?�� 메세�? 반환
     * @param key
     * @param objs
     * @return
     */
    public static String getMessage(String key, Object[] objs) {
        return msAcc.getMessage(key, objs, Locale.getDefault());
    }
}
