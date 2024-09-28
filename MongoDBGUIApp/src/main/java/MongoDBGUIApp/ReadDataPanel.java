package MongoDBGUIApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.bson.Document;
import org.bson.types.Binary;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ReadDataPanel extends JPanel {
    private JTextField isimAlanı; // Kullanıcıdan isim alacağımız metin alanı
    private JLabel görsel;      // Görseli gösterecek etiket
    private MongoDatabase database; // MongoDB veritabanı bağlantısı
	private JLabel görselİsim;			// Metin alanı için
	private JButton yazBtn;

    public ReadDataPanel() {
        setLayout(new BorderLayout());

        // MongoDB bağlantısını al
        MongoDBConnection bağlantı = new MongoDBConnection("mongodb://localhost:27017", "JavaProjesi");
        database = bağlantı.getDatabase();

        // Üst kısım: İsim alanı ve veriyi okuma butonu
        JPanel üstPanel = new JPanel();
        isimAlanı = new JTextField(20); // 20 karakterlik bir metin alanı
        yazBtn = new JButton("Görseli Yükle"); //Okuma Butonu

        üstPanel.add(new JLabel("İsim: "));
        üstPanel.add(isimAlanı);
        üstPanel.add(yazBtn);
        add(üstPanel, BorderLayout.NORTH);

        // Orta kısım: Metin alanı ve görsel gösterim
        JPanel ortaPanel = new JPanel();
        ortaPanel.setLayout(new BoxLayout(ortaPanel, BoxLayout.Y_AXIS));

        // Metin alanı
        görselİsim = new JLabel();
        görselİsim.setText("Kayıt Yok");
        ortaPanel.add(görselİsim);

        // Görüntü alanı
        görsel = new JLabel();
        görsel.setPreferredSize(new Dimension(200, 200)); // Görsel alanının boyutları
        ortaPanel.add(görsel);

        add(ortaPanel, BorderLayout.CENTER);

        // Veriyi okuma butonu için tıklama dinleyici
        yazBtn.addActionListener(e -> loadData());
    }

    private void loadData() {
        String isim = isimAlanı.getText(); // Kullanıcının girdiği isim
        MongoCollection<Document> koleksiyon = database.getCollection("veriler");

        // Koleksiyondan veriyi getir
        Document dosya = koleksiyon.find(new Document("isim", isim)).first(); // İsimle eşleşen ilk belgeyi bul

        // Metin alanını temizle
        görselİsim.setText("");

        if (dosya != null) {
            // Metin verisini al ve göster
            görselİsim.setText("İsim: " + dosya.getString("isim") + "\n");

            // Görsel verisini al ve göster
            Binary görselBilgisi = dosya.get("görsel", Binary.class);
            if (görselBilgisi != null) {
                try {
                    // Görseli byte[] formatından BufferedImage formatına çevir
                    ByteArrayInputStream bis = new ByteArrayInputStream(görselBilgisi.getData());
                    BufferedImage img = ImageIO.read(bis);
                    ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                    görsel.setIcon(imageIcon);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            görselİsim.setText("Kayıt bulunamadı.\n"); // Eğer kayıt yoksa kullanıcıya bildir
            görsel.setIcon(null); // Görseli temizle
        }
    }
}
