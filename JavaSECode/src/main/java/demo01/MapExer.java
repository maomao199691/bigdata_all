package demo01;

import java.util.*;

/**
 * @Author: MaoMao
 * @date: 2023/4/10 21:00
 */
class Student {
    private String name;
    private int grend;
    private double score;

    public Student() {
    }

    public Student(String name, int grend, double score) {
        this.name = name;
        this.grend = grend;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrend() {
        return grend;
    }

    public void setGrend(int grend) {
        this.grend = grend;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", grend=" + grend +
                ", score=" + score +
                '}';
    }
}

public class MapExer {
    public static void main(String[] args) {
        Student[] students = new Student[6];

        students[0]= new Student("小白", 5, 90.3);
        students[1]= new Student("小朱", 5, 95.3);
        students[2]= new Student("小灰", 4, 69.3);
        students[3]= new Student("小明", 3, 68.3);
        students[4]= new Student("小军", 2, 65.3);
        students[5]= new Student("小陈", 6, 80.3);


        TreeMap<Double,String> map = new TreeMap<Double,String>(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                double n = o1 - o2;
                if (n == 0){
                    return -1;
                }
                return (int)-n;
            }
        });

        for (int i = 0; i < students.length; i++) {
            map.put(students[i].getScore(),students[i].getName());

           /* System.out.println(students[i].getScore() + " -> " + students[i].getName());
            System.out.println(map);*/
        }

        for (Double key : map.keySet()) {
            System.out.println("key: " + key + " -> " + map.get(key));
        }
        /*System.out.println(map);


        Set<Map.Entry<Double, String>> entries = map.entrySet();

        int count = 0;
        for (Map.Entry<Double, String> entry : entries) {
            Double key = entry.getKey();
            String value = entry.getValue();

            count++;

            if (count <= 3){
                System.out.println(key + " = " + value);
            }
        }*/


        /*Set<Double> keySet = map.keySet();
        Collection<String> values = map.values();


        Iterator<Double> iterator = keySet.iterator();
        int count = 0;
        while (iterator.hasNext()){
            Double s = iterator.next();


            String name = map.get(s);
            count++;

            if (count < 3){
                System.out.println(s+ " = " + name);
            }
        }*/
    }
}