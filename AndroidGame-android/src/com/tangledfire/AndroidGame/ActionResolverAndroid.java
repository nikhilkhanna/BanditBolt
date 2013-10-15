package com.tangledfire.AndroidGame;

import java.util.jar.JarEntry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.tangledfire.AndroidGame.IabHelper.OnIabPurchaseFinishedListener;
import com.tangledfire.AndroidGame.IabHelper.QueryInventoryFinishedListener;
import java.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;
public class ActionResolverAndroid implements ActionResolver, QueryInventoryFinishedListener {	   
	   Handler uiThread;
	   Context appContext;
	   Activity mainActivity;
	   IabHelper mHelper;
	   String base64EncodedPublicStringKey;
	   static boolean hasLevelPack = false;
	   public static final String SKU_LEVEL = "level_pack";
	   public Inventory myInventory;
	   public ActionResolverAndroid(Context appContext, String base64EncodedStringKey, Activity main){
	      uiThread = new Handler();
	      this.appContext = appContext;
	      base64EncodedPublicStringKey = base64EncodedStringKey;
	      mainActivity=main;
	      //queryInventory();
	      //establishConnection();
	   }
	   public void makeHelper()
	   {
	        mHelper = new IabHelper(appContext, base64EncodedPublicStringKey);
	   }
	   public void showToast(final CharSequence message, final int toastDuration) {
	      uiThread.post(new Runnable(){
	         public void run(){
	            Toast.makeText(appContext, message, toastDuration).show();
	         }
	      });
	   }

	   public void showAlertBox(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText) {
	      // TODO Auto-generated method stub
	      
	   }

	   public void openUri(String uri) {
	      // TODO Auto-generated method stub
	      
	   }

	   public void showConfirmAlert(final String alertBoxTitle, final String alertBoxMessage, final String positiveButton, final String negativeButton) {
	      
	   }

	   public void confirmexit() {
	      uiThread.post(new Runnable() {
	            public void run() {
	                    AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	                    builder.setMessage("Are you sure you want to quit")
	                          .setCancelable(false)
	                          .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                     
	                             public void onClick(DialogInterface dialog, int which) {
	                                Gdx.app.exit();
	                             }
	                          })
	                          .setNegativeButton("No", new DialogInterface.OnClickListener(){
	                             public void onClick(DialogInterface dialog, int which) {
	                                dialog.cancel();
	                             }
	                          });
	                    AlertDialog alert = builder.create();
	                    alert.show();
	            }
	      });
	   }

	   public void submitLevel() {
	      
	   }
		public void establishConnection() {
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				   public void onIabSetupFinished(IabResult result) {
				      if (!result.isSuccess()) {
				    	  AndroidGame.didConnect = false;
				      }            
				         AndroidGame.didConnect = true; 
				   }
				});			
		}
		public void destroy()
		{
			   if (mHelper != null) mHelper.dispose();
			   mHelper = null;
		}

		@Override
		public void purchaseLevelPack() {
//		    makeHelper();
//			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//				   public void onIabSetupFinished(IabResult result) {
//				      if (!result.isSuccess()) {
//				    	  AndroidGame.didConnect = false;
//				      }            
//				         AndroidGame.didConnect = true; 
//						mHelper.launchPurchaseFlow(mainActivity, SKU_LEVEL, 101,   
//									   mPurchaseFinishedListener, "");
//				   }
//				});	
			mHelper.launchPurchaseFlow(mainActivity, SKU_LEVEL, 101,   
					   mPurchaseFinishedListener, "");
			makeHelper();
			establishConnection();
		}
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			  if (result.isFailure()) {
				  return;
			  }
			      else {
			        // does the user have the premium upgrade?
			        if(inventory.hasPurchase(SKU_LEVEL))
			        {
			        	AndroidGame.unlockLevels();
			        }
			        myInventory = inventory;
			        // update UI accordingly
			      }			
		}
		@Override
		public void queryInventory() {
			// TODO Auto-generated method stub
		    makeHelper();
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				   public void onIabSetupFinished(IabResult result) {
				      if (!result.isSuccess()) {
				    	  AndroidGame.didConnect = false;
				      }            
				         AndroidGame.didConnect = true; 
				         mHelper.queryInventoryAsync(mGotInventoryListener);
				   }
				});		
		}
		IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
	        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
	            if (result.isFailure()) {
	                // handle error here
	              }
	              else {
	                // does the user have the premium upgrade?
	              if(inventory.hasPurchase(SKU_LEVEL))
	            	 AndroidGame.unlockLevels();   
	              myInventory = inventory;
	                // update UI accordingly
	              }
	        }
	        };
	        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
	            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
				      if (result.isFailure()) {
				    	  return;
				      }      
				      else if (purchase.getSku().equals(SKU_LEVEL)) {
				    	  AndroidGame.unlockLevels();
				      }
	            }
	            };

		@Override
		public void hasLevelPack() {
			// TODO Auto-generated method stub
			makeHelper();
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				   public void onIabSetupFinished(IabResult result) {
				      if (!result.isSuccess()) {
				    	  AndroidGame.didConnect = false;
				      }            
				         AndroidGame.didConnect = true; 
				         mHelper.queryInventoryAsync(mGotInventoryListener);
				   }
				});		
		}
		@Override
		public void consumePack() {
		    makeHelper();
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				   public void onIabSetupFinished(IabResult result) {
				      if (!result.isSuccess()) {
				    	  AndroidGame.didConnect = false;
				      }            
				         AndroidGame.didConnect = true; 
				         if(myInventory.hasPurchase(SKU_LEVEL))
						mHelper.consumeAsync(myInventory.getPurchase(SKU_LEVEL), null);
				   }
				});	
		}
		IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
				   new IabHelper.OnConsumeFinishedListener() {
				   public void onConsumeFinished(Purchase purchase, IabResult result) {
				      if (result.isSuccess()) {
				         // provision the in-app purchase to the user
				         // (for example, credit 50 gold coins to player's character)
				      }
				      else {
				         // handle error
				      }
				   }
				};
	}