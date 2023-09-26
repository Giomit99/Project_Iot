import java.time.LocalTime;

public class MyStruct {
    private String Beacon;
    private String Aula;
    private int Postazione;
    private boolean Presenza;
    public LocalTime currenTime;

    public MyStruct(String aula, int postazione, boolean presenza, String beacon){
        Beacon= beacon;
        Aula= aula;
        Postazione= postazione;
        Presenza= presenza;
        currenTime= LocalTime.now();
    }

    public void update(boolean presenza){
        Presenza= presenza;
        currenTime= LocalTime.now();
    }

    public String getBeacon(){
        return Beacon;
    }

    public boolean getPresenza(){
        return Presenza;
    }

    public String getResults(){
        String res="";

        String disponibile;
        if(Presenza)
            disponibile= " non è disponibile";
        else
            disponibile= " è disponibile";

        res += " <b>" + Aula + "<b> con il posto: <b>" + Postazione + " <b> " + disponibile + "<br><hr><br>";
        System.out.println(res);
        return res;
    }
}
