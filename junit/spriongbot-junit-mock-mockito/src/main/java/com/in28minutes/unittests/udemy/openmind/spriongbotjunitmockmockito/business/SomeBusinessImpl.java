package com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.business;

import com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.data.SomeDataService;

public class SomeBusinessImpl {
    private SomeDataService someDataService;

    public void setSomeDataService(SomeDataService someDataService) {
        this.someDataService = someDataService;
    }

    public int calculateSum(int[] data) {
        int sum = 0;
        for(int value:data) {
            sum += value;
        }
        return sum;
        //Functional Style
        //return Arrays.stream(data).reduce(Integer::sum).orElse(0);
    }

    public int calculateSumUsingDataService() {
        int sum = 0;
        int[] data = someDataService.retrieveAllData();
        for(int value:data) {
            sum += value;
        }

        //someDataService.storeSum(sum);
        return sum;
        //Functional Style
        //return Arrays.stream(data).reduce(Integer::sum).orElse(0);
    }
}
