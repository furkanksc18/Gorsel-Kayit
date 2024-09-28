package MongoDBGUIApp;

import javax.swing.*;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("MongoDB GUI Uygulaması");
        setSize(600, 400);  // Pencere boyutu
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Uygulama kapanırken belleği temizle

        // Sekmeleri oluşturmak için bir JTabbedPane (açılır sekmeler) kullanıyoruz.
        JTabbedPane tabbedPane = new JTabbedPane();

        // İlk sekme: Veri ekleme arayüzü
        JPanel insertPanel = new InsertDataPanel();  
        tabbedPane.addTab("Veri Ekle", insertPanel);

               
        // İkinci sekme: Veri okuma arayüzü 
        JPanel readPanel = new ReadDataPanel();  
        tabbedPane.addTab("Veri Oku", readPanel);

        // Sekmeleri pencereye ekle
        add(tabbedPane);
    }

    public static void main(String[] args) {
        // GUI uygulamasını başlat
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);  // Pencereyi görünür yap
        });
    }
}
