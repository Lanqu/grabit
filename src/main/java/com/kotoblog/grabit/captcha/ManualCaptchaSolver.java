package com.kotoblog.grabit.captcha;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.kotoblog.grabit.exceptions.CaptchaGrabitException;

public class ManualCaptchaSolver extends CaptchaSolver {

	private final String url;

	public ManualCaptchaSolver(String url) {
		this.url = url;
	}

	@Override
	public String solve() throws CaptchaGrabitException {
		BufferedImage image;
		try {
			image = ImageIO.read(new URL(this.url));
		} catch (IOException e) {
			throw new CaptchaGrabitException(e);
		}
		JLabel label = new JLabel(new ImageIcon(image));
		String result = (String) JOptionPane.showInputDialog(null, label, "Enter captcha", JOptionPane.QUESTION_MESSAGE, null, null, null);
		return result;
	}

}
