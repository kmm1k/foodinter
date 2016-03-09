package ee.ttu.foodinter;

import com.firebase.client.Firebase;

/**
 * Created by kmm on 09.03.2016.
 */
public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

    }
}
