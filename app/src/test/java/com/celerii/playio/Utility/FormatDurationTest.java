package com.celerii.playio.Utility;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class FormatDurationTest {

    @Test
    public void formatDurationShouldPass() {
        String formattedDuration = FormatDuration.formatDuration(300);
        assertThat(formattedDuration, is(equalTo("5:00")));
    }

    @Test
    public void formatDurationShouldFail() {
        String formattedDuration = FormatDuration.formatDuration(240);
        assertThat(formattedDuration, not(equalTo("5:00")));
    }

    @Test
    public void makeTwoDigitsTest() {
        String twoDigits = FormatDuration.makeTwoDigits(0);
        assertThat(twoDigits, is(equalTo("00")));
    }
}