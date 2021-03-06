package nodebox.client.devicehandler;

import nodebox.client.MinimInputApplet;
import nodebox.client.NodeBoxDocument;
import nodebox.node.NodeLibrary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class AudioInputDeviceHandler implements DeviceHandler {

    private String name;
    private JFrame frame = null;

    private MinimInputApplet applet = null;

    public AudioInputDeviceHandler(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void start() {
        if (frame != null) stop();
        frame = new JFrame();
        applet = new MinimInputApplet();
        applet.init();
        frame.add(applet);
    }

    @Override
    public void stop() {
        if (frame != null) {
            applet.stop();
            applet.dispose();
            frame.dispose();
            frame = null;
        }
    }

    @Override
    public void addData(Map<String, Object> map) {
        if (applet != null && applet.getInput() != null)
            map.put(getName() + ".source", applet.getInput());
    }

    public AbstractDeviceControl createControl() {
        return new AudioInputDeviceControl(this);
    }

    private class AudioInputDeviceControl extends AbstractDeviceControl {
        private JLabel deviceNameLabel;
        private JButton startButton;
        private JButton stopButton;

        public AudioInputDeviceControl(AudioInputDeviceHandler deviceHandler) {
            super(deviceHandler);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            Dimension d = new Dimension(450, 60);
            setPreferredSize(d);
            setMaximumSize(d);
            setSize(d);

            deviceNameLabel = new JLabel(deviceHandler.getName());

            add(Box.createHorizontalStrut(10));
            add(deviceNameLabel);
            add(Box.createHorizontalStrut(5));

            startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    start();
                }
            });
            stopButton = new JButton("Stop");
            stopButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    stop();
                }
            });

            JPanel startStopPanel = new JPanel();
            startStopPanel.setLayout(new BoxLayout(startStopPanel, BoxLayout.X_AXIS));
            startStopPanel.add(startButton);
            startStopPanel.add(Box.createHorizontalStrut(5));
            startStopPanel.add(stopButton);
            startStopPanel.add(Box.createHorizontalGlue());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(startStopPanel);

            add(mainPanel);
            add(Box.createHorizontalGlue());
        }

    }
}
