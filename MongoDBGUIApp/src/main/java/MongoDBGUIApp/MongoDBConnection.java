package MongoDBGUIApp;

import com.mongodb.client.MongoClient;                // MongoClient sınıfı
import com.mongodb.client.MongoClients;               // MongoClient'i oluşturmak için kullanılan sınıf
import com.mongodb.client.MongoDatabase;              // Seçilen veritabanını temsil eden sınıf

public class MongoDBConnection {
    private MongoClient mongoClient;          // MongoDB sunucusu ile bağlantıyı temsil eden nesne.
    private MongoDatabase database;            // Seçilen veritabanını temsil eden nesne.
    
    // Constructor: MongoDB sunucusuna bağlantı kurar.
    public MongoDBConnection(String uri, String dbName) {
        mongoClient = MongoClients.create(uri);  				// MongoClient oluşturuluyor.
        database = mongoClient.getDatabase(dbName);               // Belirtilen veritabanına eriş.
    }

    // Bağlı olduğun veritabanını döner.
    public MongoDatabase getDatabase() {
        return database;
    }

    // Bağlantıyı kapatır.
    public void close() {
        mongoClient.close(); // MongoDB bağlantısını kapat.
    }

    // Uygulamanın başlangıç noktası.
    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017"; // MongoDB sunucunun URI'si
        String dbName = "JavaProjesi";       // Oluşturduğun veritabanı adı.

        // MongoDBConnection nesnesini oluştur ve veritabanı bağlantısını kur.
        MongoDBConnection connection = new MongoDBConnection(uri, dbName);
        // Bağlantı başarılıysa, veritabanı adını konsola yazdır.
        System.out.println("Database connected: " + connection.getDatabase().getName());
        connection.close(); // Bağlantıyı kapat.
    }
}
