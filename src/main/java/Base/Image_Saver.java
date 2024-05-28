package Base;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;

import org.openqa.selenium.By;

public class Image_Saver {
    public static void main(String[] args) {

//    	To Fetch Doctor name
//    	String doctorName = element.findElement(By.xpath(".//h4")).getAttribute("innerText");
    	// URL of the image
        String imageUrl = "https://files.kivihealth.com/cache/profile_pic/20170403020130_Divyaroop%20Rai.jpg";

        // Path to save the downloaded image
        String downloadPath = "C:\\Users\\naman\\Desktop\\Doctor Images\\Divyaroop_Rai_image.jpg";  // Change this path as needed

        try {
            // Open a connection to the image URL
            URL url = new URL(imageUrl);
            try (InputStream in = url.openStream();
                 FileOutputStream out = new FileOutputStream(downloadPath)) {
                // Copy the image from the URL to the specified path
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer))!= -1) {
                    out.write(buffer, 0, bytesRead);
                }
                System.out.println("Image downloaded successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}