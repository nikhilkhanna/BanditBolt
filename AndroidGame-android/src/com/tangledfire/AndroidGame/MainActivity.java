package com.tangledfire.AndroidGame;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.android.vending.billing.IInAppBillingService;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tangledfire.AndroidGame.R;
public class MainActivity extends AndroidApplication {
    private ActionResolverAndroid actionResolver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String base64EncodedPublicStringKey = "";
        base64EncodedPublicStringKey +="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhq+IxSualrNqEWwCzhq6JQYIDc/1b82X+4LhipbpoYtTfqyK9e3c6ym3Xm+f0Mb3sfsvWC8YFZd97/jSKHYUxr7x";
        base64EncodedPublicStringKey +="DLreKfk7Tq06T960JGrDlUR4caQksVFM33yRIGS60aKTlOUGxZPy6ESWty2nRRhliFU1GCrJBx7wmdsOeJsLN53ngDvrr2DgkwXix9Lx6EklndHhwcpIORjtY/0Bu+UxpENuxZ6Er4a+vSG5pmLuRhExN0XYmnd6pvEJpMIozWUTOgVWmfWeOz1ygel7tWM2qwkj03AMxaC+ppXmIR9zyGBQHm6gmrZUcI0JaBlHoMt7yMOl4K+36p7cdAAW0QIDAQAB";
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        cfg.maxSimultaneousSounds = 5;
        cfg.useAccelerometer = false;
        com.facebook.AppEventsLogger.activateApp(this, "1383264605244296");
        actionResolver = new ActionResolverAndroid(this, base64EncodedPublicStringKey, this);
        AppRater.app_launched(this);
        initialize(new AndroidGame(actionResolver), cfg);
    }
    public void onDestroy()
    {
    	super.onDestroy();
    	if(actionResolver!=null)
    	actionResolver.destroy();
    	actionResolver = null;
    }
}