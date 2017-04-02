/*
 * Copyright © 2014 - 2017 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.wurstclient.updater;

import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Font;

public class ProgressDialog extends JDialog
{
	private JLabel lblLine1;
	private JLabel lblLine2;
	
	/**
	 * Create the dialog.
	 */
	public ProgressDialog()
	{
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Wurst Updater");
		setResizable(false);
		setUndecorated(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(screen.width - 450, 0, 450, 200);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights =
			new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblWurstLogo = new JLabel((String)null);
		lblWurstLogo.setIcon(new ImageIcon(ProgressDialog.class
			.getResource("/tk/wurst_client/updater/wurst_253x64.png")));
		GridBagConstraints gbc_lblWurstLogo = new GridBagConstraints();
		gbc_lblWurstLogo.insets = new Insets(0, 0, 5, 0);
		gbc_lblWurstLogo.gridx = 0;
		gbc_lblWurstLogo.gridy = 1;
		getContentPane().add(lblWurstLogo, gbc_lblWurstLogo);
		
		lblLine1 = new JLabel((String)null);
		lblLine1.setFont(new Font("Segoe UI", Font.PLAIN, 32));
		GridBagConstraints gbc_lblLine1 = new GridBagConstraints();
		gbc_lblLine1.insets = new Insets(0, 0, 5, 0);
		gbc_lblLine1.gridx = 0;
		gbc_lblLine1.gridy = 2;
		getContentPane().add(lblLine1, gbc_lblLine1);
		
		lblLine2 = new JLabel((String)null);
		lblLine2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		GridBagConstraints gbc_lblLine2 = new GridBagConstraints();
		gbc_lblLine2.insets = new Insets(0, 0, 5, 0);
		gbc_lblLine2.gridx = 0;
		gbc_lblLine2.gridy = 3;
		getContentPane().add(lblLine2, gbc_lblLine2);
	}
	
	public void setLine1(String line1)
	{
		lblLine1.setText(line1);
		
		if(line1 != null)
			System.out.println(line1);
	}
	
	public void setLine2(String line2)
	{
		lblLine2.setText(line2);
		
		if(line2 != null)
			System.out.println(line2);
	}
}
