package MongoDBGUIApp;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bson.Document;
import org.bson.types.Binary;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class InsertDataPanel extends JPanel {

    private JTextField isimAlanı;
    private JLabel görsel,görselİsim;
    private JButton yükleBtn, kayıtBtn;
    private String görselYolu;
    private MongoDatabase database;

    public InsertDataPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // MongoDB bağlantısını al
        MongoDBConnection bağlantı = new MongoDBConnection("mongodb://localhost:27017", "JavaProjesi");
        database = bağlantı.getDatabase();

        // Metin alanı
        isimAlanı = new JTextField(20);
        add(new JLabel("İsim:"));
        add(isimAlanı);

        // Görüntü alanı
        görselİsim = new JLabel("Görsel seçilmedi.");
        görselİsim.setPreferredSize(new java.awt.Dimension(200, 200));  // Görsel alanı boyutu
        add(görselİsim);
        görsel = new JLabel();
        görsel.setPreferredSize(new java.awt.Dimension(200, 200));  // Görsel alanı boyutu
        add(görsel);


        // Resim yükleme butonu
        yükleBtn = new JButton("Resim Yükle");
        add(yükleBtn);

        // Kayıt butonu
        kayıtBtn = new JButton("Kaydet");
        add(kayıtBtn);

        // Resim yükleme işlemi için butona tıklama dinleyici
        yükleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser dosyaSeçici = new JFileChooser();  // Resim seçici
                int sonuç = dosyaSeçici.showOpenDialog(null);
                if (sonuç == JFileChooser.APPROVE_OPTION) {
                    File seçilenDosya = dosyaSeçici.getSelectedFile();
                    görselYolu = seçilenDosya.getAbsolutePath();
                    
                    ImageIcon imageIcon = new ImageIcon(görselYolu);  // Resmi ImageIcon'a dönüştür
                    Image img = imageIcon.getImage();  // Orijinal resmi al
                    Image ölçeklenmişİmg = img.getScaledInstance(görselİsim.getWidth(), görselİsim.getHeight(), Image.SCALE_AREA_AVERAGING);  // Resmi ölçeklendir
                    görselİsim.setText(seçilenDosya.getName());
                    görsel.setIcon(new ImageIcon(ölçeklenmişİmg));                 }
            }
        });

        // Kayıt butonu için tıklama dinleyici
        kayıtBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isim = isimAlanı.getText();  // Kullanıcının girdiği ismi al
                if (görselYolu != null && !isim.isEmpty()) {
                    try {
                        // Görseli dosyadan oku ve binary veriye çevir
                        File img = new File(görselYolu);
                        byte[] imgBilgisi = Files.readAllBytes(img.toPath());

                        // MongoDB'ye eklemek için bir belge (Document) oluştur
                        MongoCollection<Document> koleksiyon = database.getCollection("veriler");
                        Document dosya = new Document("isim", isim)
                                       .append("görsel", new Binary(imgBilgisi));

                        // Veriyi koleksiyona ekle
                        koleksiyon.insertOne(dosya);
                        JOptionPane.showMessageDialog(null, "Veri MongoDB'ye kaydedildi.");
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null, "Görseli okurken bir hata oluştu: " + ioException.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen bir isim ve resim seçin!");
                }
            }
        });
    }
}
