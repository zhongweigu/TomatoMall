package com.example.tomatomall.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties("alipay")
@Component
public class AlipayUtil {
    private String appId;
    private String merchantPrivateKey;
    private String alipayPublicKey;
    private String charset;
    private String notifyUrl;
    private String returnUrl;
    private String signType;
    private String serverUrl;

}
