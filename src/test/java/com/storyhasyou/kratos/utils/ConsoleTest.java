package com.storyhasyou.kratos.utils;

import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

/**
 * @author fangxi created by 2020/7/31
 */
public class ConsoleTest {

    @Test
    public void log() throws Exception {
        Console.log();
    }

    @Test
    public void testLog() throws Exception {
    }

    @Test
    public void print() throws Exception {
        Console.print("number: {}", 123);
    }

    @Test
    public void printProgress() throws Exception {
        Console.printProgress('#', 100);
    }

    @Test
    public void testPrintProgress() throws Exception {
    }

    @Test
    public void testLog1() throws Exception {
    }

    @Test
    public void testPrint() throws Exception {
    }

    @Test
    public void testLog2() throws Exception {
    }

    @Test
    public void error() throws Exception {
    }

    @Test
    public void testError() throws Exception {
    }

    @Test
    public void testError1() throws Exception {
    }

    @Test
    public void testError2() throws Exception {
    }

    @Test
    public void scanner() throws Exception {
    }

    @Test
    public void input() throws Exception {
    }

    @Test
    public void where() throws Exception {
    }
}