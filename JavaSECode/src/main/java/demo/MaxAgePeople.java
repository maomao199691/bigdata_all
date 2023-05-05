package demo;

/**
 * @Author: MaoMao
 * @date: 2022/10/24 12:51
 */
public class MaxAgePeople {
    public static void main(String[] args) {
        People sun = new People("sun", 22, null);
        People zhang = new People("zhang", 21, sun);
        People wang = new People("wang", 19, zhang);
        People li = new People("li", 18, wang);

        People p, q = null;
        int m = 0;
        p = li;
        while (p.next != null){
            if (m < p.age){
                q = p;
                p = p.next;
            }else{
                p = p.next;
            }
            m = q.age;
        }

        String name = null;
        int age = 0;
        if (p.next == null){
            name = q.age > p.age ? q.name:p.name;
            age = q.age > p.age ? q.age: p.age;;
        }
        System.out.println(name + "," + age);
    }
}

class People{
    String name;
    int age;
    People next;

    public People() {
    }

    public People(String name, int age, People next) {
        this.name = name;
        this.age = age;
        this.next = next;
    }
}
