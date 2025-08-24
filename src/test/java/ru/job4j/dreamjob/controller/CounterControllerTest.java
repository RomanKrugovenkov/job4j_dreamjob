package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CounterControllerTest {
    private CounterController counterController;

    @BeforeEach
    public void initServices() {
        counterController = new CounterController();
    }

    @Test
    public void whenRequestCountThenGetResult() {
        var result = counterController.count();

        assertThat(result).isEqualTo("Total execute : 1");
    }
}