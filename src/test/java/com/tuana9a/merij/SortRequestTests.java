package com.tuana9a.merij;

import com.tuana9a.merij.exceptions.SortOperationNotSupported;
import com.tuana9a.merij.exceptions.SortPatternNotMatchException;
import com.tuana9a.merij.requests.SortRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

public class SortRequestTests {
    @Test
    public void testAsc() throws SortPatternNotMatchException, SortOperationNotSupported {
        Sort sort = SortRequest.resolve("age=1").toSort();
        Assertions.assertEquals(sort, Sort.by(Sort.Direction.ASC, "age"));
    }

    @Test
    public void testDesc() throws SortPatternNotMatchException, SortOperationNotSupported {
        Sort sort = SortRequest.resolve("age=-1").toSort();
        Assertions.assertEquals(sort, Sort.by(Sort.Direction.DESC, "age"));
    }
}
