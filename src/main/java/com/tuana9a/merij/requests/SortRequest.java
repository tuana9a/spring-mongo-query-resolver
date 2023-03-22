package com.tuana9a.merij.requests;

import com.tuana9a.merij.exceptions.SortOperationNotSupported;
import com.tuana9a.merij.exceptions.SortPatternNotMatchException;
import org.springframework.data.domain.Sort;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortRequest {
    private String key;
    private String operator;

    public SortRequest() {
    }

    public static SortRequest resolve(String input) throws SortPatternNotMatchException {
        Pattern pattern = Pattern.compile("(\\w+\\s*)(=)(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new SortPatternNotMatchException(input);
        }
        return new SortRequest()
                .key(matcher.group(1).trim())
                .op(matcher.group(3).trim());
    }

    public SortRequest key(String key) {
        this.key = key;
        return this;
    }

    public boolean isValid() {
        return this.key != null && this.operator != null;
    }

    public SortRequest op(String operator) {
        this.operator = operator;
        return this;
    }

    public Sort toSort() throws SortOperationNotSupported {
        Sort sort;
        switch (this.operator) {
            case "1":
                sort = Sort.by(Sort.Direction.ASC, this.key);
                break;
            case "-1":
                sort = Sort.by(Sort.Direction.DESC, this.key);
                break;
            default:
                throw new SortOperationNotSupported(this.operator);
        }
        return sort;
    }

    public Sort and(Sort first) throws SortOperationNotSupported {
        Sort sort;
        switch (this.operator) {
            case "1":
                sort = first.and(Sort.by(Sort.Direction.ASC, this.key));
                break;
            case "-1":
                sort = first.and(Sort.by(Sort.Direction.DESC, this.key));
                break;
            default:
                throw new SortOperationNotSupported(this.operator);
        }
        return sort;
    }
}
