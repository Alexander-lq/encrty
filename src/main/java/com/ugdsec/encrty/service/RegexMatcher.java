package com.ugdsec.encrty.service;

/**
 * @className RegexMatcher
 * @description  
 * @author liuqi
 * @date 2024/7/8 18:02
 * @version v1.0
**/
public interface RegexMatcher {
    /**
     * 检查输入字符串是否与正则表达式匹配
     *
     * @param regex  正则表达式
     * @param input  输入字符串
     * @return 如果输入字符串与正则表达式匹配,返回 true,否则返回 false
     */
    boolean match(String regex, String input);

    /**
     * 获取正则表达式匹配的组信息
     *
     * @param regex  正则表达式
     * @param input  输入字符串
     * @return 匹配的组信息,如果没有匹配,返回 null
     */
    String[] getMatchGroups(String regex, String input);

    /**
     * 获取正则表达式匹配的第 n 个组信息
     *
     * @param regex  正则表达式
     * @param input  输入字符串
     * @param group  组索引,从 1 开始
     * @return 第 n 个组的匹配信息,如果没有匹配或组索引无效,返回 null
     */
    String getMatchGroup(String regex, String input, int group);
}
