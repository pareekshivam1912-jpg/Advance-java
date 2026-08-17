import javax.swing.*;

public class ViewsCalculator extends JFrame {

    JLabel a, b, result;
    JTextField txta, txtb, txtResult;
    JButton add, sub, mul, div;

    public ViewsCalculator() {

        setTitle("Calculator");
        setSize(450,300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        a = new JLabel("A");
        a.setBounds(30,30,100,30);
        add(a);

        txta = new JTextField();
        txta.setBounds(150,30,200,30);
        add(txta);

        b = new JLabel("B");
        b.setBounds(30,80,100,30);
        add(b);

        txtb = new JTextField();
        txtb.setBounds(150,80,200,30);
        add(txtb);

        result = new JLabel("Result");
        result.setBounds(30,130,100,30);
        add(result);

        txtResult = new JTextField();
        txtResult.setBounds(150,130,200,30);
        txtResult.setEditable(false);
        add(txtResult);

        add = new JButton("Add");
        add.setBounds(20,190,80,30);
        add(add);

        sub = new JButton("Subtract");
        sub.setBounds(110,190,100,30);
        add(sub);

        mul = new JButton("Multiply");
        mul.setBounds(220,190,100,30);
        add(mul);

        div = new JButton("Divide");
        div.setBounds(330,190,80,30);
        add(div);

        setVisible(true);
    }
}