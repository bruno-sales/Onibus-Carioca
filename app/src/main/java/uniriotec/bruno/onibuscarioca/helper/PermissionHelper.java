package uniriotec.bruno.onibuscarioca.helper;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {

    public static boolean validatePermissions(int requestCode, Activity activity, String[] permissions)
    {
        if(Build.VERSION.SDK_INT >= 23)
        {
            List<String> listPermissions= new ArrayList<>() ;

            for(String permission : permissions)
            {
                boolean validPermissao = ContextCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED;

                if(validPermissao == false)
                {
                    listPermissions.add(permission);
                }
            }

            if(listPermissions.isEmpty()) return true;

            String[] novasPermissoes = new String[listPermissions.size()];
            listPermissions.toArray(novasPermissoes);

            ActivityCompat.requestPermissions(activity,novasPermissoes,requestCode);
        }

        return true;
    }
}
