// src/app/Main.java
package app;

import javax.swing.SwingUtilities;
import ui.UI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI ventana = new UI();
            ventana.setVisible(true);
        });
    }
}
