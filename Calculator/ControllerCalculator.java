import java.awt.event.*;
import javax.swing.*;

public class ControllerCalculator implements ActionListener {

    private ViewsCalculator vc;
    private ModelsCalculator mc;

    public ControllerCalculator(ViewsCalculator vc, ModelsCalculator mc) {

        this.vc = vc;
        this.mc = mc;

        vc.add.addActionListener(this);
        vc.sub.addActionListener(this);
        vc.mul.addActionListener(this);
        vc.div.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {

            double a = Double.parseDouble(vc.txta.getText());
            double b = Double.parseDouble(vc.txtb.getText());

            double res = 0;

            if(e.getSource() == vc.add) {
                res = mc.add(a,b);
            }

            if(e.getSource() == vc.sub) {
                res = mc.sub(a,b);
            }

            if(e.getSource() == vc.mul) {
                res = mc.mul(a,b);
            }

            if(e.getSource() == vc.div) {
                res = mc.div(a,b);
            }

            vc.txtResult.setText(String.valueOf(res));

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(vc,"Please enter valid numbers");

        } catch(ArithmeticException ex) {

            JOptionPane.showMessageDialog(vc,ex.getMessage());

        }
    }
}