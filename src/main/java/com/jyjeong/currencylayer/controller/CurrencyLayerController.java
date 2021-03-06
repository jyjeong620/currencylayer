package com.jyjeong.currencylayer.controller;

import com.jyjeong.currencylayer.service.CurrencyLayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CurrencyLayerController {

    public final CurrencyLayerService currencylayerService;

    /**
     * 메인화면
     * @return
     */
    @GetMapping("/")
    public String main(){

        return "main";
    }

    /**
     * 국가 코드를 받아와 DB에 저장된 환율 반환
     * @param stdCountryCode 국가코드
     * @return 국가코드별 환율
     */
    @GetMapping("/currencyRate")
    @ResponseBody
    public BigDecimal getCurrencyInfo(
            @RequestParam(value = "stdCountryCode") String stdCountryCode
    ){
        log.debug("set stdCountryCode = " + stdCountryCode);
        BigDecimal currencyRate = currencylayerService.getCurrencyRate(stdCountryCode);
        if(currencyRate != null){
            return currencyRate.setScale(2, BigDecimal.ROUND_DOWN);
        }
        else {
            log.error("currencyRate is null");
            return BigDecimal.ZERO;
        }
    }

    /**
     * 입력받은 국가코드를 환율로 변환 
     * 입력받은 송금액과 환율을 이용하여 수취금액 반환 
     * @param stdCountryCode 국가코드
     * @param remittance 송금액
     * @return 수취금액
     */
    @GetMapping("/receivedAmount")
    @ResponseBody
    public BigDecimal getReceivedAmount(
            @RequestParam(value = "stdCountryCode") String stdCountryCode,
            @RequestParam(value = "remittance") BigDecimal remittance
    ){
        BigDecimal currencyRate = currencylayerService.getCurrencyRate(stdCountryCode);
        log.debug("set currencyRate : " + currencyRate);
        log.debug("set remittance : " + remittance);

        return currencylayerService.getReceivedAmount(currencyRate,remittance);
    }
}
