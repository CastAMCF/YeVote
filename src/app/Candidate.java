package app;

import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

import utils.ImageUtils;

public class Candidate implements Serializable{

	private static final long serialVersionUID = 1L;
	
	String name;
	String code;
	byte[] image;

	public Candidate(String name, String code, String imagePath) throws IOException {
		this.name = name;
		this.code = code;
		setImage(imagePath);
	}
	
	public Candidate(String name, String code, byte[] image) throws IOException {
		this.name = name;
		this.code = code;
		this.image = image;
	}
	
	public Candidate(String name, String code) throws IOException {
		this.name = name;
		this.code = code;
		setImage();
	}

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ImageIcon getImage() {
        return new ImageIcon(image);
    }
    
    public byte[] getImageBytes() {
        return image;
    }

    public void setImage(String imagePath) throws IOException {
        this.image = ImageUtils.iconToByteArray(imagePath);
    }
    
    public void setImage() throws IOException {
        this.image = ImageUtils.iconToByteArray(Candidate.class.getResource("/images/candidate_default.png"));
    }
    
    public String toString(){
        return code + " - " + name;
    }

}
