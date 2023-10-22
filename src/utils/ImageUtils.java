package utils;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public class ImageUtils {
	
	public static void setIcon(AbstractButton comp, InputStream file) {
		FlatSVGIcon icon = FlatSVGIcon(file);
		comp.setIcon(icon);
    }
	
	public static FlatSVGIcon FlatSVGIcon(InputStream file) {
		FlatSVGIcon icon = null;
		try {
			icon = new FlatSVGIcon(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return icon;
    }
	
	public static String changeImage(JLabel label) {
		JFileChooser imageChooser = new JFileChooser();
		imageChooser.setCurrentDirectory(new File(MessageFormat.format("{0}\\Desktop", System.getenv("USERPROFILE"))));
		imageChooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
	    imageChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG Images", "jpg"));
	    imageChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
	    imageChooser.addChoosableFileFilter(new FileNameExtensionFilter("GIF Images", "gif"));
		int response = imageChooser.showOpenDialog(null);
		
		String newImg = "";
		if(response == JFileChooser.APPROVE_OPTION) {
			newImg = imageChooser.getSelectedFile().getAbsolutePath();
			setImageByPath(label, newImg);
		}
		
		return newImg;
    }
	
	public static byte[] iconToByteArray(String imgPath) throws IOException {
		/*
        BufferedImage bi = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", out);
        g.dispose();
        
        return out.toByteArray();
        */
		Path path = Path.of(imgPath);
        return Files.readAllBytes(path);
    }
	
	public static byte[] iconToByteArray(URL resource) throws IOException {
		return resource.openStream().readAllBytes();
	}
	
	public static void setImageByIcon(JLabel label, ImageIcon icon) {
		ImageIcon ico = new ImageIcon();
		ico.setImage(icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
		label.setIcon(ico);
    }
	
	public static void setImageByURL(JLabel label, URL url) {
		ImageIcon ico = new ImageIcon(url);
		ico.setImage(ico.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
		label.setIcon(ico);
    }
	/*
	public static void setImage(JLabel label, URL path) {
		ImageIcon ico = new ImageIcon(path);
		Image img = ico.getImage();
		Image imgScale = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon newimg = new ImageIcon(imgScale);
		label.setIcon(newimg);
    }
	*/
	/*
	public static ImageIcon iconToImageIcon(Icon icon) {
        try {
            byte[] data = iconToByteArray(icon);
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            BufferedImage img = ImageIO.read(in);
            return new ImageIcon(img);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
	*/
	private static void setImageByPath(JLabel label, String path) {
		ImageIcon ico = new ImageIcon(path);
		ico.setImage(ico.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
		label.setIcon(ico);
    }
	
}
