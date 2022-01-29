package com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.business;

import com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.data.SomeDataService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SomeBusinessMockTest {

    SomeDataService someDataServiceMock;
    SomeBusinessImpl business;
    @BeforeEach
    void initBeforeEach() {
        someDataServiceMock = mock(SomeDataService.class);
        business.setSomeDataService(someDataServiceMock);
    }

    @BeforeAll
    void initBeaforeAll() {
        business = new SomeBusinessImpl();
    }

    @Test
    public void calculateSumUsingDataService_basic() {
        when(someDataServiceMock.retrieveAllData()).thenReturn(new int[]{1,2,3});
        int actualResult = business.calculateSumUsingDataService();
        int expectedResult = 6;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void calculateSumUsingDataService_empty() {
        when(someDataServiceMock.retrieveAllData()).thenReturn(new int[]{});
        int actualResult = business.calculateSumUsingDataService();//new int[] {}
        int expectedResult = 0;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void calculateSumUsingDataService_oneValue() {
        when(someDataServiceMock.retrieveAllData()).thenReturn(new int[]{5});
        int actualResult = business.calculateSumUsingDataService();
        int expectedResult = 5;
        assertEquals(expectedResult, actualResult);
    }
}
