import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

// Super Class
interface WaterLevelObserver{
    void update(int waterLevel);
        //
}
// Setting Up Display Window
class DisplayWindow extends JFrame implements WaterLevelObserver{
    private JLabel displayLabel;
    DisplayWindow(){
        setSize(300, 300);
        setTitle("Display Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        displayLabel = new JLabel("0");
        displayLabel.setFont(new Font("", 1, 30));
        add(displayLabel);
        setVisible(true);
    }

    public void update(int waterLevel){
        displayLabel.setText(Integer.toString(waterLevel));
    }
}

// Setting Alarm Window
class AlarmWindow extends JFrame implements WaterLevelObserver{
    private JLabel alarmLabel;
    AlarmWindow(){
        setSize(300, 300);
        setTitle("Alarm Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        alarmLabel= new JLabel("OFF");
        alarmLabel.setFont(new Font("", 1, 30));
        add(alarmLabel);
        setVisible(true);
    }

    public void update(int waterLevel){
        alarmLabel.setText(waterLevel > 50 ? "ON": "OFF");
    }
}

// Setting up splitter window

class SplitterWindow extends JFrame implements WaterLevelObserver{
    private JLabel splitterLabel;
    SplitterWindow(){
        setSize(300, 300);
        setTitle("Splitter Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        splitterLabel= new JLabel("OFF");
        splitterLabel.setFont(new Font("", 1, 30));
        add(splitterLabel);
        setVisible(true);
    }

    public void update(int waterLevel){
        splitterLabel.setText(waterLevel > 75 ? "ON": "OFF");
    }
}

// Setting up SMSwindow

class SMSwindow extends JFrame implements WaterLevelObserver{
    private JLabel smsLabel;
    SMSwindow(){
        setSize(600, 400);
        setTitle("SMS Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        smsLabel = new JLabel("Send The SMS");
        smsLabel.setFont(new Font("null", 1, 30));
        add(smsLabel);
        setVisible(true);
    }

    public void update(int waterLevel){
        smsLabel.setText("Sending Waterlevel: "+ Integer.toString(waterLevel));
    }
}
//////// Setting Water Tank

class WaterTankWindow extends JFrame{
    private JSlider waterLevelSlider;
    private WaterTankController waterTankController;
    WaterTankWindow(WaterTankController waterTankController){
        this.waterTankController = waterTankController;
        setSize(300, 300);
        setTitle("Water Tank Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        waterLevelSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        waterLevelSlider.setMajorTickSpacing(10);
        waterLevelSlider.setPaintLabels(true);
        waterLevelSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e){
                int waterLevel = waterLevelSlider.getValue();
                waterTankController.setWaterLevel(waterLevel);
            }
        });
        add(waterLevelSlider);
    }
}

// Setting up water tank controller
class WaterTankController{
    private WaterLevelObserver[] observers = new WaterLevelObserver[0];

  
    private int waterLevel;

    public void addWaterLevelObserver(WaterLevelObserver wlobs){
        WaterLevelObserver[] temp = new WaterLevelObserver[observers.length+1];
        for (int i = 0; i < observers.length; i++){
            temp[i] = observers[i];
        }
        temp[temp.length-1] = wlobs; // Adding a water level observer
        observers = temp;
    }

     public void notifyObjects(int waterLevel){
        for (WaterLevelObserver waterLevelObserver : observers) {
            waterLevelObserver.update(waterLevel); 
        }
    }

    public void setWaterLevel(int waterLevel){
        if (this.waterLevel != waterLevel){
            this.waterLevel = waterLevel;
            notifyObjects(waterLevel);
        }
    }
    
}

public class Project{
    public static void main(String[] args) {
        WaterTankController wtc = new WaterTankController();
        wtc.addWaterLevelObserver(new DisplayWindow());
        wtc.addWaterLevelObserver(new AlarmWindow());
        wtc.addWaterLevelObserver(new SplitterWindow());
        wtc.addWaterLevelObserver(new SMSwindow());

        WaterTankWindow wtw = new WaterTankWindow(wtc);
        wtw.setVisible(true);

    }
}