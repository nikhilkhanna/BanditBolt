package com.tangledfire.AndroidGame;
/**
 * This interface allows the desktop and android versions to both include methods to deal with
 * in app purchases and other platform specific functionality. 
 * @author Nikhil Khanna
 */
public interface ActionResolver {

	   public void showToast(CharSequence message, int toastDuration);
	   public void showAlertBox(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText);
	   public void showConfirmAlert(String alertBoxTitle, String alertBoxMessage, String positiveButton, String negativeButton);
	   public void openUri(String uri);
	   public void confirmexit();
	   public void submitLevel();
	   public void establishConnection();
	   public void hasLevelPack();
	   public void purchaseLevelPack();
	   public void queryInventory();
	   public void consumePack();
	}