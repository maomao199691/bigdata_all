package demo01;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;

/**
 * @Author: MaoMao
 * @date: 2023/4/10 21:39
 */
public class Test01 {
    public static void main(String[] args) {

        Student[] students = new Student[6];

        students[0]= new Student("小白", 5, 90.3);
        students[1]= new Student("小朱", 5, 95.3);
        students[2]= new Student("小灰", 4, 69.3);
        students[3]= new Student("小明", 3, 68.3);
        students[4]= new Student("小军", 2, 65.3);
        students[5]= new Student("小陈", 6, 80.3);


        TreeMap<Double, String> treeMap = new TreeMap<>(Comparator.reverseOrder());

        for (Student student : students) {
            treeMap.put(student.getScore(), student.getName());
        }

        Set<Double> keySet = treeMap.keySet();
        int tag = 0;
        for (Double key : keySet) {
            tag++;
            System.out.println(key + " = " + treeMap.get(key));
            if (tag == 3) break;
        }
    }
}
