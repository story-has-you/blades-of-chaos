package com.storyhasyou.kratos.config;

import com.google.common.collect.Lists;
import com.storyhasyou.kratos.annotation.EnableBladesOfChaos;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author fangxi created by 2023/10/11
 */
public class BladesOfChaosSelector extends AdviceModeImportSelector<EnableBladesOfChaos> {

    @Override
    protected String[] selectImports(@NotNull AdviceMode adviceMode) {
        List<String> result = Lists.newArrayList();
        result.add(BladesOfChaosConfig.class.getName());
        result.add(ThreadPoolConfiguration.class.getName());
        return StringUtils.toStringArray(result);
    }
}
