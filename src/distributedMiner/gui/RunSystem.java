//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2023   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package distributedMiner.gui;

import java.awt.Font;

import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;

import client.gui.Menu;

/**
 * Created on 05/12/2023, 08:47:09
 *
 * @author IPT - computer
 * @version 1.0
 */
public class RunSystem {

    @SuppressWarnings("static-access")
	public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new ServerMiner(10_010, 0, 0).setVisible(true);
        });
        java.awt.EventQueue.invokeLater(() -> {
            new ServerMiner(10_011, 400, 0).setVisible(true);
        });
        /*
        java.awt.EventQueue.invokeLater(() -> {
            new ServerMiner(10_012, 800, 0).setVisible(true);
        });
        java.awt.EventQueue.invokeLater(() -> {
            new ServerMiner(10_013, 0, 500).setVisible(true);
        });
        java.awt.EventQueue.invokeLater(() -> {
            new ServerMiner(10_014, 400, 500).setVisible(true);
        });
        */
         java.awt.EventQueue.invokeLater(() -> {
        	 try {
     			UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 15));
     			FlatNordIJTheme.setup();
     		} catch (Exception e) {
     			e.printStackTrace();
     		}
        	 
            new Menu().frmFrame.setVisible(true);
        });
         /*
         java.awt.EventQueue.invokeLater(() -> {
        	 try {
     			UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 15));
     			FlatNordIJTheme.setup();
     		} catch (Exception e) {
     			e.printStackTrace();
     		}
        	 
             new Menu().frmFrame.setVisible(true);
         });
         */

    }

}
