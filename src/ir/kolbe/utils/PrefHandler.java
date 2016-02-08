package ir.kolbe.utils;
import android.content.Context;
import android.content.SharedPreferences;


public class PrefHandler
{

    private static final String PREF_NAME = "ir.kolbe.config";
    public static final String PROPERTY_HAS_SIGNUP = "PROPERTY_HAS_SIGNUP";
    public static final String PROPERTY_EMAIL = "PROPERTY_EMAIL";
    public static final String PROPERTY_PASSWORD = "PROPERTY_PASSWORD";
    public static final String PROPERTY_NICKNAME = "PROPERTY_NICKNAME";
    public static final String PROPERTY_TOKEN = "PROPERTY_TOKEN";
    public static final String PROPERTY_ID = "PROPERTY_ID";
    
    private static PrefHandler singletonInstance;
    private static SecurePreferences sharedPref = null;

    private PrefHandler(Context context) 
    {
        //sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPref = new SecurePreferences(context, "!*g^&a(#p)", PREF_NAME);
    }

    public static synchronized void initializeInstance(Context context)
    {
        if (singletonInstance == null) 
        {
            singletonInstance = new PrefHandler(context);
        }
    }

    public static synchronized PrefHandler getInstance() 
    {
        if (singletonInstance == null) 
        {
            throw new IllegalStateException(PrefHandler.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return singletonInstance;
    }
    
    
    public void setPreference(String key, Object value)
    {
      
        SharedPreferences.Editor editor = sharedPref.edit();
        
        if(value instanceof Integer )
            editor.putInt(key, ((Integer) value).intValue());
        else if (value instanceof String)
            editor.putString(key, (String)value);
        else if (value instanceof Boolean)
            editor.putBoolean(key, (Boolean)value);
        else if (value instanceof Long)
            editor.putLong(key, (Long)value);
        
        editor.commit();
    }
    
    public int getInt(String key, int defaultValue) 
    {
        return sharedPref.getInt(key, defaultValue);
    }

    public String getString(String key, String defaultValue) 
    {
        return sharedPref.getString(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) 
    {
        return sharedPref.getBoolean(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) 
    {
        return sharedPref.getLong(key, defaultValue);
    }
    
    
    
    
    public static void clear()
    {
    	sharedPref.edit().clear().commit();
    }
    
  
    
}
