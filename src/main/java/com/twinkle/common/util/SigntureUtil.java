package com.twinkle.common.util;

import com.twinkle.common.constant.ApiBaseConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.Arrays;
import java.util.Map;

/**
 * description: 签名工具类
 *
 * @author ：King
 * @date ：2019/1/13 10:26
 */
public class SigntureUtil {
    private static final Logger logger = LoggerFactory.getLogger(SigntureUtil.class);

    public static String APP_KEY_FLAG = "app.key";

    public static String SIGN_KEY = "x20190101";

    public static String APP_KEY="king_key";

    public static Long REQUEST_TIMEOUT = 3 * 60L;// 请求超时时间，3分钟

//    static {
//        SIGN_KEY = PropertiesUtil.getString("sign.key");
//        REQUEST_TIMEOUT = PropertiesUtil.getLong("request.timestamp.timeout");
//    }

    /**
     *
     * verify:验证签名. <br/>
     *
     * @author King
     * @param params
     * @param sign
     * @return
     */
    public static boolean verify(Map<String, String> params, String sign) {
        String timestamp = params.get(ApiBaseConstant.TIMESTAMP);
        if (params == null || StringUtils.isBlank(sign) || StringUtils.isBlank(timestamp)) {
            logger.error("签名验证失败，签名或时间戳不能为空！Params={}", params);
            return false;
        }
//        if (!params.containsKey(ApiBaseConstant.IMEI) || !params.containsKey(ApiBaseConstant.PLATFORM)
//                || !params.containsKey(ApiBaseConstant.OS_VERSION)
//                || !params.containsKey(ApiBaseConstant.APP_VERSION)) {
//            logger.error("签名验证失败，基本参数不能为空！Params={}", params);
//            return false;
//        }

        Long t = 0L;
        try {
            t = Long.valueOf(timestamp);
        } catch (NumberFormatException e) {
        }
        Long s = (System.currentTimeMillis()- t) / 1000;// 当前时间与参数中的时间的时间差（秒）
        // 时间戳3分钟内有效(默认3分钟，以配置为准)
        if (s < -REQUEST_TIMEOUT || s > REQUEST_TIMEOUT) {
            logger.error("签名验证失败，时间戳已失效！Params={}", params);
            return false;
        }
        return sign.equalsIgnoreCase(sign(params));
    }

    /**
     *
     * sign:签名. <br/>
     *
     * @author King
     * @param paramMap
     * @return
     */
    public static String sign(Map<String, String> paramMap) {
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keyArray) {
            if (key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("debug")) {
                continue;
            }
            stringBuilder.append(key).append("=").append(paramMap.get(key)).append("&");
        }

        String platform = paramMap.get(ApiBaseConstant.PLATFORM);
//        String appKey = PropertiesUtil.getString(platform.toLowerCase() + "." + APP_KEY_FLAG);
        String appKey=APP_KEY;
        stringBuilder.append(appKey);// 添加appKey
        stringBuilder.append(SIGN_KEY);// 添加signkey

        return DigestUtils.md5Hex(stringBuilder.toString());
    }
}
