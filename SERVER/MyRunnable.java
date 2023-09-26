import java.time.LocalTime;

public class MyRunnable implements Runnable {
    String name;

    public MyRunnable(String nameF ){
        name= nameF;
    }

    public void run(MyStruct[] Aule) {
        while(true){
            for(MyStruct aula: Aule){
                aula.currenTime= LocalTime.now();
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
}
