package com.tangledfire.AndroidGame;

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