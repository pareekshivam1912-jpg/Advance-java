public class RunCalculator {

    public static void main(String[] args) {

        ModelsCalculator model = new ModelsCalculator();
        ViewsCalculator view = new ViewsCalculator();

        new ControllerCalculator(view, model);

        view.setVisible(true);
    }
}