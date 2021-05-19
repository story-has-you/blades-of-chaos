package com.storyhasyou.kratos.dto;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * The type Business config response vo.
 *
 * @author fangxi created by 2021/5/12
 */
@Slf4j
@Data
public class BusinessConfigResponseVO {

    /**
     * The Lock car.
     */
    private Boolean lockCar;

    /**
     * The Credit.
     */
    private Boolean credit;

    /**
     * The Messages.
     */
    private List<String> messages = Lists.newArrayList();

    /**
     * The Terminal id.
     */
    private Long terminalId;


    /**
     * Build credit error.
     *
     * @param message the message
     */
    public void buildCreditError(String message) {
        log.info("无法开通消贷业务, 原因: {}, terminalId: {}", message, terminalId);
        this.setCredit(false);
        this.getMessages().add(message);
    }

    /**
     * Build credit success.
     */
    public void buildCreditSuccess() {
        this.setCredit(true);
    }

    /**
     * Build credit success.
     *
     * @param message the message
     */
    public void buildCreditSuccess(String message) {
        this.setCredit(true);
        this.getMessages().add(message);
    }

    /**
     * Build lock car error.
     *
     * @param message the message
     */
    public void buildLockCarError(String message) {
        log.info("锁车版本不满足, 原因: {}, terminalId: {}", message, terminalId);
        this.setLockCar(false);
        this.getMessages().add(message);
    }

    /**
     * Build lock car success.
     */
    public void buildLockCarSuccess() {
        this.setLockCar(true);
    }

    /**
     * Build lock car success.
     *
     * @param message the message
     */
    public void buildLockCarSuccess(String message) {
        this.setLockCar(true);
        this.getMessages().add(message);
    }
}
