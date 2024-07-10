package com.ugdsec.encrty.service.impl;

import com.ugdsec.encrty.service.RegexMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern; /**
 * @className RegexMatcherImpl
 * @description  
 * @author liuqi
 * @date 2024/7/8 18:02
 * @version v1.0
**/
@Service
@RequiredArgsConstructor
@Slf4j
public class RegexMatcherImpl implements RegexMatcher {
    @Override
    public boolean match(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    @Override
    public String[] getMatchGroups(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            int groupCount = matcher.groupCount();
            String[] groups = new String[groupCount + 1];
            for (int i = 0; i <= groupCount; i++) {
                groups[i] = matcher.group(i);
            }
            return groups;
        }
        return null;
    }

    @Override
    public String getMatchGroup(String regex, String input, int group) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find() && group >= 1 && group <= matcher.groupCount()) {
            return matcher.group(group);
        }
        return null;
    }
}