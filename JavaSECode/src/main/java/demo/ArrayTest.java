package demo;

import java.util.ArrayList;
import java.util.Random;

/**
 * @Author: MaoMao
 * @date: 2022/11/1 10:25
 */
public class ArrayTest {
    public static void main(String[] args) {
        int[] ints = new int[10];
        Random random = new Random();
        random.setSeed(10);
        for (int i = 0; i < ints.length; i++) {
            ints[i] = random.nextInt(10 - 1);
        }

        //sort(ints);

    }

    private static void sort(int[] arr){
        int temp = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]){
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
