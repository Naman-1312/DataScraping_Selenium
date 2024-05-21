package PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class DoctorPage {

	 WebDriver driver;

	    @FindBy(className = "col l2 m2 s4 profileDocPic c1")
	    WebElement profilePhoto;

	    @FindBy(css = "h1.doctor-name")
	    WebElement doctorName;

	    @FindBy(css = "p.doctor-education")
	    WebElement doctorEducation;

	    @FindBy(css = "span.doctor-fees")
	    WebElement doctorFees;

	    public DoctorPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	    }

	    public String getProfilePhotoUrl() {
	        return profilePhoto.getAttribute("src");
	    }

	    public String getDoctorName() {
	        return doctorName.getText();
	    }

	    public String getDoctorEducation() {
	        return doctorEducation.getText();
	    }

	    public String getDoctorFees() {
	        return doctorFees.getText();
	    }
}
